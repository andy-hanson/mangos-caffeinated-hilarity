import java.awt.*;

public abstract class RectObject extends GameObject {
	Image img;
	double x1,y1,w,h; //x1,y1 are topleft
	double cx, cy;
	double vx, vy;

	protected static boolean xWraps = true;

	public RectObject(Main m) { super(m); vx = vy = 0; }
	public RectObject(Main m, double ax, double ay, double width, double height) { super(m); w = width; h = height; setCenter(ax,ay); }
	public RectObject(Main m, double ax, double ay, double width, double height, String name) { this(m,ax,ay,width,height); img = main.helper.getImage(name);	}

	public void compute() {
		x1 += vx; y1 += vy;
		if (xWraps) {
			if (x1 > main.RIGHT) x1 -= main.PLAY_WIDTH; //Wrap x
			if (x1 < main.LEFT) x1 += main.PLAY_WIDTH;
		}
		calcCenter();
	}

	public void draw(Graphics2D g) {
		//g.drawRect(Helpers.round(x1),Helpers.round(y1),Helpers.round(w),Helpers.round(h));
		g.drawImage(img,Helpers.round(x1),Helpers.round(y1),main);
	}

	protected void calcCenter() {
		cx = x1 + w/2;
		cy = y1 + h/2;
	}

	public void setCenter(double x, double y) {
		cx = x; cy = y;
		x1 = cx-w/2; y1 = cy-h/2;
	}

	public void setCenter() {
		setCenter(cx,cy);
	}

	public double getTop() { return y1; }
	public double getLeft() { return x1; }
	public double getRight() { return x1+w; }
	public double getBottom() { return y1+h; }

	public void setVelPolar(double ang, double mag) {
		vx = mag*Math.cos(ang);
		vy = mag*Math.sin(ang);
	}

	public double angleTo(CircleObject c) {
		return Math.atan2(c.y-cy,c.x-cx);
	}
}