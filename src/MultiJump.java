class MultiJump extends CircleObject {
	public MultiJump(Main m, double x, double y) {
		super(m,x,y,40,"multiJump");
	}

	public void compute() {
		super.compute();
		if (main.player.collideCircle(this)) {
			dead = true;
			main.player.getMultiJump(this);
		}
	}
}