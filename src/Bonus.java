import java.util.*;

class Bonus extends CircleObject
{
	public Bonus(Main m, double ax, double ay) {
		super(m, ax, ay, 24, "bonus");
	}

	public void compute() {
		if (main.player.collideCircle(this)) {
			main.player.status.getBonus(this);
			dead = true;
		}
	}

	public static List<Bonus> bonusRing(Main m, double x, double y, double rad, int num, double angOffset) {
		List<Bonus> l = new ArrayList<Bonus>();
		for(int i = 0; i < num; i++) {
			double ang = angOffset + Math.PI*2 * i/num;
			l.add(new Bonus(m, x + rad*Math.cos(ang), y + rad*Math.sin(ang)));
		}
		return l;
	}
	public static List<Bonus> bonusRing(Main m, double x, double y, double rad, int num) {
		return bonusRing(m,x,y,rad,num,0);
	}
}