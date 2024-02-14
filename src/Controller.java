import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Controller extends JPanel {

        private boolean horizontal;
        private boolean vertical = true;
        public JPanel controllerPanel;
        public JLabel lastKeyPressed;
        Action rightAction;
        Action leftAction;
        Action upAction;
        Action downAction;

    class LeftAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (vertical) {
            lastKeyPressed.setLocation(lastKeyPressed.getX()-10, lastKeyPressed.getY());
            horizontal = true;
            vertical = false;
            }
        }
    }

    class RightAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (vertical) {
            lastKeyPressed.setLocation(lastKeyPressed.getX()+10, lastKeyPressed.getY());
            horizontal = true;
            vertical = false;
            }
        }
    }

    class UpAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (horizontal) {
            lastKeyPressed.setLocation(lastKeyPressed.getX(), lastKeyPressed.getY()-10);
            vertical = true;
            horizontal = false;
            }
        }
    }

    class DownAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (horizontal) {
            lastKeyPressed.setLocation(lastKeyPressed.getX(), lastKeyPressed.getY()+10);
            vertical = true;
            horizontal = false;
            }
        }
    }

    public Controller() {

        InputMap input = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap action = getActionMap();

        lastKeyPressed = new JLabel();
            lastKeyPressed.setBackground(Color.black);
            lastKeyPressed.setBounds(300,300,20,20);
            lastKeyPressed.setLocation(300,300);
            lastKeyPressed.setOpaque(true);

        rightAction = new RightAction();
        leftAction = new LeftAction();
        upAction = new UpAction();
        downAction = new DownAction();

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

        add(lastKeyPressed);
        setLayout(null);
        setVisible(true);
        setBorder(BorderFactory.createLineBorder(Color.black));

    }

}