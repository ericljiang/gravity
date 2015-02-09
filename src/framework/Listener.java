package framework;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.gl2.GLUT;


/**
 * This class serves as mediator between your scene code and the boilerplate OpenGL code that needs
 * to be written. It calls your scene methods at the appropriate time.
 *
 * It may be updated from time to time to add functionality.
 *
 * @author Robert C. Duvall
 */
public class Listener implements GLEventListener, KeyListener {
    // constants
    public static long ONE_SECOND = 1000;

    // user's scene to animate and display
    private Scene myScene;
    // animation state
    private AnimatorBase myAnimator;
    private int myFrameCount;
    private long myLastFrameTime;
    private double myFPS;
    private boolean isRunning;
    private boolean showFPS;
    // cache creation of these objects
    private static GLU glu = new GLU();
    private static GLUT glut = new GLUT();


    /**
     * Create this listener with the arguments given on the command-line and the animation thread.
     *
     * @param args command-line arguments
     * @param animator animation thread
     */
    public Listener (Scene scene, AnimatorBase animator) {
        myScene = scene;
        myAnimator = animator;
        isRunning = true;
        myFrameCount = 0;
        myLastFrameTime = System.currentTimeMillis();
        myFPS = 0;
        showFPS = false;
    }

    /**
     * Get the title of the scene.
     *
     * @return title of scene
     */
    public String getTitle () {
        return myScene.getTitle();
    }

    // //////////////////////////////////////////////////////////
    // GLEventListener methods
    /**
     * Called once immediately after the OpenGL context is initialized.
     *
     * @see GLEventListener#init(GLAutoDrawable)
     */
    @Override
    public void init (GLAutoDrawable drawable) {
        // get graphics context
        GL2 gl = drawable.getGL().getGL2();
        // interesting?
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
        System.err.println("GL_CLASS: " + gl.getClass().getName());
        // set to draw in window based on depth
        gl.glEnable(GL2.GL_DEPTH_TEST);
        // start scene
        myScene.init(gl, glu, glut);
    }

    /**
     * Called repeatedly to render the OpenGL scene.
     *
     * @see GLEventListener#display(GLAutoDrawable)
     */
    @Override
    public void display (GLAutoDrawable drawable) {
        // get graphics context
        GL2 gl = drawable.getGL().getGL2();
        // update scene for this time step
        myScene.animate(gl, glu, glut);
        // clear the drawing surface
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        // display model
        gl.glPushMatrix();
        myScene.setCamera(gl, glu, glut);
        myScene.display(gl, glu, glut);
        gl.glPopMatrix();
        // display frame rate
        computeFPS();
    }

    /**
     * Called immediately after the component has been resized
     *
     * @see GLEventListener#reshape(GLAutoDrawable, int, int, int, int)
     */
    @Override
    public void reshape (GLAutoDrawable drawable, int x, int y, int width, int height) {
        // reset camera based on new viewport
        setPerspective(drawable.getGL().getGL2(), glu, GL2.GL_RENDER, null);
    }

    /**
     * Called when the display mode or the display device has changed.
     *
     * @see GLEventListener#displayChanged(GLAutoDrawable, boolean, boolean)
     */
    public void displayChanged (GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        // not generally used
    }

    /**
     * Called when the display is closed.
     *
     * @see GLEventListener#dispose(GLAutoDrawable)
     */
    @Override
    public void dispose (GLAutoDrawable drawable) {
        // not generally used
    }

    // //////////////////////////////////////////////////////////
    // KeyListener methods
    /**
     * Called when any key is pressed within the canvas.
     */
    @Override
    public void keyPressed (KeyEvent e) {
        // pass event onto user's code
        myScene.keyPressed(e.getKeyCode());
    }

    /**
     * Called when any key is released within the canvas.
     */
    @Override
    public void keyReleased (KeyEvent e) {
        switch (e.getKeyCode()) {
        // toggle animation running
        case KeyEvent.VK_Z:
            showFPS = !showFPS;
            break;
            // toggle animation running
        case KeyEvent.VK_P:
            isRunning = !isRunning;
            if (isRunning) {
                myAnimator.start();
            } else {
                myAnimator.stop();
            }
            break;
            // quit the program
        case KeyEvent.VK_ESCAPE:
        case KeyEvent.VK_Q:
            myAnimator.stop();
            System.exit(0);
            break;
            // pass event onto user's code
        default:
            myScene.keyReleased(e.getKeyCode());
        }
    }

    /**
     * Called when standard alphanumeric keys are pressed and released within the canvas.
     */
    @Override
    public void keyTyped (KeyEvent e) {
        // by default, do nothing
    }

    // //////////////////////////////////////////////////////////
    // helper methods
    /**
     * Reset perspective matrix based on size of viewport.
     */
    private void setPerspective (GL2 gl, GLU glu, int mode, Point pt) {
        // get info about viewport (x, y, w, h)
        int[] viewport = new int[4];
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
        // set camera to view viewport area
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        // check for selection
        if (mode == GL2.GL_SELECT) {
            // create 5x5 pixel picking region near cursor location
            glu.gluPickMatrix(pt.x, viewport[3] - pt.y, 5.0, 5.0, viewport, 0);
        }
        // view scene in perspective
        glu.gluPerspective(45.0, (float)viewport[2]/viewport[3], 0.1, 500.0);
        // prepare to work with model again
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /*
     * Compute and print frames per second of animation
     */
    private double computeFPS () {
        myFrameCount++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - myLastFrameTime > ONE_SECOND) {
            myFPS = myFrameCount * ONE_SECOND / (double) (currentTime - myLastFrameTime);
            myLastFrameTime = currentTime;
            if (showFPS) {
                System.out.printf("%3.2f\n", myFPS);
            }
            myFrameCount = 0;
        }
        return myFPS;
    }
}
