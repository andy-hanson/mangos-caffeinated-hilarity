import java.util.*;

public class Level7 extends LevelCreator {
	public List<GameObject> makeObjects(Main m) {
		m.backgrounder.getBackground(new PolarLightningBackground(m));

		m.player = new Player(m,250,-100);
		//m.player = new Player(m,250,-550); for later section
		get(m.player);
		get(new LevelBottom(m));

		List<Double> xs = new ArrayList<Double>(); List<Double> ys = new ArrayList<Double>();
		xs.add(200.); ys.add(0.);
		xs.add(200.); ys.add(-125.);
		xs.add(300.); ys.add(-125.);
		xs.add(300.); ys.add(-75.);
		get(Line.lineSequence(m,xs,ys));
		xs = new ArrayList<Double>(); ys = new ArrayList<Double>();
		xs.add(400.); ys.add(0.);
		xs.add(400.); ys.add(-250.);
		xs.add(100.); ys.add(-250.);
		xs.add(100.); ys.add(-75.);
		get(Line.lineSequence(m,xs,ys));
		get(new Line(m,0, 0,0, 0,-150));
		//xs = new ArrayList<Double>(); ys = new ArrayList<Double>();
		//xs.add(400); ys.add(0);
		//xs.add(400); ys.add(-250);
		//xs.add(0); ys.add(-250.);
		//get(Line.lineSequence(m,xs,ys));

		get(new JMan(m,450,-100)); get(new JMan(m,450,-200));

		double p1 = -250;

		get(new Line(m,0, 250,p1, 500,p1-250));
		get(new Line(m,0,	0,p1-250,250,p1-250));

		get(new JellyMom(m,250,-250));

		//								main rad ang	x1 y1				x2	y2	 			time dissapear
		get(new LinearMovingLine(m,50,90,		250,p1-62.5,	0,p1-62.5,		80,true));
		get(new LinearMovingLine(m,62.5,90,		0,p1-187.5,		250,p1-187.5,	80,true));

		get(new RotatingLine(m,350,p1-250,100,-2));
		get(new Line(m,0,	500,p1-250, 0,p1-750));

		get(new JMan(m,425,p1-250));

		get(new JMan(m,250,p1-375));

		get(new Cannon(m,50,p1-300, -45)); get(new Cannon(m,50,p1-450, 45));
		get(new Cannon(m,200,p1-300, -135)); //No cannon in up-right because JellyMom is there
		get(new SpinCannon(m,125,p1-375, -3));
		get(new JMan(m,125,p1-525));
		get(new Line(m,0, 375,p1-375, 500, p1-375));
		get(new JMan(m,475,p1-325));

		double p2 = p1 - 500;

		get(new JellyMom(m,250,p2));

		double r = 60;
		get(JMan.JManPlus(m,375,p2,r));
		get(JMan.JManPlus(m,375,p2-225,r));
		get(JMan.JManPlus(m,150,p2-225,r));

		m.CUR_LEVEL_HEIGHT = -(p2-400);

		return objs;
	}
}