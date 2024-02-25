import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.File;
//import java.io.IOException;

public class Controller extends JPanel {
        private int FPS = 2;
        private Timer timer;

        private int userHeading = 1;
        private int initBearing = 1;
        private int startX = 300;
        private int startY = 300;
        private int segDistance = 30;
        private int segCount = 4;

        private int spritePxls = 32;  //32 x 32 icons in 30x30 pixel grid gives slight overlap

        private JLabel[] caterpillarLabels;
        private ImageIcon[] headIcon;
        private ImageIcon bodyIcon;
        private ImageIcon collisionIcon;

        private ArrayList<JLabel> leafLabels;
        private ArrayList<int[]> leafPositions;
        private int leafNum = 5;
        private int leafStartNum = 5;
        private ImageIcon leafIcon;
        private Clip leafNoise;

        private int score = 0;
        private int level = 1;
        private boolean gameOn = false;

        private String[] backgrounds = {"assets/barren.png", "assets/patchy-grass.png", "assets/grassy.png", "assets/grassy-leafy.png", "assets/jungle.png"};
        private BufferedImage backgroundImage;
        private int gameAreaX = 600;  //this is 20 divisions of 30 pixels each
        private int gameAreaY = 510;  //this is 17 divisions of 30 pixels each

        public JPanel controllerPanel;
        Action rightAction;
        Action leftAction;
        Action upAction;
        Action downAction;
        Action pauseAction;
        Action resetAction;
        Action quitAction;
        Caterpillar kate;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    class PauseAction extends AbstractAction{
        public void actionPerformed(ActionEvent e){
            if (timer.isRunning()){
                timer.stop();
            }
            else{
                userHeading = kate.getBearing();
                if (gameOn){timer.start();}
            }
        }
    }

    class ResetAction extends AbstractAction{
        public void actionPerformed(ActionEvent e){
            timer.stop();

            FPS = 2;
            timer = new Timer(1000/FPS, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    animate();}});
   
            for (int x = 0; x < leafNum; x++){  //clean up all these leaves :s
                leafLabels.get(x).setVisible(false);
            }
            for (int x = 0; x < kate.getLength(); x++){
                caterpillarLabels[x].setVisible(false);
            }

            leafNum = leafStartNum;
            
            resetCaterpillar();

