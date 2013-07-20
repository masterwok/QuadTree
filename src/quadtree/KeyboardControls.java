/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Jonathan Trowbridge
 */
public class KeyboardControls implements KeyListener {

    private TestQuadTree tester;

    public KeyboardControls(TestQuadTree tester) {
        this.tester = tester;
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            //System.out.println("Generating new points");
            tester.generateNewPoints();
        }
    }
}
