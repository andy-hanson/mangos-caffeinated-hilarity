import java.util.*;

public class Level4 extends LevelCreator {
	public List<GameObject> makeObjects(Main m) {
		m.backgrounder.getBackground(new TrianglesBackground(m));

		m.player = new Player(m,20,-80);
		get(m.player);
		get(new LevelBottom(m));

		double p1 = -380;

		get(new Line(m,0,	250,0,	250,p1));
		get(new Line(m,1,	125,-50,	50,-45));

		get(new Bonus(m,125,-75));
		get(new Bonus(m,100,-150));
		get(new Bonus(m,175,-200));

		get(new RotatingLine(m,200,-150,50,-6));

		get(new Bonus(m,150,-250));
		get(new Bonus(m,100,-300));
		get(new Bonus(m,50,-250));

		//Now go to left

		//								main rad ang	x1 y1			x2	y2	 			time dissapear offset
		get(new LinearMovingLine(m,50,30,		375,-180,	375,-380,		100,true));
		get(new LinearMovingLine(m,50,30,		375,-180,	375,-380,		100,true,50));
		get(new Bonus(m,300,-280));

		//Remember, p1=-380
		get(new Line(m,0,250,p1,500,p1-250));
		get(new JMan(m,375,p1-50));
		get(new JMan(m,125,p1-50));
		//Thingy moving upleft to downright
		double r = 50; double com = r/Math.sqrt(2); //the x-component of it
		get(new LinearMovingLine(m,r,45,		com,p1-250+com,	250-com,p1-com,	40,false));
		get(new LinearMovingLine(m,r,45,		250+com,p1-500+com,500-com,p1-250-com,40,false));
		get(new Line(m,0,0,p1-250,500,p1-250-500));

		get(new JMan(m,250,p1-100));
		get(new JMan(m,150,p1-200));
		get(new JMan(m,250,p1-200));
		get(new JMan(m,350,p1-200));
		get(new JMan(m,150,p1-300));
		get(new JMan(m,250,p1-300));
		get(new JMan(m,350,p1-300));
		get(new JMan(m,250,p1-400));



		double p2 = p1-500;
		get(new JMan(m,50,p2));
		get(new JMan(m,50,p2+100));
		double gap = 20;
		get(new Line(m,0, 0,p2-250,				125-gap, p2-125-gap));
		get(new Line(m,0, 125+gap,p2-125+gap,	250,p2));

		get(new RotatingLine(m,250,p2-150,100,-3));
		get(new Bonus(m,100,p2-250+10));
		get(new Bonus(m,400,p2-250+10));


		m.CUR_LEVEL_HEIGHT = -(p2-250);

		return objs;
	}
}