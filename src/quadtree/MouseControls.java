package quadtree;

import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author jtsan
 */
public class MouseControls implements MouseListener, MouseMotionListener {

    private TestQuadTree controller;
    private int height;

    public MouseControls(TestQuadTree controller, int height) {
        this.controller = controller;
        this.height = height;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.generateNewPoints(e.getX(), height - e.getY(), 5);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.generateNewPoints(e.getX(), height - e.getY(), 10);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
