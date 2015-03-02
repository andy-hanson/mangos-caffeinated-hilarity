abstract class MovingLine extends Line {
	public MovingLine(Main m, double rad, double ang) {
		super(m,1,0,0,rad,ang); //Centered at Origin (We'll move it anyway)
	}

	public void compute() {
		move();
		calcFromCenter();
	}

	protected abstract void move();
}