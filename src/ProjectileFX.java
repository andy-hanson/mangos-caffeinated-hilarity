import java.awt.*;

public class ProjectileFX extends RectObject implements GraphicsFX
{
	final double GRAV = 0.22;
	int lastTime, timeLeft;
	public ProjectileFX(Main m, String name, double ax, double ay, double iVX, double iVY, int aLastTime) {
		super(m);
		img = main.helper.getImage(name);
		w = img.getWidth(main); h = img.getHeight(main);
		setCenter(ax,ay);
		vx = iVX; vy = iVY;
		lastTime = aLastTime; timeLeft = lastTime;
	}

	public void compute() {
		super.compute();
		vy += GRAV;
		timeLeft--;
	}

	public void draw(Graphics2D g) {
		float alpha = ((float)timeLeft)/lastTime; //Must be float to fit into AlphaComposite.getInstance
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
		super.draw(g);
		g.setPaintMode(); //Return to normal mode
	}

	public boolean isDead() {
		return timeLeft <= 0;
	}
}