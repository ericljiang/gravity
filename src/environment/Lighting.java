package environment;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

public class Lighting {
	private float[] myLightPos = { 0, 0, 1, 1 };
	private boolean isLightOn;
	private boolean isSmooth=true;
	
	public Lighting(){
		isLightOn=true;
	}

	// animation state

	public void setLighting (GL2 gl, GLU glu, GLUT glut) {
		// interpolate color on objects across polygons or not
        if (isSmooth) {
            gl.glShadeModel(GL2.GL_SMOOTH);
        }
        else {
            gl.glShadeModel(GL2.GL_FLAT);            
        }
        // turn one light on or off
        if (isLightOn) {
            gl.glEnable(GL2.GL_LIGHTING);
            gl.glEnable(GL2.GL_LIGHT0);
            gl.glEnable(GL2.GL_LIGHT1);
        }
        else {
            gl.glDisable(GL2.GL_LIGHTING);
            gl.glDisable(GL2.GL_LIGHT1);
        }
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, myLightPos, 0);
        float[] ambientVals={3.0f, 3.0f, 3.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambientVals, 0);
	}
	
	public void setLightPosition(float[] pos){
    	myLightPos=pos;
    }
}
