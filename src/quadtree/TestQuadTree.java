package quadtree;

import com.jogamp.opengl.util.Animator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

/**
 *
 * @author Jonathan Trowbridge
 */
public class TestQuadTree implements GLEventListener {

    private GLCanvas canvas;
    private GLU glu = new GLU();
    QuadTree quadTree;
    ArrayList<Point> points = new ArrayList<>();
    private final int MAX_LEVEL = 4;

    @SuppressWarnings("LeakingThisInConstructor")
    public TestQuadTree() {
        System.setProperty("sun.awt.noerasebackground", "true");
        JFrame frame = new JFrame("Test Quad Tree");
        Animator animator;

        // Create the initial quad tree node
        quadTree = new QuadTree(600, 600, MAX_LEVEL);

        // Setup the canvas
        canvas = new GLCanvas();
        canvas.setPreferredSize(new Dimension(600, 600));
        canvas.addGLEventListener(this);

        // Setup the frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.requestFocus();
        canvas.requestFocusInWindow();

        canvas.addKeyListener(new KeyboardControls(this));

        // Start the animator
        animator = new Animator(canvas);
        animator.start();
    }

    public void generateNewPoints() {

        quadTree = new QuadTree(600, 600, MAX_LEVEL);

        // Generate random points
        points.clear();
        int min = 0, max = 600;
        int x, y;
        Point p;
        for (int i = 0; i < 500; i++) {
            x = min + (int) (Math.random() * ((max - min) + 1));
            y = min + (int) (Math.random() * ((max - min) + 1));
            p = new Point(x, y);
            quadTree.insert(p);
            points.add(p);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        glu.gluLookAt(0, 0, 1, 0, 0, 0, 0f, 1f, 0f);


        // Draw the points in this quad
        gl.glPushMatrix();
        gl.glColor3f(1, 0, 1);
        gl.glBegin(GL.GL_POINTS);
        for (Point p : points)
            gl.glVertex3d(p.getX(), p.getY(), 0.5f);
        gl.glEnd();
        gl.glPopMatrix();

        // Draw everything here
        quadTree.draw(gl);

        // Check for errors
        int error = gl.glGetError();
        if (error != GL.GL_NO_ERROR)
            System.out.println("OpenGL Error: " + error);
    }

    private void drawWindowBorder(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3f(1f, 0f, 0f);
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3f(0f, 0f, 0f);
        gl.glVertex3f(0f, 600, 0f);
        gl.glVertex3f(600, 600, 0f);
        gl.glVertex3f(600, 0, 0f);
        gl.glEnd();
        gl.glColor3f(0f, 1f, 0f);
        gl.glPopMatrix();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.setSwapInterval(1);
        gl.glClearColor(0, 0, 0, 0);        // Black background
        gl.glLineWidth(1f);
        gl.glPointSize(3f);
        gl.glShadeModel(GL2.GL_SMOOTH);         // smooth or flat 		
        gl.glClearDepth(1.0f);			// depth handling routines	
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        width = height = Math.min(width, height); 	//this avoids different x,y scaling
        gl.glViewport(0, 0, width, height);
        gl.glOrtho(-1, 601, -1, 601, -1, 601);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(0, 0, 1, 0, 0, 0, 0f, 1f, 0f);
    }

    public static void main(String[] args) {
        new TestQuadTree();
    }
}
