import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller extends JPanel {
        private final int FPS = 2;
        private static Timer timer;

        private boolean horizontal;
        private boolean vertical = true;
        private int bearing = 1;


        public JLabel[] caterpillarLabels;
        private ImageIcon headIcon;
        private ImageIcon bodyIcon;
        private int spritePxls = 32;

        private BufferedImage backgroundImage;
        private int gameAreaX = 600;
        private int gameAreaY = 510;

        public JPanel controllerPanel;
        Action rightAction;
        Action leftAction;
        Action upAction;
        Action downAction;
        Action pauseAction;
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
                timer.start();
            }
        }
    }

    class QuitAction extends AbstractAction{
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }

    class LeftAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (bearing != 1){
                bearing = 3;
            }
        }
    }

    class RightAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (bearing != 3){
                bearing = 1;
            }
        }
    }

    class UpAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (bearing != 2){
                bearing = 0;
            }
        }
    }

    class DownAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (bearing != 0){
                bearing = 2;
            }
        }
    }

    public Controller() {
        int startX = 300;
        int startY = 300;
        int segDistance = 30;
        int segCount = 4;

        InputMap input = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap action = getActionMap();

        kate = new Caterpillar(startX, startY, segDistance, segCount, bearing); //x, y, segDistance, segCount, bearing

        //load the images
        String numString = Integer.toString(bearing);
        headIcon = new ImageIcon(numString + ".png");
        bodyIcon = new ImageIcon("segment.png");
        String imagePath = "background.png";
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        caterpillarLabels = new JLabel[segCount];
        int[][] positions = kate.getPos(); //stores each body segment's location
        caterpillarLabels[0] = new JLabel(headIcon);
            caterpillarLabels[0].setBounds(positions[0][0], positions[0][1], spritePxls, spritePxls);
            caterpillarLabels[0].setOpaque(false);

        for (int x = 1; x < segCount; x++){
            caterpillarLabels[x] = new JLabel(bodyIcon);
            caterpillarLabels[x].setBounds(positions[x][0], positions[x][1], spritePxls, spritePxls);
            caterpillarLabels[x].setOpaque(false);
        }

        pauseAction = new PauseAction();
        quitAction = new QuitAction();
        rightAction = new RightAction();
        leftAction = new LeftAction();
        upAction = new UpAction();
        downAction = new DownAction();

            input.put(KeyStroke.getKeyStroke("Q"), "quitAction");
            action.put("quitAction", quitAction);
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

        for (JLabel label : caterpillarLabels){
            add(label);
        }

        setLayout(null);
        setVisible(true);
        setBorder(BorderFactory.createLineBorder(Color.black));

        timer = new Timer(1000/FPS, new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    animate();}});
        timer.start();

    }

    public void animate(){

        int[][] positions = kate.getPos(); //each body segment's location
        kate.setBearing(bearing);

        int headX = positions[0][0];
        int headY = positions[0][1];
        switch(bearing){

            case 0: //heading NORTH
                if (headY > 0){                                //check for collision before moving
                    kate.slither();}
                else {
                    bearing = (headX > gameAreaX/2 ? 3 : 1);       //if collision is imminent, pick a new bearing
                    kate.setBearing(bearing);
                    kate.slither();}
                break;

            case 1:
                if (headX < (gameAreaX - spritePxls)){
                    kate.slither();}
                else {
                    bearing = (headY > gameAreaY/2 ? 0 : 2);
                    kate.setBearing(bearing);
                    kate.slither();}
                break;

            case 2:
                if (headY + 30 < (gameAreaY - spritePxls)){
                    kate.slither();}
                else {
                    bearing = (headX > gameAreaX/2 ? 3 : 1);
                    kate.setBearing(bearing);
                    kate.slither();}
                break;

            case 3:
                if (headX > 0){
                    kate.slither();}
                else {
                    bearing = (headY > gameAreaY/2 ? 0 : 2);
                    kate.setBearing(bearing);
                    kate.slither();}
                break;
        }

        //switch to the appropriate image for the direction
        String numString = Integer.toString(bearing);
        headIcon = new ImageIcon(numString + ".png");
        caterpillarLabels[0].setIcon(headIcon);

        positions = kate.getPos();
        for (int x = 0; x < kate.getLength(); x++){
            caterpillarLabels[x].setLocation(positions[x][0], positions[x][1]);
        }
    }
}
