public class SpinJellyMom extends JellyMom
{
	double ang, spin;
	public SpinJellyMom(
		Main m, double x, double y,
		double angChange, int timeBetweenShots, int str) {
		//ANGCHANGE IN DEGREES/FRAME!
		super(m,x,y,timeBetweenShots,str);
		ang = 0; spin = angChange * Math.PI/180;
	}

	public void compute() {
		super.compute();
		ang += spin;
	}

	public void shoot() {
		main.getObject(new Jelly(main,x,y,ang,strength));
	}
}