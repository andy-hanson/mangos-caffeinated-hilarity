import java.awt.Graphics2D;
import java.util.List;
import java.util.Arrays;

public class JMan extends CircleObject {
	//Can bounce off JMan indefinitely without losing combo!

	public boolean happy;

	//Can't be hit for a few frames after getting hit
	private static final int MAX_HIT_WAIT=8;
	private int hitWait;

	public JMan(Main m, double x, double y) {
		super(m,x,y,18);
		img = main.helper.getImage(Helpers.pathJoin("enemies","JManSad"));
		happy = false;
	}

	public void compute() {
		super.compute();
		if (hitWait > 0) hitWait--;

		if (collideCircle(main.player) && hitWait == 0) {
			if (!happy) {
				if (main.player.attacking) {
					main.player.getHitJMan(this);
					happy = true;
					img = main.helper.getImage(Helpers.pathJoin("enemies","JManHappy"));
					main.player.getMadeJManHappy(this);
					main.helper.playSound("JManBounce");
				}
				else main.player.getHurt();
			}
			else { main.player.getHitJMan(this); main.helper.playSound("JManBounce"); }
			hitWait = MAX_HIT_WAIT;
		}
	}

	public void draw(Graphics2D g) { super.draw(g); }

	public static List<JMan> JManPlus(Main m, double x, double y, double r) {
		return Arrays.asList(
			new JMan(m,x-r,y),
			new JMan(m,x+r,y),
			new JMan(m,x,y-r),
			new JMan(m,x,y+r));
	}
}