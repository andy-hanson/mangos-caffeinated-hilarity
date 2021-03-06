class SinMovingLine extends MovingLine {
	double cx1, cy1, cx2, cy2; //Points oscillated between
	int moveTime; //Time taken to go from c1 to c2
	int movingTime; //Time since I started moving away from c1 or c2.

	//If timeOffset = 0, starts in center moving towards point 1.
	public SinMovingLine(
		Main m, double rad, double ang,
		double acx1, double acy1, double acx2, double acy2,
		int aMoveTime, int timeOffset) {
		super(m,rad,ang);
		cx1 = acx1; cy1 = acy1; cx2 = acx2; cy2 = acy2;
		moveTime = aMoveTime;

		movingTime = timeOffset;
	}
	public SinMovingLine(
		Main m, double rad, double ang,
		double acx1, double acy1, double acx2, double acy2,
		int aMoveTime) {
		this(m,rad,ang,acx1,acy1,acx2,acy2,aMoveTime, 0);
	}
	public SinMovingLine(Main m) {
		super(m,100,0);
	}

	protected void move() {
		movingTime++; if (movingTime == moveTime) movingTime = 0;
		cx = (cx1+cx2)/2 + (cx1-cx2)/2 * Math.sin(2*Math.PI*movingTime/moveTime);
		cy = (cy1+cy2)/2 + (cy1-cy2)/2 * Math.sin(2*Math.PI*movingTime/moveTime);
	}
}