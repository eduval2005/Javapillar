import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI {

//Declaring the base variables (more to be added of course)

    private JFrame gui;

    Action rightAction;
    Action leftAction;
    Action upAction;
    Action downAction;
    private JLabel title;
    private JButton startButton;
    private JPanel panel;

//Default constructor

public GUI() {

//Initializing components (JButton, JLabel and JPanel)

    startButton = new JButton();
        startButton.setText("Start");
        startButton.setToolTipText("Hello there!");
       //Sets the action listener of button to ButtonHandler (a class found later on)
        startButton.addActionListener(new ButtonHandler());
        startButton.setActionCommand("start");
        startButton.setBounds(250,350,100,50);
    title = new JLabel();
        title.setText("Javapillar");
        title.setFont(new Font("Arial", Font.PLAIN, 58));
        title.setBounds(175,100,250,100);
    panel = new JPanel();
        panel.setLayout(null);
    gui = new JFrame();

//Sets the details of the JFrame

    gui.setSize(600,500);
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gui.setVisible(true);
    gui.setTitle("Javapillar");

//Adding previous components to panel

    panel.add(startButton);
    panel.add(title);
    panel.setBorder(BorderFactory.createLineBorder(Color.black));

//Adding panel to JFrame (since the class is extended from JFrame)

    gui.add(panel);
}

//The button actions are below

private class ButtonHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {

        //The action when you press the start button
        if (e.getActionCommand().equals("start")) {
            title.setVisible(false);
            startButton.setVisible(false);
            gui.add(new Controller());
        }
    }
}

public static void main(String[] args) {

    new GUI();
    
}
}