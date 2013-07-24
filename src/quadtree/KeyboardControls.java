package quadtree;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Jonathan Trowbridge
 */
public class KeyboardControls implements KeyListener {

    private TestQuadTree controller;

    public KeyboardControls(TestQuadTree controller) {
        this.controller = controller;
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_SPACE):
                //controller.generateNewPoints(5);
                break;
            case (KeyEvent.VK_C):
                controller.reset();
                break;
        }
    }
}
