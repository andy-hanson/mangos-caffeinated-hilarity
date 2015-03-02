import java.awt.*;
import java.applet.*;
import java.net.*;
import java.io.*;

public class Helpers {
	public static String pathJoin(String p1, String p2) {
 		File f1 = new File(p1);
		File f2 = new File(f1, p2);
		return f2.getPath();
	}

	public static String pathJoin(String p1, String p2, String p3) {
		return pathJoin(pathJoin(p1,p2),p3);
	}

	public static void drawStringRightAligned(Graphics g, String s, double x, double y) {
		//Pre: Is MONOSPACED!
		double drawX = x - s.length()*g.getFont().getSize() * 19/32;
		g.drawString(s,Helpers.round(drawX),Helpers.round(y));
	}

	public static void drawStringCentered(Graphics g, String s, double x, double y) {
		double drawX = x - s.length()*g.getFont().getSize() * 19/64;
		g.drawString(s,Helpers.round(drawX),Helpers.round(y));
	}

	public static int round(double d) {
		return (int) Math.round(d);
	}

	public static class HueCycler extends GameObject {
		int cycleFrames; //# frames per cycle
		int H, S,V,A;
		public HueCycler(Main m, int cycFrames, int aS, int aV, int aA) {
			super(m);
			cycleFrames = cycFrames;
			//ccop = new ColorConvertOp(new ColorSpace(ColorSpace.HSV,4), new ColorSpace(ColorSpace.RGB,4), 0);
			H = 0; S = aS; V = aV; A = aA;
		}
		public HueCycler(Main m, int cycFrames, int aS, int aV) { this(m,cycFrames,aS,aV,255); }
		public HueCycler(Main m, int cycFrames) { this(m,cycFrames,100,100); }
		public void compute() { H += 360/cycleFrames; if (H>=360) H -= 360; }
		public Color getColor() { return HSVtoRGB(H,S,V,A); }
		public void draw(Graphics2D g) { /*pass*/ }

	}

	public static Color HSVtoRGB(double H, double S, double V, int A) {
		double F, P, Q, T;
		double R,G,B;

		//Everything is done scaled from 0-1
		V /= 100; S /= 100;

		if (S == 0)
			R = G = B = V;
		else {
			H /= 60; //Now in range 0-5
			int i = (int) Math.floor(H);
			F = H - i; //remaining part left over, 0-1
			P = V*(1 - S);
			Q = V*(1 - S*F);
			T = V*(1 - S*(1 - F));
			switch(i) {
				case 0: R = V; G = T; B = P; break;
				case 1: R = Q; G = V; B = P; break;
				case 2: R = P; G = V; B = T; break;
				case 3: R = P; G = Q; B = V; break;
				case 4: R = T; G = P; B = V; break;
				case 5: R = V; G = P; B = Q; break;
				default: System.out.println("I oops'ed in Helpers.HSVtoRGB with i="+i+"'cuz H="+H); R=G=B=0; break;
			}
		}
		try {
			return new Color(round(R*255),round(G*255),round(B*255),A);
		}
		catch (IllegalArgumentException e) {
			System.out.println(R + " " + G + " " + B + " ");
			return Color.WHITE;
		}
	}

	public static Color randHSV(double S, double V) {
		return HSVtoRGB(Math.random()*359,S,V);
	}
	public static Color randHSV() {
		return randHSV(100,100);
	}
	public static Color HSVtoRGB(double H, double S, double V) {
		return HSVtoRGB(H,S,V,255);
	}

	public static int slowGrowFunction(int x) {
		//x = 0	1	2	3	4	5	6	7	8
		//y = 0	1	2	2	3	3	3	4	4
		int y = 0; int counter = 0;
		for(int i=0; i<x; i++) {
			counter++;
			if (counter >= y) { counter = 0; y++; }
		}
		return y;
	}
	public static int fastGrowFunction(int x) {
		//x = 0  1	2	3	4	5	6	7	8
		//y = 0	1	3	6	10	15	21	28	36
		int y = 0;
		for(int i=1; i<=x; i++)
			y += i;
		return y;
	}

	public static double nearestTenth(double d) {
		return Math.round(d*10)/10;
	}
	public static int arraySum(int[] a) {
		int sum = 0; for (int b : a) sum += b; return sum;
	}
	public static double arraySum(double[] a) {
		double sum = 0; for (double b : a) sum += b; return sum;
	}
}