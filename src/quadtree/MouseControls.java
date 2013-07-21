package quadtree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author jtsan
 */
public class MouseControls implements MouseListener {

    private TestQuadTree controller;
    private int height;

    public MouseControls(TestQuadTree controller, int height) {
        this.controller = controller;
        this.height = height;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.insertPointAtPosition(e.getX(), height - e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
