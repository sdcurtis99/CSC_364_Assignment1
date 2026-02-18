package javiergs.mqtt;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_UP:
                Blackboard.getInstance().up();
                break;

            case KeyEvent.VK_DOWN:
                Blackboard.getInstance().down();
                break;

            case KeyEvent.VK_LEFT:
                Blackboard.getInstance().left();
                break;

            case KeyEvent.VK_RIGHT:
                Blackboard.getInstance().right();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed
    }
}