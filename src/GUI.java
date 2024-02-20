import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class GUI {

//Declaring the base variables (more to be added of course)

    private JFrame gui;
    private Clip clip;
    Action rightAction;
    Action leftAction;
    Action upAction;
    Action downAction;
    private JLabel title;
    private JButton startButton;
    private JPanel panel;
    private BufferedImage backgroundImage;

    //Default constructor
    public GUI() {

    //Initializing components (JButton, JLabel and JPanel)

        playMusic("Intro.wav");

        startButton = new JButton();
            startButton.setText("Start");
            startButton.setToolTipText("Hello there!");
        //Sets the action listener of button to ButtonHandler (a class found later on)
            startButton.addActionListener(new ButtonHandler());
            startButton.setActionCommand("start");
            startButton.setBounds(250,220,100,50);
        title = new JLabel();
            title.setText("Javapillar");
            title.setFont(new Font("Arial", Font.PLAIN, 58));
            title.setBounds(175,100,250,100);

        panel = new splashPanel();
        panel.setLayout(null);

        gui = new JFrame();

        //Sets the details of the JFrame
        gui.setResizable(false);
        gui.setSize(600,500);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        gui.setTitle("Javapillar");

        //Adding previous components to panel
        panel.add(startButton);
        panel.add(title);
        panel.setVisible(true);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        //Adding panel to JFrame (since the class is extended from JFrame)
        gui.add(panel);
    }

    //The button actions are below
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            //The action when you press the start button
            if (e.getActionCommand().equals("start")) {
                panel.setVisible(false); //otherwise it sometimes leaves bits on the screen
                clip.stop();             //stop the intro music or it just keeps playing
                playMusic("Game.wav");
                gui.add(new Controller());
            }
        }
    }

    public static void main(String[] args) {
        new GUI();
    }

    //To put a background we have to override the
    //JPanel class paintComponent method
    public class splashPanel extends JPanel{
        private BufferedImage backgroundImage;
        public splashPanel(){
            try {
                backgroundImage = ImageIO.read(new File("Caterpillar_splash.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    //A method to play WAV files for music
    //stop the music with clip.stop()
    public void playMusic(String filePath){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }

        catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}
