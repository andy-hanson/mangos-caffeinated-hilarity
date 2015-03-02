import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.image.*;
import java.util.*;

class Line extends GameObject {
	public double x1,y1,x2,y2;
	public double length, angle, cx, cy; //Don't change these except to correct after changing x1,y1,x2,y2
	public double width;
	protected Image unRotImg;
	protected BufferedImage img;

	public boolean beenHit;

	public Line(Main m) { super(m); } //Don't use this. Allows MovingLine to compile.

	public Line(Main m, int initType, double A, double B, double C, double D)
	{
		super(m);

		switch(initType)
		{
			case 0: {
				//A,B,C,D = x1,y1,x2,y2
				x1 = A; y1 = B;
				x2 = C; y2 = D;
				if (x1 > x2) { double temp = x1; x1 = x2; x2 = temp; temp = y1; y1 = y2; y2 = temp; }
				if (x1 == x2) { if (y1 < y2) { double temp = y1; y1 = y2; y2 = temp; }
										if (y1 == y2) { System.out.println("ERROR: SIZE 0 LINE: ("+x1+","+y1+"), ("+x2+","+y2+")"); System.exit(1); } }
				length = Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
				angle = Math.atan2(y2-y1,x2-x1); fixAngle(); //Angle of p1 pointing to p2, fixed so in correct range
				break;
			}
			case 1: {
				//A,B,C,D = cx,cy,rad,ang. ANGLE IN DEGREES!
				length = C*2; angle = D*Math.PI/180; fixAngle();
				x1 = A-C*Math.cos(angle); x2 = A+C*Math.cos(angle);
				y1 = B-C*Math.sin(angle); y2 = B+C*Math.sin(angle);
				break;
			}
			default: {
				System.out.println("Bad initType for Line");
				break;
			}
		}
		width = 12;

		getUnRotImage("bumper");
		getImg();
	}

	protected void getUnRotImage(String type)
	{
		// Type is "bumper" or "dull"
		String unRotName = type + " length " + length;

		if (main.helper.hasImage(unRotName))
			unRotImg = main.helper.getImage(unRotName);

		else {
			Image mainImage, capRImage, capLImage; //Used temporarily when synthesizing the image.

			mainImage = main.helper.getImage(Helpers.pathJoin("line",type));
			capLImage = main.helper.getImage(Helpers.pathJoin("line",type + "CapL"));
			capRImage = main.helper.getImage(Helpers.pathJoin("line",type + "CapR"));

			unRotImg = new BufferedImage(Helpers.round(length + width*2),Helpers.round(width*2),BufferedImage.TYPE_INT_ARGB);
			Graphics g = unRotImg.getGraphics();

			g.drawImage(capLImage,0,0,main); //Draw capImage flipped on the left side.
			//Stretch mainImage over the whole length
			g.drawImage(main.helper.tileImage(mainImage,Helpers.round(length),Helpers.round(width*2)),Helpers.round(width),0,main);
			g.drawImage(capRImage,Helpers.round(width+length),0,main);//,round(length+width*2),round(width*2),main); //Put a capImage on the right (no flip needed)

			main.helper.storeImage(unRotName, unRotImg);
		}
	}

	protected void getImg()
	{
		//Pre: has called getUnRotImage()
		double w = length + width*2, h = width*2;
		//Create img with expected size after rotation
		img = new BufferedImage(
			Helpers.round(w*Math.abs(Math.cos(angle))+h*Math.abs(Math.sin(angle))),
			Helpers.round(w*Math.abs(Math.sin(angle))+h*Math.abs(Math.cos(angle))),
			BufferedImage.TYPE_INT_ARGB);
		Graphics2D rg = img.createGraphics();

		rg.translate(img.getWidth()/2,img.getHeight()/2); //New origin is at center
		rg.rotate(angle);
		rg.drawImage(unRotImg,-Helpers.round(w/2),Helpers.round(-h/2),main);
		/*rg.setColor(Color.GREEN);
		/rg.drawLine(-10,0,10,0);
		/rg.drawLine(0,-10,0,10);*/
	}

	public void compute()
	{
		//Do nothing. Overriden a lot.
	}

	public void getHit()
	{
		beenHit=true;
		getUnRotImage("dull");
		getImg();
	}

	public void draw(Graphics2D g)
	{
		int blitX, blitY;
		if (angle > 0) {
			blitX = Helpers.round(x1 - width*Math.sin(angle) - width*Math.cos(angle));
			blitY = Helpers.round(y1 - width*Math.cos(angle) - width*Math.sin(angle));
		}
		else {
			blitX = Helpers.round(x1 - width*Math.sin(-angle) - width*Math.cos(-angle));
			blitY = Helpers.round(y1 - width*Math.cos(-angle) - length*Math.sin(-angle) - width*Math.sin(-angle));
		}
		g.drawImage(img,blitX,blitY,main);

		//To make sure is right:
		//g.setColor(Color.BLACK);
		//g.drawLine(Helpers.round(x1),Helpers.round(y1),Helpers.round(x2),Helpers.round(y2));
	}

	protected void fixAngle()
	{
		while (angle >= Math.PI/2)
			angle -= Math.PI;
		while (angle < -Math.PI/2)
			angle += Math.PI;
	}

	protected void calcFromCenter()
	{
		//Calculate x1,y1,x2,y2 based on cx,cy,length,angle
		double rad = length/2;
		x1 = cx - rad*Math.cos(angle); x2 = cx + rad*Math.cos(angle);
		y1 = cy - rad*Math.sin(angle); y2 = cy + rad*Math.sin(angle);
	}

	protected void setCenter(double acx, double acy)
	{
		cx = acx; cy = acy;
		calcFromCenter();
	}




	public static List<Line> lineSequence(Main m, List<Double> xs, List<Double> ys)
	{
		if (xs.size() != ys.size()) { System.out.println("Error in Line.lineSequence"); System.exit(1); }
		List<Line> l = new ArrayList<Line>(xs.size());
		for (int i = 0; i < xs.size()-1; i++)
			l.add(new Line(m,0, xs.get(i),ys.get(i), xs.get(i+1),ys.get(i+1)));
		return l;
	}
}