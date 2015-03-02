import java.util.*;

public class Level2 extends LevelCreator {
	public List<GameObject> makeObjects(Main m) {
		m.backgrounder.getBackground(new ZapBackground(m));

		m.player = new Player(m,m.WIDTH/2,-80);
		get(m.player);
		get(new LevelBottom(m));

		get(new JMan(m,150,-80));
		get(new JMan(m,350,-80));
		//								main rad ang		x1 y1				x2	y2	 		time dissapear offset
		double p1 = -180;
		get(new LinearMovingLine(m, 50, 0,		-100, p1,		600, p1,			80, true));
		get(new LinearMovingLine(m, 20, 0,		400, p1,			400, p1-260,	160, true));
		get(new LinearMovingLine(m, 20, 0,		400, p1,			400, p1-260,	160, true, 80));
		get(new LinearMovingLine(m, 40, 0,		60, p1-100,		280, p1-100,	120, false, 60));
		get(new LinearMovingLine(m, 40, 0,		60, p1-180,		280, p1-180,	120, false));
		get(new Bonus(m,160,p1-160));

		double p2 = p1 - 280;
		double p3 = p2 - 200;

		get(new Line(m,1,	250,p2, 60,0));
		get(new LinearMovingLine(m, 40, 30,		60, p2,			440, p3,				140, false));
		get(new LinearMovingLine(m, 40, -30,		440, p2,			60, p3,			140, false));
		get(new Line(m,1,	250,p3, 60,0));

		double k = 60; double r = 40;
		get(new LinearMovingLine(m, r, 0,		r, p3-1*k,		500-r, p3-1*k,		160, false));
		get(new LinearMovingLine(m, r, 0,		r, p3-2*k,		500-r, p3-2*k,		130, false));
		get(new LinearMovingLine(m, r, 0,		r, p3-3*k,		500-r, p3-3*k,		100, false));
		get(new LinearMovingLine(m, r, 0,		r, p3-4*k,		500-r, p3-4*k,		80, false));
		get(new LinearMovingLine(m, r, 0,		r, p3-5*k,		500-r, p3-5*k,		60, false));
		get(new LinearMovingLine(m, r, 0,		r, p3-6*k,		500-r, p3-6*k,		50, false));
		get(new LinearMovingLine(m, r, 0,		r, p3-7*k,		500-r, p3-7*k,		40, false));

		m.CUR_LEVEL_HEIGHT = -(p3-8*k);

		return objs;
	}
}