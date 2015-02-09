package Main;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import objects.Planet;
import ray.math.Point;
import ray.math.Vector;

import com.jogamp.opengl.util.gl2.GLUT;

import framework.JOGLFrame;
import framework.Scene;


/**
 * Display a simple scene to demonstrate OpenGL.
 *
 * @author Robert C. Duvall
 */
/**
 * @author Eric
 *
 */
public class Gravity extends Scene {
	// animation state
	private double myAngleX = 90.0;
	private double myAngleZ = 0.0;
	private double myX;
	private double myY;
	private double myZ;
	private double distance = 1.0;
	private double size = 50;

	public static ArrayList<Planet> myPlanets;

	/**
	 * Create the scene with the given arguments.
	 *
	 * For example, the number of shapes to display.
	 *
	 * @param args command-line arguments
	 */
	public Gravity (String[] args) {
		myPlanets = new ArrayList<Planet>();
		myPlanets.add(new Planet(10, 0, 0, 0));
		for (int i = 0; i < 500; i++) {
			Planet planet = new Planet(
					0.005 * Math.random(),
					size * Math.random() - size * 0.5,
					size * Math.random() - size * 0.5,
					size * Math.random() - size * 0.5
					);
			planet.myVel = planet.myPos.sub(new Point()).cross(new Vector(0, 0, 1)).scale(.0001);
			myPlanets.add(planet);
		}
		
	}

	/**
	 * @return title for this scene
	 */
	@Override
	public String getTitle () {
		return "blargh";
	}

	public void collide(Planet a, Planet b) {
		if (a.myMass > b.myMass) {
			Vector momentumA = a.myVel.scale(a.myMass);
			Vector momentumB = b.myVel.scale(b.myMass);
			a.myVel = momentumA.add(momentumB).scale(1 / (a.myMass + b.myMass));
			a.myMass += b.myMass;
			myPlanets.remove(b);
		} else {
			Vector momentumA = a.myVel.scale(a.myMass);
			Vector momentumB = b.myVel.scale(b.myMass);
			b.myVel = momentumA.add(momentumB).scale(1 / (a.myMass + b.myMass));
			b.myMass += a.myMass;
			myPlanets.remove(a);
		}
		//System.out.println("Collision");
		
//		Point mid = a.myPos.scaleAdd(0.5, b.myPos.sub(a.myPos));
//		Planet newPlanet = new Planet(a.myMass + b.myMass, mid.x, mid.y, mid.z);
//		newPlanet.myVel = (a.myVel.scale(a.myMass).add(b.myVel.scale(b.myMass))).scale(1 / newPlanet.myMass);
//
//		myPlanets.add(newPlanet);
//		myPlanets.remove(a);
//		myPlanets.remove(b);
	}

	public void update() {
		Planet[] remove = null;
		//ArrayList<Planet[]> removeList = new ArrayList<Planet[]>();
		for (Planet planet:myPlanets) {
			remove = planet.update();
		}
		if (remove != null) {
			collide(remove[0], remove[1]);
		}

		/*if (!removeList.isEmpty()) {
			for (Planet[] pair:removeList) {
				collide(pair[0], pair[1]);
			}
		}*/
		//System.out.println(myPlanets.size());
	}

