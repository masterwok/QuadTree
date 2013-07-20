/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.awt.Point;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 *
 * @author Jonathan Trowbridge
 */
public class BoundingBox {

    private float x;
    private float y;
    private float width;
    private float height;

    public BoundingBox(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3f(0f, 1f, 1f);                   // Cyan
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3f(x, y - height, 0f);           // Bottom left
        gl.glVertex3f(x, y, 0f);                    // Top left
        gl.glVertex3f(x + width, y, 0f);            // Top right
        gl.glVertex3f(x + width, y - height, 0f);   // Bottom right
        gl.glEnd();
        gl.glPopMatrix();
    }

    public boolean contains(Point p) {
        return (p.x >= getX() && p.x <= getX() + getWidth()) && (p.y >= getY() - getHeight() && p.y <= getY());
    }

    @Override
    public String toString() {
        return String.format("Top Left: (%f, %f) Width: %f Height: %f", x, y, width, height);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
