import java.util.*;
import java.awt.*;

public class SpecialGraphics extends GameObject {
	final private double STARS_ANGLE_RANGE = Math.PI/3;
	final private double STARS_MAG_RANGE = 0.25; //AS A FRACTION OF IT
	final private double STARS_VY_PLUS = 2;
	final private int STARS_LAST_TIME = 80;
	final private int NUM_STAR_TYPES = 5;

	private java.util.List<GraphicsFX> fx;

	public SpecialGraphics(Main m) {
		super(m);
		fx = new ArrayList<GraphicsFX>();
	}

	public void compute() {
		for(int i=0; i<fx.size(); i++) {
			if(fx.get(i).isDead()) { fx.remove(i); i--; }
			else fx.get(i).compute();
		}
	}

	public void draw(Graphics2D g) {
		for(GraphicsFX gfx : fx)
			gfx.draw(g);
	}

	public void getScore(double x, double y, int score, double playerVX, double playerVY) {
		fx.add(new ScoreFX(main,x,y,score));

		//SPOUT SOME STARS!!!
		double mag = Math.sqrt(playerVX*playerVX+playerVY*playerVY);
		double angle = Math.atan2(playerVY,playerVX) + Math.PI; //Spout stars going in direction about opposite that in which player came.
		for(int i=0; i < score; i++) {
			double thisAngle = angle + (Math.random()-.5)*STARS_ANGLE_RANGE;
			double thisMag = mag + (Math.random()-.5)*STARS_MAG_RANGE;
			int starType = (int) (Math.random()*NUM_STAR_TYPES);
			fx.add(new ProjectileFX(main, Helpers.pathJoin("star","star"+starType), x,y,
											mag*Math.cos(thisAngle),mag*Math.sin(thisAngle)-STARS_VY_PLUS, STARS_LAST_TIME));
		}
	}

	public void getComboLost() {
		for(int i = 0; i < fx.size(); i++)
			if (fx.get(i) instanceof ScoreFX) { fx.remove(i); i--; }
	}

	public void getNewLevel() {
		fx = new ArrayList<GraphicsFX>();
	}
}