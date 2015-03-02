import java.awt.*;
import java.applet.*;

abstract class CircleObject extends GameObject {
	Image img;
	double x,y, vx, vy;
	int rad;

	public CircleObject(Main m) {
		super(m);
		vx = vy = 0;
	}
	public CircleObject(Main m, double ax, double ay, int arad) {
		this(m);
		x = ax;
		y = ay;
		rad = arad;
	}
	public CircleObject(Main m, double ax, double ay, int arad, String name) {
		this(m,ax,ay,arad);
		img = main.helper.getImage(name);
	}
	public void compute() {
		x += vx; y += vy;
		if (x > main.RIGHT)
			x -= main.PLAY_WIDTH; //Wrap x
		if (x < main.LEFT)
			x += main.PLAY_WIDTH;
	}
	public void draw(Graphics2D g) {
		g.drawImage(img,Helpers.round(getLeft()),Helpers.round(getTop()),main);
	}
	protected void drawFilled(Graphics2D g) {
		g.fillOval(
			Helpers.round(getLeft()),
			Helpers.round(getTop()),
			Helpers.round(getWidth()),
			Helpers.round(getHeight()));
	}
	protected void drawOutline(Graphics2D g) {
		g.drawOval(
			Helpers.round(getLeft()),
			Helpers.round(getTop()),
			Helpers.round(getWidth()),
			Helpers.round(getHeight()));
	}

	public double getTop() { return y - rad; }
	public void setTop(double top) { y = top + rad; }
	public double getLeft() { return x - rad; }
	public void setLeft(double left) { x = left + rad; }
	public double getRight() { return x + rad; }
	public void setRight(double right) { x = right - rad; }
	public double getBottom() { return y + rad; }
	public void setBottom(double bot) { y = bot - rad; }
	public double getWidth() { return 2*rad; }
	public double getHeight() { return 2*rad; } //No set() because width=height always

	public boolean collideCircle(CircleObject c) {
   		return (x-c.x)*(x-c.x) + (y-c.y)*(y-c.y) <= (rad+c.rad)*(rad+c.rad);
	}

	public class LineCollisionData
	{
		boolean collided; boolean above;
		public LineCollisionData(boolean col, boolean ab) { collided = col; above = ab; }
		/*Only use below if collided=false!*/
		public LineCollisionData(boolean col) { collided = col; above = false; }
	}

	public LineCollisionData collideLine(Line l)
	{
		/*
		We will determine the distance from the circle to the line.
		If that distance if less than or equal to the circle's radius, then the circle is touching the line.
		Now we will determine an mx+b equation for both the line and a line perpendicular to this line which passes through player's center.
		*/

		double X_TOLERANCE = 0.01;
		if (Math.abs(l.x2 - l.x1) < X_TOLERANCE) { //If it's vertical or close to it:
			//Special case, because m in y=mx+b is infinite.
			double dist = Math.abs(x - l.x1);
			boolean touchesSegment = Math.min(l.y1,l.y2) <= y && y <= Math.max(l.y1,l.y2);
			if (dist <= rad+1 && touchesSegment)
			{
				boolean above = x < l.x1;
				return new LineCollisionData(true,above);
			}
			return new LineCollisionData(false);
		}

		//Otherwise m can be found, so compute that way.
		//Line 1: the line, represented as y=mx+b
		double m1 = Math.tan(l.angle);
		double b1 = l.y1 - m1*l.x1;
		//Line 2: Passing perpendicularly through the line and through me
		double m2 = Math.tan(l.angle+Math.PI/2);
		double b2 = y - m2*x; //y = mx + b; know m,x,y

		//Find the point of intersection of these 2 lines, which is the closest point on the line to me.
		double intersectX = (b2-b1)/(m1-m2); //Thanks to some algebra
		double intersectY = m1*intersectX + b1;
		double dist = Math.sqrt((intersectX-x)*(intersectX-x) + (intersectY-y)*(intersectY-y));

		//If dist<rad collide with infinite line, but we only collide with this line if we're touching the SEGMENT.
		double lesserX = Math.min(l.x1,l.x2);
		double greaterX = Math.max(l.x1,l.x2);
		double lesserY = Math.min(l.y1,l.y2);
		double greaterY = Math.max(l.y1,l.y2);
		boolean touchesSegment = (lesserX <= intersectX && intersectX <= greaterX)
										&& (lesserY <= intersectY && intersectY <= greaterY);

		if (dist <= rad && touchesSegment) {
			boolean above = y < intersectY; //If it's above me
			return new LineCollisionData(true,above);
		}
		return new LineCollisionData(false);
	}

	boolean collideRect(RectObject ro) {
		return (x+rad>ro.getLeft() && x-rad<ro.getRight()) && (y+rad>ro.getTop() && y-rad<ro.getBottom()); //Imperfect but very fast
	}

	public void setVelPolar(double ang, double mag) {
		vx = mag*Math.cos(ang);
		vy = mag*Math.sin(ang);
	}

	public double getAngleTo(CircleObject c) {
		return Math.atan2(c.y-y,c.x-x);
	}
	public double getDistanceTo(CircleObject c) {
		return Math.sqrt((c.x-x)*(c.x-x)+(c.y-y)+(c.y-y));
	}
	public double speed() {
		return Math.sqrt(vx*vx+vy*vy);
	}
}