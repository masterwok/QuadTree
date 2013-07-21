package quadtree.objects;

import javax.media.opengl.GL2;

/**
 *
 * @author jtsan
 */
public class Particle {

    private float radius = 6;
    private final int SEGMENTS = 10;
    private float x;
    private float y;
    private float velocityX;
    private float velocityY;
    private float boundWidth;
    private float boundHeight;

    public Particle(float x, float y, float velocityX, float velocityY, float boundWidth, float boundHeight) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.boundWidth = boundWidth;
        this.boundHeight = boundHeight;
    }

    public void move() {

        x += velocityX;
        y += velocityY;

        // Check if moving out of boundWidth
        if (x - radius < 0) {
            velocityX = -velocityX; // Reflect along normal
            x = radius;             // Re-position at the edge
        } else if (x + radius > boundWidth) {
            velocityX = -velocityX;
            x = boundWidth - radius;
        }

        // Check if moving out of boundHeight
        if (y - radius < 0) {
            velocityY = -velocityY;                 // Reflect along normal
            y = radius;                             // Re-position at the edge
        } else if (y + radius > boundWidth) {
            velocityY = -velocityY;
            y = boundWidth - radius;
        }

    }

    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3f(1, 0, 1);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(x, y);
        for (int n = 0; n <= SEGMENTS; ++n) {
            float t = 2 * (float) Math.PI * (float) n / (float) SEGMENTS;
            gl.glVertex2f(x + (float) Math.sin(t) * radius, y + (float) Math.cos(t) * radius);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }

    /**
     * @return the radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return the velocityX
     */
    public float getVelocityX() {
        return velocityX;
    }

    /**
     * @param velocityX the velocityX to set
     */
    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    /**
     * @return the velocityY
     */
    public float getVelocityY() {
        return velocityY;
    }

    /**
     * @param velocityY the velocityY to set
     */
    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * @return the boundWidth
     */
    public float getBoundWidth() {
        return boundWidth;
    }

    /**
     * @param boundWidth the boundWidth to set
     */
    public void setBoundWidth(float boundWidth) {
        this.boundWidth = boundWidth;
    }

    /**
     * @return the boundHeight
     */
    public float getBoundHeight() {
        return boundHeight;
    }

    /**
     * @param boundHeight the boundHeight to set
     */
    public void setBoundHeight(float boundHeight) {
        this.boundHeight = boundHeight;
    }
}
