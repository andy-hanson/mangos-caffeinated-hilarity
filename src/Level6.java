import java.util.*;

public class Level6 extends LevelCreator {
	public List<GameObject> makeObjects(Main m) {
		m.backgrounder.getBackground(new PolarLightningBackground(m));

		m.player = new Player(m,250,-100);
		get(m.player);
		get(new LevelBottom(m));

		double k = 120;
		double r = 20;

		get(new EvilOrbFirer(m,150,20,-90,8.5,60));
		get(new EvilOrbFirer(m,350,20,-90,8, 60));

		get(new Line(m,0, 450,-1*k+r,		450,-1*k-r));
		double r2 = 100;
		get(new Line(m,0, 250,-2*k+r2,	250,-2*k-r2));
		get(new Line(m,0,  50,-3*k+r,		50,-3*k-r));
		get(new Line(m,0, 400+r,-4*k+r,	400-r,-4*k-r));
		r2 = 100;
		get(new Line(m,0, 400+r2,-5*k+r2,400-r2,-5*k-r2));
		get(new Bonus(m,400,-5*k));
		get(new Line(m,0, 150+r,-4*k+r,	150-r,-4*k-r));
		get(new Line(m,0, 150-r,-5*k+r,	150+r,-5*k-r));


		double p1 = -5*k;

		get(new Cannon(m,250,p1,-90,22));
		int n = 32;
		k = 100;
		double sinAmp = 100;
		double x,y;
		for (int i = 0; i < n; i++)
		{
			double sin = Math.sin(2*Math.PI * i/n);
			x = 250 - sinAmp * sin*Math.abs(sin); //Based on sin*|sin|
			y = p1 - (i+1)*k;
			get(new Bonus(m,x,y));
		}
		get(new Cannon(m,250,p1-16*k,-90,28)); //slightly harder second time

		double p2 = p1 - 32*k;

		k = 160;
		double canStr = 14, orbStr = 7; int orbFrec = 20;
		get(new SpinCannon(m,250,p2,3,canStr));
		get(new EvilOrbFirer(m,m.LEFT,p2-k,0,orbStr,orbFrec));
		get(new SpinCannon(m,400,p2-k,-4,canStr));
		get(new EvilOrbFirer(m,m.RIGHT,p2-2*k,180,orbStr,orbFrec));

		get(new SpinCannon(m,100,p2-2*k,5,canStr));
		get(new Line(m,0, 0,p2-3*k, 250, p2-3*k));
		get(new RotatingLine(m,450,p2-3*k,k/2,-2));
		get(new SpinCannon(m,100,p2-4*k,-6,canStr));

		double p3 = p2 - 6*k;

		k = 100;
		double rad = 60;
		int lineTime = 120;
		orbStr = 8; orbFrec = 60;
		//							main rad ang	x1 y1			x2	y2	 			time offset
		get(new SinMovingLine(m,rad,0,		rad,p3-0*k,	500-rad,p3-0*k,lineTime,0));
		get(new EvilOrbFirer(m,m.LEFT,p3,-20,orbStr,orbFrec));
		get(new SinMovingLine(m,rad,0,		rad,p3-1*k,	500-rad,p3-1*k,lineTime,lineTime/2));
		get(new EvilOrbFirer(m,m.RIGHT,p3-k,-180+25,orbStr,orbFrec));
		get(new SinMovingLine(m,rad,0,		rad,p3-1.5*k,	500-rad,p3-2.5*k,lineTime,0));
		get(new EvilOrbFirer(m,m.LEFT,p3-2*k,-30,orbStr,orbFrec));
		get(new SinMovingLine(m,rad,0,		rad,p3-3*k,	500-rad,p3-2*k,lineTime,lineTime/2));
		get(new EvilOrbFirer(m,m.LEFT,p3-2*k,-180+35,orbStr,orbFrec));

		m.CUR_LEVEL_HEIGHT = -(p3 - 4*k);

		return objs;
	}
}