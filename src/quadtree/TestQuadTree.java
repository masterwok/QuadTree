package quadtree;

import com.jogamp.opengl.util.Animator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import quadtree.objects.Particle;

/**
 *
 * @author Jonathan Trowbridge
 */
public class TestQuadTree implements GLEventListener {

    private GLCanvas canvas;
    private GLU glu;
    QuadTree quadTree;
    ArrayList<Particle> particles = new ArrayList<>();
    private final int MAX_LEVEL = 6;
    private final int MAX_CAPACITY = 4;

    @SuppressWarnings("LeakingThisInConstructor")
    public TestQuadTree() {
        System.setProperty("sun.awt.noerasebackground", "true");
        JFrame frame = new JFrame("Test Quad Tree");
        Animator animator;

        // Create the initial quad tree node
        quadTree = new QuadTree(600, 600, MAX_LEVEL, MAX_CAPACITY);

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

        quadTree = new QuadTree(600, 600, MAX_LEVEL, MAX_CAPACITY);

        // Generate random points
        particles.clear();
        int min = 0, max = 600;
        int minVelocity = -3, maxVelocity = 3;
        int x, y;
        int velocityX, velocityY;
        Particle p;
        for (int i = 0; i < 100; i++) {
            x = min + (int) (Math.random() * ((max - min) + 1));
            y = min + (int) (Math.random() * ((max - min) + 1));
            velocityX = minVelocity + (int) (Math.random() * ((maxVelocity - minVelocity) + 1));
            velocityY = minVelocity + (int) (Math.random() * ((maxVelocity - minVelocity) + 1));
            p = new Particle(x, y, velocityX, velocityY, 600, 600);
            quadTree.insert(p);
            particles.add(p);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        glu.gluLookAt(0, 0, 1, 0, 0, 0, 0f, 1f, 0f);

        // Draw everything here
        quadTree = new QuadTree(600, 600, MAX_LEVEL, MAX_CAPACITY);

        for (Particle p : particles) {
            quadTree.insert(p);
            p.move();
            p.draw(gl);
        }

        quadTree.draw(gl);

        // Check for errors
        int error = gl.glGetError();
        if (error != GL.GL_NO_ERROR)
            System.out.println("OpenGL Error: " + error);
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
        glu = new GLU();

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

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    public static void main(String[] args) {
        new TestQuadTree();
    }
}
