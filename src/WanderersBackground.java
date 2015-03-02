import java.awt.*;

public class WanderersBackground extends SpawnerBackground {
	public boolean YScrolling() { return false; }

	protected double SPAWN_RAND_CHANCE() { return 1; }

	private final double MIN_Z_SPEED = 1;
	private final double Z_SPEED_INC = 0.25; //When get points
	private final double Z_SPEED_DEC = 0.05; //Every frame
	private final double MAX_Z = 250;
	private double Z_SPEED;

	private class Wanderer extends Spawned {
		private static final double eyeDistFromScreen = 20;
		private static final double STAR_FADE_RAD = 75; //Radius at which opacity is 100%; then fades out by inverse square
		private static final int DRAW_RADIUS = 3;
		private static final double SPARKLE_CHANCE = 0.08;
		private static final double WANDER = 3.0;

		double x,y,z; Color color;
		double vx, vy; //vz = Z_SPEED
		//X and Y are centered about main.WIDTH/2, main.HEIGHT/2

		public Wanderer(double ax, double ay, double avx, double avy, Color c) {
			x = ax; y = ay; color = c;
			vx = avx;
			vy = avy;
			z = 0;
		}
		public Wanderer(double ax, double ay, double avx, double avy) { this(ax,ay,avx,avy,Helpers.randHSV(50,100)); }

		public void compute() {
			x += vx; y += vy;
			vx += WANDER*(Math.random()-.5); vy += WANDER*(Math.random()-.5);
			z += Z_SPEED;
			dead = z >= MAX_Z;
		}

		public void draw(Graphics2D g) {
			//Random sparkling
			Color thisFrameColor = color;
			if (Math.random() < SPARKLE_CHANCE) { thisFrameColor = Color.WHITE; }

			//Fades out by inverse square
			int alpha = Helpers.round(255 * 1/((z/STAR_FADE_RAD)*(z/STAR_FADE_RAD))); if (alpha>255) alpha=255; if (alpha < 0) alpha = 0;
			g.setColor(new Color(thisFrameColor.getRed(),thisFrameColor.getGreen(),thisFrameColor.getBlue(),alpha));

			int convX = main.WIDTH/2 + Helpers.round(x*(eyeDistFromScreen/(eyeDistFromScreen+z)));
			int convY = main.HEIGHT/2 + Helpers.round(y*(eyeDistFromScreen/(eyeDistFromScreen+z)));
			g.drawOval(convX-DRAW_RADIUS,convY-DRAW_RADIUS,2*DRAW_RADIUS,2*DRAW_RADIUS);
		}
	}

	public WanderersBackground(Main m) { super(m); Z_SPEED = MIN_Z_SPEED; }

	public void compute() {
		super.compute();
		if (Z_SPEED > MIN_Z_SPEED) Z_SPEED -= Z_SPEED_DEC;
	}

	protected void getNew(double x, double y, double vx, double vy) {
		spawneds.add(new Wanderer(x,y,vx,vy));
	}
	protected void getNew(double x, double y) {
		getNew(x,y,0,0);
	}
	protected void getNew() {
		double r = main.WIDTH;
		double ang = Math.random()*Math.PI*2;
		getNew(r*Math.cos(ang), r*Math.sin(ang));
	}

	public void getAction(double x, double y, int score, double playerVX, double playerVY) {
		Z_SPEED = MIN_Z_SPEED + Z_SPEED_INC*score;
		for (int i = 0; i < Helpers.fastGrowFunction(score); i++) getNew(x-main.WIDTH/2, y-main.HEIGHT/2-main.scrollY, playerVX, playerVY);
	}
}