import java.util.*;
import java.awt.Graphics2D;

public class Backgrounder {
	Main main;
	private List<BackgroundGraphics> backgrounds;
	public Backgrounder(Main m) { main = m; backgrounds = new ArrayList<BackgroundGraphics>(); }
	public void compute() { for(BackgroundGraphics bg : backgrounds) bg.compute(); }
	public void drawBeforeYScroll(Graphics2D g) {
		for(BackgroundGraphics bg : backgrounds)
			if (!bg.YScrolling())
				bg.draw(g);
	}
	public void drawAfterYScroll(Graphics2D g) {
		for(BackgroundGraphics bg : backgrounds)
			if (bg.YScrolling())
				bg.draw(g);
	}
	public void getAction(double x, double y, int score, double playerVX, double playerVY) {
		for (BackgroundGraphics bg : backgrounds)
			bg.getAction(x,y,score,playerVX,playerVY);
	}
	public void getBackground(BackgroundGraphics bg) {
		backgrounds.add(bg);
	}
	public void reset() {
		backgrounds = new ArrayList<BackgroundGraphics>();
	}
}