import java.util.*;

public class Level9 extends LevelCreator {
	public List<GameObject> makeObjects(Main m) {
		m.backgrounder.getBackground(new CirclesBackground(m));
		m.backgrounder.getBackground(new ZoomDownBackground(m));

		m.player = new Player(m,50,-100);
		get(m.player);
		get(new LevelBottom(m));
		get(new MultiJump(m,250,-180));

		double xL = 50, xR = 450;
		double y1 = -240, y2 = -360, y3 = -440;
		double gap = 80;
		get(new Line(m,0, xL,y1,		xR-gap,y1));
		get(new Line(m,0, gap,y2,	xR,y2));
		get(new Line(m,0, xL,y3,		xR-gap,y3));
		get(new Line(m,0, xR,y1,	xR,y3));
		get(new Line(m,0, xL,y1,		xL,y3));
		get(new Bonus(m,250,(y1+y2)/2));
		get(new Bonus(m,250,(y2+y3)/2));

		// main x y angle speed freq
		get(new EvilOrbFirer(m, m.LEFT, -500, -30, 4, 80));
		get(new EvilOrbFirer(m, m.RIGHT, -500, -150, 4, 80));
		//Next: EvilOrbFirers that change direction!

		double p1 = -600;
		for (double x = 50; x <= 450; x += 200)
			for (double y = p1; y >= p1 - 400; y -= 200)
				get(new JMan(m,x,y));
		for (double x = 150; x <= 350; x += 200)
			for (double y = p1 - 100; y >= p1 - 300; y -= 200)
				get(new Bonus(m,x,y));
		JellyMom jm = new JellyMom(m,250,p1-200,16,3);
		double fireRangeBuffer = 100;
		jm.setFireRange(p1-400-fireRangeBuffer,p1+fireRangeBuffer);
		get(jm);

		double p2 = p1 - 400;

		double p3 = p2-200-m.X_BUF;
		get(new Line(m,0, m.LEFT,p2,	200,p3));
		get(new Line(m,0, 300,p3,		m.RIGHT,p2));
		get(new SinMovingLine(m));
		double rad = 100, k = 100; int lineTime = 120;
		//							main rad ang	x1 y1		x2	y2	 		time offset
		get(new SinMovingLine(m,rad,0,	rad,p3,		300,p3,		lineTime));
		get(new SinMovingLine(m,rad,0,	500-rad,p3,	200,p3,		lineTime));

		k = 120;
		// main x y angChng spd freq angRestricted minAng maxAng
		get(new SpinningEvilOrbFirer(m, m.LEFT, p3-k, 4, 4, 60, true, -60, 0));
		get(new SpinningEvilOrbFirer(m, m.RIGHT, p3-k, 4, 4, 60, true, -180, -120));
		get(new SpinningEvilOrbFirer(m, m.LEFT, p3-2*k, 3, 2, 60, true, -90, -30));
		get(new SpinningEvilOrbFirer(m, m.RIGHT, p3-2*k, 3, 2, 60, true, -150,-90));
		get(new SpinningEvilOrbFirer(m, 250, p3-4*k, -8, 6, 30));

		double p4 = p3 - 5*k;

		k = 100; int time = 360;
		//								main rad ang	x1 y1				x2	y2	 			time dissapear
		get(new Line(m,0, xL,p4,		xL,p4-3*k));
		get(new Line(m,0, xR,p4,	xR,p4-3*k));
		get(new LinearMovingLine(m,200,0,		m.LEFT,p4-0*k,	m.RIGHT,p4-0*k,time));
		get(new Bonus(m, 250, p4 - 0.5*k));
		get(new LinearMovingLine(m, 233, 0, m.RIGHT, p4-1*k, m.LEFT, p4-1*k, time));
		get(new Bonus(m, 250, p4 - 1.5*k));
		get(new LinearMovingLine(m, 267, 0, m.LEFT, p4-2*k, m.RIGHT, p4-2*k, time));
		get(new Bonus(m, 250, p4 - 2.5*k));
		get(new LinearMovingLine(m, 300, 0, m.RIGHT, p4-3*k, m.LEFT, p4-3*k, time));

		double p5 = p4 - 6*k;

		k = 100;
		get(new RotatingLine(m, 250,p5, 250,-1));
		get(new Bonus(m,250,p5)); //Behind the JellyMom, so can't see it!
		jm = new SmartJellyMom(m,250,p5,20,4);
		double fireRange = 2*k;
		jm.setFireRange(p5-fireRange,p5+fireRange);
		get(jm);
		get(Bonus.bonusRing(m, 250, p5, 150, 16));

		double p6 = p5 - 3*k;

		k = 250;
		//						main x y				spin frec strength
		get(new Bonus(m,250,p6-0*k)); get(new Bonus(m,250,p6-1*k)); get(new Bonus(m,250,p6-2*k)); get(new Bonus(m,250,p6-3*k));
		get(Bonus.bonusRing(m,250,p6-0*k,150,6)); get(Bonus.bonusRing(m,250,p6-1*k,120,5));
		get(Bonus.bonusRing(m,250,p6-2*k, 90,4)); get(Bonus.bonusRing(m,250,p6-3*k, 60,3));
		// main x y angChange freq strength
		get(new SpinJellyMom(m, 250, p6-0*k, 0.25, 24, 3));
		get(new SpinJellyMom(m, 250, p6-1*k, -0.5, 12, 3));
		get(new SpinJellyMom(m, 250, p6-2*k, 0.75, 8, 3));
		get(new SpinJellyMom(m, 250, p6-3*k, -1, 6, 3));

		m.CUR_LEVEL_HEIGHT = -(p6-4*k);

		return objs;
	}
}