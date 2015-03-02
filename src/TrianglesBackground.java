import java.awt.*;

public class TrianglesBackground extends BackgroundGraphics {
	private double rad, angle; //The smallest, elementary radius. < 1; and the basic tilt angle for the whole of drawing.
	private final double MAX_RAD = 10; //The biggest we let rad be before setting it back to a very small amount.
	private final double NORM_RAD_MULT = 1.01;
	private final double RAD_MULT_INC = 0.005; //When get points
	private final double RAD_MULT_DEC = 0.0005; //For a normal frame, going down to NORM_RAD_MULT
	private double cx, cy;
	private double radMult;

	private final double NORM_SPIN = 1*Math.PI/180; //Minimum absolute value of spin
	private final double SPIN_MULT = 1.1;
	private final double SPIN_DEC = 0.1*Math.PI/180; //Absolute value goes down to NORM_SPIN
	private double spin;

	public boolean YScrolling() { return false; }

	public TrianglesBackground(Main m) {
		super(m);
		cx = main.WIDTH/2; cy = main.HEIGHT/2;

		rad = 1; angle = 0;
		radMult = NORM_RAD_MULT;

		spin = -NORM_SPIN;
	}

	public void compute() {
		rad *= radMult;
		if (radMult > NORM_RAD_MULT) radMult -= RAD_MULT_DEC;
		while (rad > MAX_RAD) rad /= 4; //Divide b 2 twice;
		//above: go back 2 layers of triangles because they alternate in angle and we want r /= 4 to be unnoticeable.

		angle += spin;
		//Now make spinning slower over time
		if (spin > 0)
			if (spin > NORM_SPIN) spin -= SPIN_DEC;
		else
			if (spin < -NORM_SPIN) spin += SPIN_DEC;
	}

	public void draw(Graphics2D g) {
		int xpoints[] = new int[3]; int ypoints[] = new int[3];
		double anglesA[] = { angle + Math.PI*0/3, angle + Math.PI*2/3, angle + Math.PI*4/3 }; //Since we use them so much and they are the same.
		double anglesB[] = { angle + Math.PI*1/3, angle + Math.PI*3/3, angle + Math.PI*5/3 };
		double anglesArray[];
		boolean usingAnglesA = true;

		g.setColor(Helpers.randHSV());

		for (double r = rad; r < main.WIDTH*3; r *= 2) {
			anglesArray = (usingAnglesA)? anglesA : anglesB;
			usingAnglesA = !usingAnglesA;
			for (int i = 0; i < 3; i++) {
				xpoints[i] = Helpers.round(cx + r*Math.cos(anglesArray[i])); ypoints[i] = Helpers.round(cy + r*Math.sin(anglesArray[i]));
			}
			g.drawPolygon(new Polygon(xpoints,ypoints,3));
		}
	}

	public void getAction(double x, double y, int score, double playerVX, double playerVY) {
		radMult = NORM_RAD_MULT + RAD_MULT_INC * score;

		spin = NORM_SPIN * Math.pow(SPIN_MULT,score);
		if (score % 2 == 1) spin *= -1;
	}
}