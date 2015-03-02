import java.awt.Image;

class JellyMom extends CircleObject {
	private static final int NORM_TIME_BETWEEN_SHOTS = 80;
	private static final int NORM_STRENGTH = 3;
	private static final int MAX_RECOVER_TIME = 120;
	private int recoverTime;
	private int shootTime, timeTillShoot;
	protected double strength;
	// Only fires if player inside this range. By default, range is infinite (value 1)
	private double minFireY, maxFireY;

	Image normImg, recoverImg;

	public JellyMom(Main m, double x, double y, int timeBetweenShots, double str) {
		super(m,x,y,32);
		timeTillShoot = shootTime = timeBetweenShots; strength = str;
		recoverTime = 0;

		normImg = main.helper.getImage(Helpers.pathJoin("enemies", "jellyMom"));
		recoverImg = main.helper.getImage(Helpers.pathJoin("enemies", "jellyMomRecovering"));
		img = normImg;

		minFireY = maxFireY = 1; //1 is the 'null' value. Can change with setShootRange()
	}

	public JellyMom(Main m, double x, double y, int timeBetweenShots) {
		this(m,x,y,timeBetweenShots,NORM_STRENGTH);
	}
	public JellyMom(Main m, double x, double y) {
		this(m,x,y,NORM_TIME_BETWEEN_SHOTS);
	}

	public void compute() {
		super.compute();

		if (main.player.collideCircle(this)) {
			if (main.player.attacking) {
				recoverTime = MAX_RECOVER_TIME;
			}
			else if (recoverTime == 0)
				main.player.getHurt();
		}

		if (recoverTime > 0) {
			recoverTime--;
			img = recoverImg;
		}
		else {
			//Try to shoot
			if ((minFireY == 1 || main.player.y > minFireY) && (maxFireY == 1 || main.player.y < maxFireY)) {
				timeTillShoot--;
				if (timeTillShoot == 0) {
					timeTillShoot = shootTime;
					shoot();
				}
			}
			img = normImg;
		}
	}

	public void shoot() {
		double ang = getAngleTo(main.player);
		main.getObject(new Jelly(main,x,y,ang,strength));
	}

	public void setFireRange(double min, double max) {
		/* DO NOT INPUT 1 as either range marker!!! */
		minFireY = min; maxFireY = max;
	}
}