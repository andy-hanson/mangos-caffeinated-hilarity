import java.awt.*;

public class ZoomDownBackground extends SpawnerBackground {
	public boolean YScrolling() { return false; }

	protected double SPAWN_RAND_CHANCE() { return spawnChance; }

	private final double MIN_Y_SPEED = 4;
	private final double Y_SPEED_INC = 1; //When get points
	private final double Y_SPEED_DEC = 0.01; //Every frame
	private final double MIN_SPAWN_CHANCE = .5;
	private final double SPAWN_CHANCE_INC = 0.125; //When get points
	private final double SPAWN_CHANCE_DEC = 0.0125; //Every frame
	private double spawnChance;
	private double Y_SPEED;

	private class Zoomer extends Spawned {
		private static final int DRAW_RADIUS = 3;
		private static final double SPARKLE_CHANCE = 0.08;
		int x; double y; //x will never change, so might as well store it as an int!
		Color color;

		public Zoomer(int ax, Color c) {
			x = ax; y = 0; color = c;
		}
		public Zoomer(int ax) {
			this(ax,Helpers.randHSV(50,100));
		}

		public void compute() {
			y += Y_SPEED;
			dead = y >= main.HEIGHT;
		}

		public void draw(Graphics2D g) {
			if (Math.random() < SPARKLE_CHANCE) g.setColor(Color.WHITE);
			else g.setColor(color);
			g.drawOval(x-DRAW_RADIUS,Helpers.round(y-DRAW_RADIUS),2*DRAW_RADIUS,2*DRAW_RADIUS);
		}
	}

	public ZoomDownBackground(Main m) {
		super(m);
		Y_SPEED = MIN_Y_SPEED;
		spawnChance = MIN_SPAWN_CHANCE;
	}

	public void compute() {
		super.compute();
		if (Y_SPEED > MIN_Y_SPEED) Y_SPEED -= Y_SPEED_DEC;
		if (spawnChance > MIN_SPAWN_CHANCE) spawnChance -= SPAWN_CHANCE_DEC;
	}

	protected void getNew(double x) {
		spawneds.add(new Zoomer(Helpers.round(x)));
	}
	protected void getNew() {
		getNew(Math.random()*main.HEIGHT);
	}

	public void getAction(double x, double y, int score, double playerVX, double playerVY) {
		Y_SPEED = MIN_Y_SPEED + Y_SPEED_INC*score;
		spawnChance = MIN_SPAWN_CHANCE + SPAWN_CHANCE_INC*score;
		for (int i = 0; i < Helpers.fastGrowFunction(score); i++) getNew(x-main.WIDTH/2);
	}
}