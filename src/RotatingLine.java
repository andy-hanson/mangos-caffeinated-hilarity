class RotatingLine extends Line {
	double spin;
	public RotatingLine(Main m, double acx, double acy, double rad, double aspin, double startAngle) {
		//SPIN IN DEGREES/FRAME!
		super(m,1,acx,acy,rad,startAngle);
		spin = aspin*Math.PI/180;

		cx = (x1+x2)/2; cy = (y1+y2)/2;
		angle = Math.atan2(y2-y1,x2-x1);
	}
	public RotatingLine(Main m, double acx, double acy, double rad, double aspin) {
		this(m,acx,acy,rad,aspin,0);
	}
	public RotatingLine(Main m, double acx, double acy, double rad) {
		this(m,acx,acy,rad,-1);
	}
	public RotatingLine(Main m, double acx, double acy) {
		this(m,acx,acy,50);
	}

	public void compute() {
		angle += spin;
		fixAngle();
		calcFromCenter();
		getImg();
	}
}