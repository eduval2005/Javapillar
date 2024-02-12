import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JFrame {

//Declaring the base variables (more to be added of course)

    private JLabel title;
    private JLabel rng;
    private JButton button;
    private JPanel panel;

//Default constructor

public GUI() {

//Initializing components (JButton, JLabel and JPanel)

    button = new JButton();
        button.setText("Start");
        button.setToolTipText("Hello there!");
       //Sets the action listener of button to ButtonHandler (a class found later on)
        button.addActionListener(new ButtonHandler());
        button.setBounds(250,350,100,50);
    title = new JLabel();
        title.setText("Javapillar");
        title.setFont(new Font("Arial", Font.PLAIN, 58));
        title.setBounds(175,100,250,100);
    rng = new JLabel();
        rng.setBounds(288,300,75,25);
        rng.setFont(new Font("Arial", Font.PLAIN, 24));
    panel = new JPanel();
        panel.setLayout(null);

//Sets the details of the JFrame

    setSize(600,500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
    setTitle("Javapillar");

//Adding previous components to panel

    panel.add(button);
    panel.add(title);
    panel.add(rng);

//Adding panel to JFrame (since the class is extended from JFrame)

    add(panel);
}

//RNG just to test button functionality

private class ButtonHandler implements ActionListener{
    public void actionPerformed(ActionEvent e) {
        int random = (int) (1 + Math.random()*100);
        rng.setText(Integer.toString(random));
}
}

public static void main(String[] args) {

    new GUI();
    
}
}