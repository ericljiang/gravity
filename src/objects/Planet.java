package objects;

import Main.Gravity;
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

	public Vector calcGrav(Planet a, Planet b) {
		double g = 0.0001 * b.myMass / a.myPos.sub(b.myPos).lengthSquared();
		Vector delta = b.myPos.sub(a.myPos).scale(g);
		return delta;
	}

	public Planet[] update() {
		Planet[] remove = null;
		for (Planet other:Gravity.myPlanets) {
			if (other != this) {
				this.myVel = this.myVel.add(calcGrav(this, other));
				this.myPos = this.myPos.add(this.myVel);

				// check for collision
				double d = this.myPos.sub(other.myPos).length();
				if (d < this.getRadius() || d < other.getRadius()) {
					remove = new Planet[] {this, other};
					//removeList.add(new Planet[] {planet, other});
				}
			}

		}
		return remove;
	}

	public double getRadius() {
		return Math.pow(myMass, 1.0 / 3.0);
	}
}
