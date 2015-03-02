import java.awt.*;

public class PolarLightningBackground extends SpawnerBackground {
	public boolean YScrolling() {
		return false;
	}

	private final double MIN_SPAWN_CHANCE = 0.08;
	private final double SPAWN_CHANCE_INC = 0.02; //When get points
	private final double SPAWN_CHANCE_DEC = 0.0001; //Per frame
	private double spawnChance;

	protected double SPAWN_RAND_CHANCE() {
		return spawnChance;
	}

	private final double MIN_Z_SPEED = 2;
	private final double Z_SPEED_INC = 0.75; //When get points
	private final double Z_SPEED_DEC = 0.005; //Every frame
	private double LIGHTNING_RAD;
	private double Z_SPEED;

	private class Lightning extends Spawned {
		private static final double eyeDistFromScreen = 20;
		private static final double LIGHTNING_FADE_RAD = 75; //Radius at which opacity is 100%; then fades out by inverse square
		private static final double SPARKLE_CHANCE = 0.06;
		private static final double LIGHTNING_LENGTH = 80;
		private static final double angOff = Math.PI/24;

		double LIGHTNING_RAD, ang; Color color;
		double z1, z2, z3; //z1 is furthest (greatest) z, closest is z3 = z1-length

		//X and Y are centered about main.WIDTH/2, main.HEIGHT/2

		public Lightning(double aang, Color c) {
			LIGHTNING_RAD = main.WIDTH;
			ang = aang;
			color = c;
			z1 = 250; z2 = z1 - LIGHTNING_LENGTH/2; z3 = z1 - LIGHTNING_LENGTH;
		}

		public Lightning(double aang) { this(aang,Helpers.randHSV(50,100)); }

		public void compute() {
			z1 -= Z_SPEED; z2 -= Z_SPEED; z3 -= Z_SPEED;
			dead = z1 <= 0;
		}

		public void draw(Graphics2D g) {
			//Random sparkling
			Color thisFrameColor = color;
			if (Math.random() < SPARKLE_CHANCE) { thisFrameColor = Color.WHITE; }

			//Fades out by inverse square
			int alpha;
			if (z3 <= LIGHTNING_FADE_RAD) { alpha = 255; }
			else alpha = Helpers.round(255 * 1/((z3/LIGHTNING_FADE_RAD)*(z3/LIGHTNING_FADE_RAD)));

			g.setColor(new Color(thisFrameColor.getRed(),thisFrameColor.getGreen(),thisFrameColor.getBlue(),alpha));

			double xMid = LIGHTNING_RAD*Math.cos(ang); double yMid = LIGHTNING_RAD*Math.sin(ang);
			double xOff1 = LIGHTNING_RAD*Math.cos(ang+angOff); double yOff1 = LIGHTNING_RAD*Math.sin(ang+angOff);
			double xOff2 = LIGHTNING_RAD*Math.cos(ang-angOff); double yOff2 = LIGHTNING_RAD*Math.sin(ang-angOff);

			g.drawLine(convert(xMid,z1),convert(yMid,z1), convert(xOff1,z2),convert(yOff1,z2));
			g.drawLine(convert(xOff1,z2),convert(yOff1,z2), convert(xOff2,z2),convert(yOff2,z2));
			g.drawLine(convert(xOff2,z2),convert(yOff2,z2), convert(xMid,z3),convert(yMid,z3));
		}

		int convert(double x, double z) {
			//Works for both x and y
			if (z <= 0) return main.WIDTH/2 + Helpers.round(x);
			return main.WIDTH/2 + Helpers.round(x*(eyeDistFromScreen/(eyeDistFromScreen+z)));
		}
	}

	public PolarLightningBackground(Main m) {
		super(m);
		LIGHTNING_RAD = main.WIDTH;
		Z_SPEED = MIN_Z_SPEED;
	}

	public void compute() {
		super.compute();
		if (Z_SPEED > MIN_Z_SPEED)
			Z_SPEED -= Z_SPEED_DEC;
		if (spawnChance > MIN_SPAWN_CHANCE)
			spawnChance -= SPAWN_CHANCE_DEC;
	}

	protected void getNew(double ang) {
		spawneds.add(new Lightning(ang));
	}
	protected void getNew() {
		double ang = Math.random() * 2*Math.PI;
		getNew(ang);
	}

	public void getAction(double x, double y, int score, double playerVX, double playerVY) {
		Z_SPEED = MIN_Z_SPEED + Z_SPEED_INC*score;
		spawnChance = MIN_SPAWN_CHANCE + SPAWN_CHANCE_INC*score;
	}
}