import java.util.*;

public class Level3 extends LevelCreator {
	public List<GameObject> makeObjects(Main m) {
		m.backgrounder.getBackground(new ParticleSpiralBackground(m));

		m.player = new Player(m,20,-80);
		get(m.player);
		get(new LevelBottom(m));

		double k = 80;
		get(new RotatingLine(m,250,-1*k,60,-2));
		get(new RotatingLine(m,250,-3*k,60,3));
		get(new RotatingLine(m,250,-5*k,60,-4));

		double p1 = -6*k;

		get(new RotatingLine(m,400,p1,20,-6));
		get(new RotatingLine(m,100,p1,20,6));
		//								main rad ang	x1 y1			x2	y2	 			time dissapear
		get(new LinearMovingLine(m,40,90,	m.RIGHT,p1-80,	m.LEFT,p1-80,	160,true));
		get(new LinearMovingLine(m,40,90,	m.LEFT,p1-160,	m.RIGHT,p1-160,	160,true));
		double p2 = p1 - 300;
		k = 90;

		get(new RotatingLine(m,375,p2+20,40,3));
		get(new RotatingLine(m,375,p2-60,40,-3));

		//interlude on left
		get(new Line(m,0, 0,p2+k, 250,p2-k));
		get(new Line(m,0,	250,p2-k, 250,p2-3*k));
		get(new Line(m,0,	250,p2-3*k, 0,p2-5*k)); //***
		get(new RotatingLine(m,125,p2-1*k,40,-3));
		get(new RotatingLine(m,125,p2-3*k,40,-3));
		get(new RotatingLine(m,25,p2-2*k,25,3));

		//Go back to right
		double ang = 90 + Math.atan2(2*k,250)*180/Math.PI;
		double yDiff = (450-50)* (2*k/250);
		//								main rad ang	x1 y1			x2	y2	 			time dissapear
		get(new LinearMovingLine(m,60,ang,		450,p2-3*k,	50,p2-3*k-yDiff,		120,true));
		get(new LinearMovingLine(m,60,ang,		450,p2-3*k,	50,p2-3*k-yDiff,		120,true,60));
		//Above drag player left

		//Warp right again!
		double p3 = p2-5*k;
		k = 75;
		get(new RotatingLine(m,400,p3-0*k,50,1));
		get(new RotatingLine(m,300,p3-1*k,50,-2));
		get(new RotatingLine(m,200,p3-2*k,50,3));
		get(new RotatingLine(m,100,p3-3*k,50,-4));
		get(new RotatingLine(m,400,p3-4*k,50,5));
		get(new RotatingLine(m,300,p3-5*k,50,-6));
		get(new RotatingLine(m,200,p3-6*k,50,-7));
		get(new RotatingLine(m,100,p3-7*k,50,-8));

		m.CUR_LEVEL_HEIGHT = -(p3 - 9*k);

		return objs;
	}
}