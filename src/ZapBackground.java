import java.awt.*;

public class ZapBackground extends SpawnerBackground {
	protected double SPAWN_RAND_CHANCE() { return 0.03; }
	final private int MIN_NUM = 3, ZAP_IN_TIME = 8, ZAP_OUT_TIME = 32;

	private class Zap extends Spawned {
		double x,y; int num; Color color;
		int time; double rInner, rOuter; boolean zappingIn;
		double MAX_R;

		public Zap(double ax, double ay, int aNum, Color c) {
			x = ax; y = ay; num = aNum; color = c;
			time = ZAP_IN_TIME; zappingIn = true;
			MAX_R = main.WIDTH;
		}
		public Zap(double ax, double ay, int aNum) { this(ax,ay,aNum,Helpers.randHSV()); }

		public void compute() {
			if (zappingIn) { time--; if (time <= 0) zappingIn = false; }
			else { time++; if (time >= ZAP_OUT_TIME) dead = true; }

			if (zappingIn) {
				if (time > ZAP_IN_TIME/2) {
					rInner = MAX_R*(time-ZAP_IN_TIME/2)/(ZAP_IN_TIME/2); rOuter = MAX_R; }
				else {
					rInner = 0; rOuter = MAX_R*time/(ZAP_IN_TIME/2); }
			}
			else {
				rInner = MAX_R * time/ZAP_OUT_TIME;
				rOuter = MAX_R*2 * time/ZAP_OUT_TIME;
			}
		}

		public void draw(Graphics2D g) {
			g.setColor(color);
			for(int i = 0; i < num; i++) {
				double ang = 0 + Math.PI*2*i/num;
				g.drawLine(	Helpers.round(x+rInner*Math.cos(ang)),Helpers.round(y+rInner*Math.sin(ang)),
								Helpers.round(x+rOuter*Math.cos(ang)),Helpers.round(y+rOuter*Math.sin(ang)) );
			}
		}
	}

	public ZapBackground(Main m) { super(m); }

	protected void getNew(double x, double y, int num) { spawneds.add(new Zap(x,y,num)); }
	protected void getNew(double x, double y) { getNew(x,y,MIN_NUM);  }
	protected void getNew() { getNew(Math.random()*main.WIDTH, main.scrollY+Math.random()*main.HEIGHT); }

	public void getAction(double x, double y, int score, double playerVX, double playerVY) {
		getNew(x,y,MIN_NUM+Helpers.slowGrowFunction(score)-1);
	}
}