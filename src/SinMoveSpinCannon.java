public class SinMoveSpinCannon extends SpinCannon {
	protected double x1, y1, x2, y2;
	protected double cx, cy, xChange, yChange; //For easier computation
	int moveTime; //Time taken to go from p1 to p2
	int movingTime; //Time since I started moving away from p1 or p2.
	//Starts off in middle heading towards p1
	public SinMoveSpinCannon(
		Main m,
		double x1, double y1, double x2, double y2,
		int sinTime, double spinSpeed, double strength) {
		super(m,x1,y1,spinSpeed,strength); //Position will be written over by compute()
		cx = (x1+x2)/2; cy = (y1 + y2)/2; xChange = (x1-x2)/2; yChange = (y1-y2)/2;
		moveTime = sinTime; movingTime = 0;
	}

	public void compute() {
		super.compute();
		movingTime++;
		x = cx + xChange*Math.sin(2*Math.PI*movingTime/moveTime);
		y = cy + yChange*Math.sin(2*Math.PI*movingTime/moveTime);
	}
}