import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Controller class for handling key events
public class Controller extends JPanel {

    // Variables to control movement direction
    private boolean horizontal;
    private boolean vertical = true;
    public JPanel controllerPanel;
    public JLabel lastKeyPressed;
    Action rightAction;
    Action leftAction;
    Action upAction;
    Action downAction;

    // Action class for moving left
    class LeftAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (vertical) {
                lastKeyPressed.setLocation(lastKeyPressed.getX() - 10, lastKeyPressed.getY());
                horizontal = true;
                vertical = false;
            }
        }
    }

    // Action class for moving right
    class RightAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (vertical) {
                lastKeyPressed.setLocation(lastKeyPressed.getX() + 10, lastKeyPressed.getY());
                horizontal = true;
                vertical = false;
            }
        }
    }

    // Action class for moving up
    class UpAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (horizontal) {
                lastKeyPressed.setLocation(lastKeyPressed.getX(), lastKeyPressed.getY() - 10);
                vertical = true;
                horizontal = false;
            }
        }
    }

    // Action class for moving down
    class DownAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (horizontal) {
                lastKeyPressed.setLocation(lastKeyPressed.getX(), lastKeyPressed.getY() + 10);
                vertical = true;
                horizontal = false;
            }
        }
    }

    // Constructor for the controller panel
    public Controller() {

        InputMap input = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap action = getActionMap();

        // Initialize the lastKeyPressed label
        lastKeyPressed = new JLabel();
        lastKeyPressed.setBackground(Color.black);
        lastKeyPressed.setBounds(300, 300, 20, 20);
        lastKeyPressed.setLocation(300, 300);
        lastKeyPressed.setOpaque(true);

        // Initialize actions for different key strokes
        rightAction = new RightAction();
        leftAction = new LeftAction();
        upAction = new UpAction();
        downAction = new DownAction();

        // Define key bindings for actions
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

        // Add components to the panel
        add(lastKeyPressed);
        setLayout(null);
        setVisible(true);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }
}