            level = 1;
            score = 0;
            levelStart();
        }
    }

    class QuitAction extends AbstractAction{
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }

    class LeftAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            userHeading = 3;
        }
    }

    class RightAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            userHeading = 1;
        }
    }

    class UpAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            userHeading = 0;
        }
    }

    class DownAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            userHeading = 2;
        }
    }

    public Controller() {

        InputMap input = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap action = getActionMap();

        //load the images
        headIcon = new ImageIcon[4];
        for (int x = 0; x < 4; x++){
            String numString = Integer.toString(x);
            headIcon[x] = new ImageIcon("assets/" + numString + ".png");
        }
        bodyIcon = new ImageIcon("assets/segment.png");
        collisionIcon = new ImageIcon("assets/collision.png");
        leafIcon = new ImageIcon("assets/leaf.png");

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("assets/crunch.wav").getAbsoluteFile());
            leafNoise = AudioSystem.getClip();
            leafNoise.open(audioInputStream);            
        } catch (Exception e) {
            e.printStackTrace();
        }

        pauseAction = new PauseAction();
        resetAction = new ResetAction();
        quitAction = new QuitAction();
        rightAction = new RightAction();
        leftAction = new LeftAction();
        upAction = new UpAction();
        downAction = new DownAction();

            input.put(KeyStroke.getKeyStroke("Q"), "quitAction");
            action.put("quitAction", quitAction);
            input.put(KeyStroke.getKeyStroke("R"), "resetAction");
            action.put("resetAction", resetAction);
            input.put(KeyStroke.getKeyStroke("P"), "pauseAction");
            action.put("pauseAction", pauseAction);
            input.put(KeyStroke.getKeyStroke("UP"), "upAction");
            action.put("upAction", upAction);
            input.put(KeyStroke.getKeyStroke("W"), "upAction");
            action.put("upAction", upAction);
            input.put(KeyStroke.getKeyStroke("DOWN"), "downAction");
            action.put("downAction", downAction);
            input.put(KeyStroke.getKeyStroke("S"), "downAction");
            action.put("downAction", downAction);
            input.put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
            action.put("rightAction", rightAction);
            input.put(KeyStroke.getKeyStroke("D"), "rightAction");
            action.put("rightAction", rightAction);
            input.put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
            action.put("leftAction", leftAction);
            input.put(KeyStroke.getKeyStroke("A"), "leftAction");
            action.put("leftAction", leftAction);

        setLayout(null);
        setVisible(true);
        setBorder(BorderFactory.createLineBorder(Color.black));

        timer = new Timer(1000/FPS, new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    animate();}});
        
        resetCaterpillar();
        levelStart();

    }

    public void animate(){

        int[][] positions = kate.getPos(); //each body segment's location

        int headX = positions[0][0];
        int headY = positions[0][1];

        int[] lastSegmentLocation = positions[kate.getLength()-1];

        if (userHeading != kate.getBearing()){
            if (validityCheck(headX, headY, userHeading, kate.getBearing())){
                kate.setBearing(userHeading);
            }
        }
    
        switch(kate.getBearing()){

            case 0: //heading NORTH
                if (validityCheck(headX, headY, 0, 0)){                                //check for collision before moving
                    kate.slither();}
                else {
                    userHeading = (headX > gameAreaX/2 ? 3 : 1);       //if collision is imminent, pick a new bearing
                    kate.setBearing(userHeading);
                    kate.slither();}
                break;

            case 1:
                if (validityCheck(headX, headY, 1, 1)){
                    kate.slither();}
                else {
                    userHeading = (headY > gameAreaY/2 ? 0 : 2);
                    kate.setBearing(userHeading);
                    kate.slither();}
                break;

            case 2:
                if (validityCheck(headX, headY, 2, 2)){
                    kate.slither();}
                else {
                    userHeading = (headX > gameAreaX/2 ? 3 : 1);
                    kate.setBearing(userHeading);
                    kate.slither();}
                break;

            case 3:
                if (validityCheck(headX, headY, 3, 3)){
                    kate.slither();}
                else {
                    userHeading = (headY > gameAreaY/2 ? 0 : 2);
                    kate.setBearing(userHeading);
                    kate.slither();}
                break;
        }

        //check for collisions with new positions
        positions = kate.getPos();
        int[] headPos = positions[0];

        //check for leaf collisions
        for (int x = 0; x < leafNum; x++){
            int[] leafPos = leafPositions.get(x);
            if ((leafPos[0] == headPos[0]) && (leafPos[1] == headPos[1])){
                //System.out.println("Got leaf at: " + leafPos[0] + ", " + leafPos[1]);
                playLeafSound();
                leafPositions.remove(x);
                leafLabels.get(x).setVisible(false);
                leafLabels.remove(x);
                kate.grow(lastSegmentLocation);
                addSegmentLabel(lastSegmentLocation);
                leafNum--;
                score++;
                break;
            }
        }

        //switch to the appropriate image for the direction
        caterpillarLabels[0].setIcon(headIcon[kate.getBearing()]);

        //Move the body
        positions = kate.getPos();
        for (int x = 0; x < kate.getLength(); x++){  //Iterate over segment labels and move
            caterpillarLabels[x].setLocation(positions[x][0], positions[x][1]);
        }

        //Check for head/body collision!!
        for (int x = 1; x < kate.getLength(); x++){
            if ((headPos[0] == positions[x][0]) && (headPos[1] == positions[x][1])) { //collision!
                caterpillarLabels[0].setIcon(collisionIcon);
                gameOver();
            }
        }

        //check if leaves are left!
        if (leafNum <= 0){
            timer.stop();
            FPS++;
            level++;
            levelStart();
        }

        repaint();

    }

    public void resetCaterpillar(){
        removeAll();
        super.removeAll();

        kate = new Caterpillar(startX, startY, segDistance, segCount, initBearing); //x, y, segDistance, segCount, bearing
        caterpillarLabels = new JLabel[segCount];
        int[][] positions = kate.getPos(); //stores each body segment's location
        caterpillarLabels[0] = new JLabel(headIcon[initBearing]);
            caterpillarLabels[0].setBounds(positions[0][0], positions[0][1], spritePxls, spritePxls);
            caterpillarLabels[0].setOpaque(false);
            add(caterpillarLabels[0]);

        for (int x = 1; x < segCount; x++){
            caterpillarLabels[x] = new JLabel(bodyIcon);
            caterpillarLabels[x].setBounds(positions[x][0], positions[x][1], spritePxls, spritePxls);
            caterpillarLabels[x].setOpaque(false);
            add(caterpillarLabels[x]);
        }
    }

    public void levelStart(){
        try{
            backgroundImage = ImageIO.read(new File(backgrounds[level-1]));}
        catch(Exception e){
            System.out.println("Error loading background!");}

        timer = new Timer(1000/FPS, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                animate();}});

        leafNum = level * leafStartNum;
        generateLeaves(leafNum);

        JLabel levelMarker = new JLabel();
            levelMarker.setText("Level " + level);
            levelMarker.setFont(new Font("Arial", Font.BOLD, 54));
            levelMarker.setForeground(Color.ORANGE);
            levelMarker.setOpaque(false);
            add(levelMarker);
            levelMarker.setBounds(200,115,375,100);
            levelMarker.setVisible(true);
            setComponentZOrder(levelMarker, 0);
        repaint();

        ActionListener listener = new ActionListener(){
            public void actionPerformed(ActionEvent event){
                levelMarker.setVisible(false);
                userHeading = kate.getBearing();
                gameOn = true;
                timer.start();
            }
        };

        Timer t = new Timer(3000, listener);
        t.setRepeats(false);
        t.start();
    }

    public void gameOver(){
        timer.stop();
        gameOn = false;

        JLabel endLabel = new JLabel();  //add a game over label
            endLabel.setText("Game over");
            endLabel.setFont(new Font("Arial", Font.BOLD, 54));
            endLabel.setForeground(Color.ORANGE);
            endLabel.setOpaque(false);
            add(endLabel);
            endLabel.setBounds(160,120,375,100);
            endLabel.setVisible(true);
            setComponentZOrder(endLabel, 0);

        JLabel scoreLabel = new JLabel();  //show the score
            scoreLabel.setText("Score: " + score);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 54));
            scoreLabel.setForeground(Color.ORANGE);
            scoreLabel.setOpaque(false);
            add(scoreLabel);
            scoreLabel.setBounds(195,190,375,100);
            scoreLabel.setVisible(true);
            setComponentZOrder(scoreLabel, 1);
    }

    public boolean validityCheck(int headX, int headY, int userHeading, int kateBearing){
        switch(userHeading){
            case 0: //heading NORTH
                if ((headY > 0) && (kateBearing != 2)){                                //check for collision before moving
                    return true;}
                else {
                    return false;}
            
            case 1:
                if ((headX < (gameAreaX - spritePxls)) && (kateBearing != 3)){
                    return true;}
                else {
                    return false;}

            case 2:
                if ((headY + 30 < (gameAreaY - spritePxls)) && (kateBearing != 0)){
                    return true;}
                else {
                    return false;}

            case 3:
                if ((headX > 0) && (kateBearing != 1)){
                    return true;}
                else {
                    return false;}
        }
        return false;
    }

    public void addSegmentLabel(int[] tailLoc){
        int len = kate.getLength();
        JLabel[] newArray = new JLabel[len];
        for (int x = 0; x < len-1; x++){
            newArray[x] = caterpillarLabels[x];
        }
        newArray[len-1] = new JLabel(bodyIcon);
        newArray[len-1].setBounds(tailLoc[0], tailLoc[1], spritePxls, spritePxls);
        newArray[len-1].setOpaque(false);
        newArray[len-1].setVisible(true);
        add(newArray[len-1]);
        caterpillarLabels = newArray;
    }

    public void generateLeaves(int leafNum){
        leafLabels = new ArrayList<JLabel>(leafNum);
        leafPositions = new ArrayList<int[]>(leafNum);

        Random random = new Random();
        int rand_x;
        int rand_y;
        
        for (int leaf = 0; leaf < leafNum; leaf++){
            while(true){  //loop until valid x, y
                boolean duped = false;
                rand_x = random.nextInt(gameAreaX/30) * 30;
                rand_y = random.nextInt((gameAreaY-30)/30) * 30;
                for (int[] leafPos : leafPositions){
                    if ((rand_x == leafPos[0]) && (rand_y == leafPos[1])){  //check for duplicates!!
                        duped = true;
                    }
                }
                if (duped) {continue;}
                else {break;}
            }

            int[] newPos = {rand_x, rand_y};
            JLabel newLeaf = new JLabel(leafIcon);
            add(newLeaf);
            newLeaf.setBounds(rand_x, rand_y, spritePxls, spritePxls);
            newLeaf.setOpaque(false);
            leafLabels.add(newLeaf);
            leafPositions.add(newPos);
            
        }
    }

        public void playLeafSound(){
        try {
            leafNoise.setFramePosition(0);
            leafNoise.start();
        }

        catch(Exception e) {
            System.out.println("Error playing leaf sound!");
            e.printStackTrace();
        }
    }

}
