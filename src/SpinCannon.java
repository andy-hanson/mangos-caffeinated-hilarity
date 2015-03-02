public class SpinCannon extends Cannon
{
	protected double spin;
	public SpinCannon(Main m, double x, double y, double spinSpeed, double str) {
		//SPINSPEED IN DEGREES/FRAME!
		super(m,x,y,0,str);
		spin = spinSpeed*Math.PI/180;
	}
	public SpinCannon(Main m, double x, double y, double spinSpeed) {
		this(m,x,y,spinSpeed,NORM_STRENGTH);
	}

	public void compute() {
		super.compute();
		dir += spin;
	}
}