	/**
	 * Gessler's color ramp function
	 * @param part
	 * @param whole
	 * @return
	 */
	public double[] colorRamp(double part, double whole)
	{
		if (whole == 0) whole++;               // prevent divide by zero
		double pixelDistanceAlongEdges = (part * 1792) / whole;
		double red, green, blue;
		// Which edge of the color cube are we on?
		if (pixelDistanceAlongEdges < 256) {        // from BLACK to BLUE
			red = 0; green = 0; blue = pixelDistanceAlongEdges;
		}
		else if (pixelDistanceAlongEdges < 512) {   // from BLUE to CYAN
			red = 0; green = pixelDistanceAlongEdges - 256; blue = 255;
		}
		else if (pixelDistanceAlongEdges < 768) {   // from CYAN to GREEN
			red = 0; green = 255; blue = 255 - (pixelDistanceAlongEdges - 512);
		}
		else if (pixelDistanceAlongEdges < 1024) {  // from GREEN to YELLOW
			red = (pixelDistanceAlongEdges - 768); green = 255; blue = 0;
		}
		else if (pixelDistanceAlongEdges < 1280) {  // from YELLOW to RED
			red = 255; green= 255-(pixelDistanceAlongEdges - 1024); blue = 0;
		}
		else if (pixelDistanceAlongEdges < 1536) {  // from RED to MAGENTA
			red = 255; green= 0; blue = pixelDistanceAlongEdges - 1280;
		}
		else {                                     // from MAGENTA to WHITE
			red = 255; green = pixelDistanceAlongEdges - 1537; blue = 255;
		}
		return new double[] {red, green, blue};
	}

	/**
	 * Draw all of the objects to display.
	 */
	@Override
	public void display (GL2 gl, GLU glu, GLUT glut) {
		update();
		
		
		gl.glPushMatrix();
		// camera
		gl.glScaled(distance, distance, distance);
		gl.glRotated(myAngleX, 1, 0, 0);
		gl.glRotated(myAngleZ, 0, 0, 1);
		Planet sun = myPlanets.get(0);

		glut.glutWireCube(50);
		glut.glutWireCylinder(10, 0, 20, 0);
		glut.glutWireCylinder(20, 0, 20, 0);
		glut.glutWireCylinder(30, 0, 20, 0);
		gl.glTranslated(-1 * sun.myPos.x, -1 * sun.myPos.y, -1 * sun.myPos.z);

		for (Planet planet:myPlanets) {
			gl.glPushMatrix(); {
				// define object's color
				double[] rgb = colorRamp(planet.myMass, 0.04);
				gl.glColor3d(rgb[0], rgb[1], rgb[2]);
				gl.glTranslated(planet.myPos.x, planet.myPos.y, planet.myPos.z);
				glut.glutSolidSphere(planet.getRadius(), 8, 8);
				//glut.glutWireCube((float) planet.getRadius());
			} gl.glPopMatrix();
		}
		gl.glPopMatrix();

		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glPushMatrix(); {
			//gl.glRasterPos3i(10, 0, 0);
			gl.glRasterPos2i(20, 0);
			gl.glColor3f(1f, 1f, 1f);
			glut.glutBitmapString(8, "test");
		} gl.glPopMatrix();
	}

	/**
	 * Set the camera's view of the scene.
	 */
	@Override
	public void setCamera (GL2 gl, GLU glu, GLUT glut) {
		glu.gluLookAt(0, 100, 0,  // from position
				0, 0, 0,  // to position
				0, 0, 1); // up direction
	}

	/**
	 * Animate the scene by changing its state slightly.
	 *
	 * For example, the angle of rotation of the shapes.
	 */
	@Override
	public void animate (GL2 gl, GLU glu, GLUT glut) {
		// animate model by spinning it a few degrees each time
		//myAngle += 2;
	}

	public void keyPressed (int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			myAngleZ -= 3;
			break;
		case KeyEvent.VK_RIGHT:
			myAngleZ += 3;
			break;
		case KeyEvent.VK_UP:
			myAngleX -= 3;
			break;
		case KeyEvent.VK_DOWN:
			myAngleX += 3;
			break;
		case KeyEvent.VK_W:
			myX -= 3;
			break;
		case KeyEvent.VK_S:
			myX += 3;
			break;
		case KeyEvent.VK_PLUS:
		case KeyEvent.VK_EQUALS:
			distance -= 0.01;
			break;
		case KeyEvent.VK_MINUS:
		case KeyEvent.VK_UNDERSCORE:
			distance += 0.01;
			break;
		}
	}

	// allow program to be run from here
	public static void main (String[] args) {
		new JOGLFrame(new Gravity(args));
	}
}
