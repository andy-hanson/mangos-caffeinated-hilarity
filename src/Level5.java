import java.util.*;

public class Level5 extends LevelCreator {
	public List<GameObject> makeObjects(Main m) {
		m.backgrounder.getBackground(new SpeedyStarsBackground(m));

		m.player = new Player(m,50,-80);
		get(m.player);
		get(new LevelBottom(m));

		//Short segment with cannons firing left and right
		get(new Cannon(m,25,-60,0,		18));
		get(new Cannon(m,475,-60,-150,	8));
		get(new Line(m,0, 250,-50,250,-150));
		get(new Bonus(m,475,-140));
		get(new Cannon(m,475,-180,-170,14));
		get(new Bonus(m,25,-240));
		get(new Cannon(m,25,-180,-45,	14));

		double p1 = -180 - 140;

		//Spinning cannon and stuff to shoot at
		get(new SpinCannon(m,250,p1,8,14));
		get(new Line(m,0,	20,p1-40,	20,p1+40));
		get(new Line(m,0,	480,p1-40,	480,p1+40));
		get(new Line(m,0,	230,p1+40,	270,p1+40));
		get(new JMan(m,150,p1-100));
		get(new JMan(m,350,p1-100));
		get(new Bonus(m,50,p1-150));
		get(new Bonus(m,450,p1-150));

		//Shoot through the gap
		double p2 = p1-300;
		double gap = 40;
		get(new Line(m,0,	m.LEFT,p2,	250-gap,p2));
		get(new Line(m,0, 250+gap,p2,	m.RIGHT,p2));
		//									main x y				rad #cannons spin		cannonSpin strength
		get(new SpinningCannonCircle(m,250,p2-200,	150, 6, 3,				-5, 12));
		get(Bonus.bonusRing(m,250,p2-200,75,6));
		get(Bonus.bonusRing(m,250,p2-200,200,6));

		get(new Billboard(m,250,p2-550,175,200,"arrowU"));

		double p3 = p2 - 800;
		gap = 20;
		get(new Line(m,0,	m.LEFT,p3,	250-gap,p3));
		get(new Line(m,0, 250+gap,p3,	m.RIGHT,p3));

		double spin = -12;
		get(new SpinCannon(m,250,p3-120,spin,12));
		get(new RotatingLine(m,125,p3-120,100,-2));
		get(new RotatingLine(m,375,p3-120,100,-2));

		double p4 = p3 - 320;
		gap = 30;
		get(new Line(m,0,	m.LEFT,p4,	250-gap,p4));
		get(new Line(m,0, 250+gap,p4,	m.RIGHT,p4));
		//								main rad ang	x1 y1			x2	y2	 			time dissapear
		get(new LinearMovingLine(m,50,90,		m.LEFT,p4-50,m.RIGHT,p4-50,120,true));
		get(new LinearMovingLine(m,50,90,		m.RIGHT,p4-150,m.LEFT,p4-150,120,true));

		m.CUR_LEVEL_HEIGHT = -(p4-200);

		return objs;
	}
}