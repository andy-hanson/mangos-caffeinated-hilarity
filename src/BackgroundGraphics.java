import java.awt.Graphics2D;

public abstract class BackgroundGraphics
{
	Main main;
	public abstract boolean YScrolling();
	public BackgroundGraphics(Main m) {
		main = m;
	}
	public abstract void compute();
	public abstract void draw(Graphics2D g);
	public abstract void getAction(double x, double y, int score, double playerVX, double playerVY);
}