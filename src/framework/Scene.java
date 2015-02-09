package framework;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;


/**
 * A class that highlights the most useful methods for you to write to create an OpenGL scene. You
 * should subclass this to do your actual work.
 *
 * @author Robert C. Duvall
 */
public abstract class Scene {
    /**
     * Get the title of the scene.
     *
     * @return title of scene
     */
    public abstract String getTitle ();

    /**
     * Initialize global OpenGL state.
     *
     * For example, setting lighting or texture parameters
     *
     * @param gl basic interface to OpenGL
     * @param glu basic interface to GLU
     * @param glut basic interface to GLUT
     */
    public void init (GL2 gl, GLU glu, GLUT glut) {
        // by default, do nothing
    }

    /**
     * Display complete scene.
     *
     * This is called whenever the contents of the window need to be redrawn.
     *
     * @param gl basic interface to OpenGL
     * @param glu basic interface to GLU
     * @param glut basic interface to GLUT
     */
    public abstract void display (GL2 gl, GLU glu, GLUT glut);

    /**
     * Establish camera's view of the scene.
     *
     * This is called to get the current camera's position.
     *
     * @param gl basic interface to OpenGL
     * @param glu basic interface to GLU
     * @param glut basic interface to GLUT
     */
    public abstract void setCamera (GL2 gl, GLU glu, GLUT glut);

    /**
     * Animate scene by making small changes to its state.
     *
     * For example, changing the absolute position or rotation angle of an object.
     *
     * @param gl basic interface to OpenGL
     * @param glu basic interface to GLU
     * @param glut basic interface to GLUT
     */
    public void animate (GL2 gl, GLU glu, GLUT glut) {
        // by default, do nothing
    }

    /**
     * Respond to the press of a key.
     *
     * @param keyCode Java code representing pressed key
     */
    public void keyPressed (int keyCode) {
        // by default, do nothing
    }

    /**
     * Respond to the release of a key.
     *
     * @param keyCode Java code representing released key
     */
    public void keyReleased (int keyCode) {
        // by default, do nothing
    }

    /**
     * Respond to the press and release of an alphanumeric key.
     *
     * @param key text representing typed key
     */
    public void keyTyped (int keyCode) {
        // by default, do nothing
    }
}
