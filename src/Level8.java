import java.util.*;

public class Level8 extends LevelCreator {
	public List<GameObject> makeObjects(Main m) {
		m.backgrounder.getBackground(new WanderersBackground(m));

		m.player = new Player(m,250,-100);
		get(m.player);
		get(new LevelBottom(m));

		get(new EvilOrbFirer(m,150,20,-90,6,60));
		get(new EvilOrbFirer(m,350,20,-90,7,60));

		//				main	x	y		rad ang
		get(new Line(m,1, 100,-125, 50,30));
		get(new JMan(m,450,-250));

		double p1 = -400;
		double k = 100;

		double x1 = 150, x2 = 350, x3 = 425;

		List<Double> xs = new ArrayList<Double>(); List<Double> ys = new ArrayList<Double>();
		xs.add(100.); ys.add(-250.);
		xs.add(100.); ys.add(p1);
		xs.add(x3); ys.add(p1);
		xs.add(x3); ys.add(p1-5*k);
		xs.add(0.); ys.add(p1-5*k);
		xs.add(0.); ys.add(p1-3*k);
		xs.add(x1); ys.add(p1-3*k);
		xs.add(x1); ys.add(p1-2*k);
		xs.add(0.); ys.add(p1-2*k);
		get(Line.lineSequence(m,xs,ys));
		xs = new ArrayList<Double>(); ys = new ArrayList<Double>();
		xs.add(x1); ys.add(p1-4*k);
		xs.add(x2); ys.add(p1-4*k);
		xs.add(x2); ys.add(p1-1*k);
		xs.add(m.LEFT); ys.add(p1-1*k);
		get(Line.lineSequence(m,xs,ys));

		get(new JellyMom(m,250,p1-5*k/2));
		get(new JMan(m,50,p1-5*k/2));
		get(new Cannon(m, x3+(500-x3)/2,p1-3*k,-90,16));
		get(new Cannon(m, x2+(x3-x2)/2,p1-2*k,-90,10));

		get(new Line(m,0, x3,p1-1*k, m.RIGHT,p1-1*k));

		double p2 = p1 - 5*k;
		k = 200;
		get(new EvilOrbFirer(m,0,p2-1*k,		-15,8,40));
		get(new EvilOrbFirer(m,500,p2-2*k,	-165,8,40));




		double p3 = p2 - 2*k - 250;

		double gap = 80;
		get(new Line(m,0, 0,p2-2*k, 250 - gap/2,p3));
		get(new Line(m,0, 250 + gap/2,p3, 500,p2-2*k));
		get(new JMan(m,50,p3+125)); get(new JMan(m,450,p3+125));
		get(new Cannon(m,100,p3+25,-70,16));
		get(new Cannon(m,400,p3+25,-110,16));

		k = 400; double str = 18;

		get(new SpinCannon(m,250,p3-1*k,-8,str));
		get(new Billboard(m,250,p3-1.5*k,200,125,"arrowL"));
		get(new SpinCannon(m,50,p3-2*k,12,str));
		get(new Billboard(m,250,p3-2.5*k,200,125,"arrowR"));
		get(new SpinCannon(m,450,p3-3*k,-8,str));
		//								main	x1 y1			x2 y2			sinTime spinSpeed strength
		get(new SinMoveSpinCannon(m,	100,p3-4*k,	400,p3-4*k,	120,8,str));
		get(new SinMoveSpinCannon(m,	100,p3-5*k,	400,p3-5*k,	80,6,str));
		get(new SinMoveSpinCannon(m,	150,p3-5.5*k,50,p3-6.5*k,60,5,str));
		get(new SinMoveSpinCannon(m,	350,p3-5.5*k,450,p3-6.5*k,60,-5,str));

		get(new EvilOrbFirer(m,0,p3-6*k,0,2,60));
		get(new EvilOrbFirer(m,500,p3-6*k,180,2,60));


		m.CUR_LEVEL_HEIGHT = -(p3 - 7*k);

		return objs;
	}
}