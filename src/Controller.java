import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller extends JPanel {
        private final int FPS = 5;
        private static Timer timer;
        private boolean horizontal;
        private boolean vertical = true;
        private int bearing = 1;
        public JPanel controllerPanel;
        public JLabel caterpillarLabel;
        private int spritePxls = 32;
        private ImageIcon icon;
        private BufferedImage backgroundImage;
        private int gameAreaX = 600;
        private int gameAreaY = 500;
        Action rightAction;
        Action leftAction;
        Action upAction;
        Action downAction;
        Action pauseAction;
        Action quitAction;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
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
            bearing = 3;
        }
    }

    class RightAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            bearing = 1;
        }
    }

    class UpAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            bearing = 0;
        }
    }

    class DownAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            bearing = 2;
        }
    }

    public Controller() {

        InputMap input = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap action = getActionMap();

        icon = new ImageIcon("1.png");
        caterpillarLabel = new JLabel(icon);
            //caterpillarLabel.setBackground(Color.red);
            caterpillarLabel.setBounds(300,300, spritePxls, spritePxls);
            caterpillarLabel.setLocation(300,300);
            caterpillarLabel.setOpaque(false);

        // Provide the path to your PNG image
        String imagePath = "background.png";
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
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

        add(caterpillarLabel);
        setLayout(null);
        setVisible(true);
        setBorder(BorderFactory.createLineBorder(Color.black));

        timer = new Timer(1000/FPS, new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    animate();}});
        timer.start();

    }

    public void animate(){

        //store the location as x and y
        int x = caterpillarLabel.getX();
        int y = caterpillarLabel.getY();

        String numString = Integer.toString(bearing);
        icon = new ImageIcon(numString + ".png");
        caterpillarLabel.setIcon(icon);



        switch(bearing){

            case 0: //heading NORTH
                if (y - 10 > 0){                                //check for collision before moving
                    caterpillarLabel.setLocation(x, y - 10);}   //move along the current bearing
                else {
                    bearing = (x > gameAreaX/2 ? 3 : 1);}               //if collision is imminent, pick a new bearing
                break;

            case 1:
                if (x + 10 < (gameAreaX - spritePxls)){
                    caterpillarLabel.setLocation(x + 10, y);}
                else {
                    bearing = (y > gameAreaY/2 ? 0 : 2);}
                break;

            case 2:
                if (y + 10 < (gameAreaY - (spritePxls * 2))){
                    caterpillarLabel.setLocation(x, y + 10);}
                else {
                    bearing = (x > gameAreaX/2 ? 3 : 1);}
                break;

            case 3:
                if (x - 10 > 0){
                    caterpillarLabel.setLocation(x - 10, y);}
                else {
                    bearing = (y > gameAreaY/2 ? 0 : 2);}
                break;
        }

    }
}
