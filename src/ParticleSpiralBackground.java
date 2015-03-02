import java.awt.*;

public class ParticleSpiralBackground extends SpawnerBackground {
	protected double SPAWN_RAND_CHANCE() { return 0.08; }
	private double MIN_SPIN = -0.5; private double SPIN_INC = -0.5; // Degrees/frame
	private double MIN_GROW = 1; private double GROW_INC = 1; // Degrees/frame
	private double MAX_PARTICLE_RAD;

	public boolean YScrolling() {
		return true;
	}

	protected class Particle extends Spawned {
		static final int PARTICLE_RAD = 3;
		double cx, cy, rad, ang, spin, grow; int num; Color color;

		public Particle(double x, double y, double rotSpeed, double flyOutSpeed, int anum, Color c) {
			//SPIN IN DEGREES/FRAME
			cx = x; cy = y; rad = 0; ang = Math.random()*2*Math.PI;
			spin = rotSpeed*Math.PI/180; grow = flyOutSpeed;
			num = anum;
			color = c;
		}
		public Particle(double x, double y, double rotSpeed, double flyOutSpeed, int anum) {
			this(x,y,rotSpeed,flyOutSpeed,anum,Helpers.randHSV());
		}

		public void compute() {
			ang += spin;
			rad += grow;
			if (rad>MAX_PARTICLE_RAD)
				dead = true;
		}

		public void draw(Graphics2D g) {
			g.setColor(color);
			for (int i = 0; i < num; i++) {
				int x = Helpers.round(cx + rad*Math.cos(ang + 2*Math.PI*i/num)); int y = Helpers.round(cy + rad*Math.sin(ang + 2*Math.PI*i/num));
				g.drawOval(Helpers.round(x-PARTICLE_RAD),Helpers.round(y-PARTICLE_RAD),PARTICLE_RAD*2,PARTICLE_RAD*2);
			}
		}
	}

	public ParticleSpiralBackground(Main m) {
		super(m);
		MAX_PARTICLE_RAD = m.WIDTH*2;
	}

	private void getNew(double x, double y, double spin, double grow, int num) {
		spawneds.add(new Particle(x,y,spin,grow,num));
	}
	protected void getNew() {
		getNew(Math.random()*main.WIDTH, main.scrollY+Math.random()*main.HEIGHT, MIN_SPIN, MIN_GROW, 1);
	}

	//Does NOT use Helpers.slowGrowFunction
	public void getAction(double x, double y, int score, double playerVX, double playerVY) {
		getNew(x,y,MIN_SPIN+SPIN_INC*score,MIN_GROW+GROW_INC*score,score);
	}
}