class HomingJellySpawner extends GameObject {
	//Used by challenge mode!
	private static final int TIME_BETWEEN_SHOTS = 80;
	private int timeUntilShoot;

	public HomingJellySpawner(Main m) {
		super(m);
		timeUntilShoot = TIME_BETWEEN_SHOTS;
	}

	public void compute() {
		timeUntilShoot--;
		if (timeUntilShoot <= 0) { main.getObject(new HomingJelly(main,250,0,0)); timeUntilShoot = TIME_BETWEEN_SHOTS; }
	}

	public void draw(java.awt.Graphics2D g) { /*pass*/ }
}