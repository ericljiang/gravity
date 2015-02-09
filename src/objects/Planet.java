package objects;

import ray.math.Point;
import ray.math.Vector;

public class Planet {
	public double myMass;
	public Point myPos;
	public Vector myVel;

	public Planet (double mass, double x, double y, double z) {
		myMass = mass;
		myPos = new Point(x, y, z);
		myVel = new Vector(); 
	}

	public double getRadius() {
		return Math.pow(myMass, 1.0 / 3.0);
	}
}
