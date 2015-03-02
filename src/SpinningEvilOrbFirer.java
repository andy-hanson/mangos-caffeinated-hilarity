import java.awt.*;

public class SpinningEvilOrbFirer extends EvilOrbFirer {
	private boolean angleRestricted, angleGoingUp;
	private double minAngle, maxAngle;
	private double angleChange;
	private final static double DRAW_RAD = 60;

	public SpinningEvilOrbFirer(
		Main m, double x, double y,
		double angleChanging, double fireSpeed, int frecuency,
		boolean hasMinMaxAngle, double minAng, double maxAng) {
		/* angleSpeed in degrees/second; minAng, maxAng in degrees. If hasMinMaxAngle, make sure angleSpeed is > 0! */
		super(m,x,y,0,fireSpeed,frecuency);
		if (angleRestricted = hasMinMaxAngle) {
			minAngle = minAng * Math.PI/180; maxAngle = maxAng * Math.PI/180;
			angle = minAngle; angleGoingUp = true;
		}
		angleChange = angleChanging * Math.PI/180;
	}
	public SpinningEvilOrbFirer(
		Main m, double x, double y,
		double angleChanging, double fireSpeed, int frecuency) {
		this(m,x,y,angleChanging,fireSpeed,frecuency,false,0,0);
	}

	public void compute() {
		if (angleRestricted) {
			if (angleGoingUp)
				angle += angleChange;
			else
				angle -= angleChange;

			if (angle < minAngle)
				angleGoingUp = true;
			else if(angle > maxAngle)
				angleGoingUp = false;
		}
		else {
			angle += angleChange;
		}
		super.compute();
	}

	public void draw(Graphics2D g) {
		//Draw a ray from me, showing where I'm pointing.
		double b = 1 - ((double)timeUntilJuggle)/juggleWaitTime;
		b = 0.5 + b/2;
		int RGB = Helpers.round(255*b);
		g.setColor(new Color(RGB,RGB,RGB));

		g.drawLine(
			Helpers.round(x),
			Helpers.round(y),
			Helpers.round(x+DRAW_RAD*Math.cos(angle)),
			Helpers.round(y+DRAW_RAD*Math.sin(angle)));
	}
}