import java.util.*;

public class SpinningCannonCircle extends GameObject
{
	private List<Cannon> cannons;

	double cx, cy, rad, ang, spinSpeed;
	int numCannons;

	public SpinningCannonCircle(
		Main m, double x, double y, double radius,
		int numberCannons, double spinningSpeed, double cannonSpin, double strength) {
		//SPINNINGSPEED IN DEGREES/FRAME!
		super(m);
		cx = x; cy = y; rad = radius; numCannons = numberCannons; spinSpeed = spinningSpeed*Math.PI/180;
		ang = 0;

		cannons = new ArrayList<Cannon>();
		for(int i = 0; i < numCannons; i++) {
			Cannon c = new SpinCannon(main,0,0,cannonSpin,strength);
			main.getObject(c);
			cannons.add(c);
		}
	}

	public void compute() {
		ang += spinSpeed;
		for(int i = 0; i < numCannons; i++) {
			double thisAng = ang + Math.PI*2 * i/numCannons;
			cannons.get(i).x = cx + rad*Math.cos(thisAng); cannons.get(i).y = cy + rad*Math.sin(thisAng);
		}
	}

	public void draw(java.awt.Graphics2D g) { /*pass*/ }
}