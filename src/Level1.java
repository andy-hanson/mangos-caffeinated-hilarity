import java.util.*;

public class Level1 extends LevelCreator {
	public List<GameObject> makeObjects(Main m) {
		m.backgrounder.getBackground(new CirclesBackground(m));

		double k = 80;
		m.player = new Player(m,m.WIDTH/2,-k+10);
		get(m.player);
		get(new LevelBottom(m));
		get(new Line(m,0,	m.LEFT,-1*k,	100,-1*k));
		get(new Line(m,0,	400,-1*k,		m.RIGHT,-1*k));
		get(new Line(m,0,	100,-2*k,		400,-2*k));
		get(new Line(m,0,	100,-2.5*k,		100,-4*k));
		get(new Line(m,0,	200,-3.5*k,		300,-4*k));
		get(new Line(m,0,	400,-4.5*k,		400,-6*k));
		get(new Line(m,0,	200,-7*k,		300,-5.5*k));

		get(new RotatingLine(m, 350,-7*k,	20)); //Out of the way but keeps combo going
		get(new RotatingLine(m,	100,-8*k,	80));
		get(new RotatingLine(m,	250,-9*k,	40));
		get(new RotatingLine(m,	400,-10*k,	80));

		//Short sequence of short, horizontal, even lines
		int n = 5;
		double minX = 40; double maxX = 380;
		double rad = 20;
		for(int i = 0; i < n; i++) {
			double y = -12*k + 1*k*i/(n-1); //Starts at -12k on left, goes 'down' to -11k
			double x = minX + (maxX-minX)*i/n;
			get(new Line(m,1,x,y,rad,0)); //ang=0
		}

		m.CUR_LEVEL_HEIGHT = 13*k;

		return objs;
	}
}