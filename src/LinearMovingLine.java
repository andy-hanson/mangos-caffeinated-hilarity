class LinearMovingLine extends MovingLine {
	double cx1, cy1, cx2, cy2; //Points oscillated between
	int moveTime; //Time taken to go from c1 to c2
	int movingTime; //Time since I started moving away from c1 or c2.

	boolean disappear; //If true, goes from c1 to c2, then cycles again without spending any time moving back
	boolean goingForward; //Used only if not disappear. Tells whether going from c1 to c2

	public LinearMovingLine(
		Main m, double rad, double ang,
		double acx1, double acy1, double acx2, double acy2,
		int aMoveTime, boolean aDisappear, int timeOffset) {
		super(m,rad,ang);
		cx1 = acx1; cy1 = acy1; cx2 = acx2; cy2 = acy2;
		moveTime = aMoveTime;

		movingTime = timeOffset;
		goingForward = true;
		disappear = aDisappear;
	}
	public LinearMovingLine(
		Main m, double rad, double ang,
		double acx1, double acy1, double acx2, double acy2,
		int aMoveTime, boolean aDisappear) {
		this(m,rad,ang,acx1,acy1,acx2,acy2,aMoveTime,aDisappear, 0);
	}
	public LinearMovingLine(
		Main m, double rad, double ang,
		double acx1, double acy1, double acx2, double acy2,
		int aMoveTime) {
		this(m,rad,ang,acx1,acy1,acx2,acy2,aMoveTime, false);
	}

	protected void move() {
		if (disappear) {
			movingTime++;
			if (movingTime == moveTime)
				movingTime = 0;
		}
		else {
			if (goingForward) {
				movingTime++;
				if (movingTime == moveTime)
					goingForward = false;
			}
			else {
				movingTime--;
				if (movingTime == 0)
					goingForward = true;
			}
		}
		cx = cx1 + (cx2-cx1)*movingTime/moveTime;
		cy = cy1 + (cy2-cy1)*movingTime/moveTime;
	}
}