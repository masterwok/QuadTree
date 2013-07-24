package quadtree;

import java.awt.geom.Rectangle2D;
import javax.media.opengl.GL2;

/**
 *
 * @author jtsan
 */
public class BoundingBox extends Rectangle2D.Float {

    public BoundingBox(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public boolean contains(double x, double y, double width, double height) { 
        return containsPoint(x, y - height)
                && containsPoint(x, y)
                && containsPoint(x + width, y)
                && containsPoint(x + width, y - height);
    }

    private boolean containsPoint(double x, double y) {
        return (x >= this.x && x <= this.x + this.width) && (y >= this.y - this.height && y <= this.y);
    }

    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex3f(x, y - height, 0f);
        gl.glVertex3f(x, y, 0f);
        gl.glVertex3f(x + width, y, 0f);
        gl.glVertex3f(x + width, y - height, 0f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    @Override
    public String toString() {
        return String.format("[x = %f, y = %f, width = %f, height = %f", x, y, width, height);
    }
}
