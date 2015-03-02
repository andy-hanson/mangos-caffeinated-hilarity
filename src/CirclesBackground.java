import java.awt.*;

public class CirclesBackground extends SpawnerBackground
{
	protected double SPAWN_RAND_CHANCE() { return 0.04; }
	final private double MIN_GROW_SPEED = 1.0, GROW_SPEED_MULT = 1.25; //Exponential increase in growSpeed based on score

	private class Circle extends Spawned {
		double x,y; double rad, growSpeed; Color color;
		private double maxRad;
		public Circle(double ax, double ay, double aGrowSpeed, Color c) {
			x = ax; y = ay; growSpeed = aGrowSpeed; rad = 0.0; color = c;
			maxRad = main.WIDTH*2/3; dead = false;
		}
		public Circle(double ax, double ay, double growSpeed) { this(ax,ay,growSpeed,Helpers.randHSV()); }

		public void compute() { rad += growSpeed; if (rad >= maxRad) dead=true; }
		public void draw(Graphics2D g) {
			Color c = new Color(color.getRed(),color.getGreen(),color.getBlue(),Helpers.round(255-255*rad/maxRad));
			g.setColor(c);
			g.drawOval(Helpers.round(x-rad),Helpers.round(y-rad),
							Helpers.round(rad*2),Helpers.round(rad*2));
		}
	}

	public CirclesBackground(Main m) { super(m); }

	protected void getNew(double x, double y, double growSpeed) {
		spawneds.add(new Circle(x,y,growSpeed));
	}
	protected void getNew(double x, double y) {
		getNew(x,y,MIN_GROW_SPEED);
	}
	protected void getNew() {
		getNew(Math.random()*main.WIDTH, main.scrollY+Math.random()*main.HEIGHT);
	}

	public void getAction(double x, double y, int score, double playerVX, double playerVY) {
		double growSpeed = MIN_GROW_SPEED;
		for (int i = 0; i < Helpers.slowGrowFunction(score); i++) {
			getNew(x,y,growSpeed);
			growSpeed *= GROW_SPEED_MULT;
		}
	}
}