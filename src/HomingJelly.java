public class HomingJelly extends Jelly
{
	/* Follows player! */
	final static private double SPEED_MULT = 0.8; //Travels at this much times the player's speed.
	double speed;
	AnimatedImage animatedL, animatedR; //Will change direction, so needs both of these.

	public HomingJelly(Main m, double x, double y, double mag) {
		super(m,x,y);
		animatedL = new AnimatedImage(main,Helpers.pathJoin("enemies","jellyL"),2,4);
		animatedR = new AnimatedImage(main,Helpers.pathJoin("enemies","jellyR"),2,4);
		animatedImage.dead = true; //Don't need it, have replaced it!
		speed = mag;
	}

	public void compute() {
		double ang = angleTo(main.player); double mag = main.player.speed()*SPEED_MULT;
		vx = mag*Math.cos(ang); vy = mag*Math.sin(ang);

		super.compute();

		if (vx <= 0) { animatedL.compute(); img = animatedL.getImage(); }
		else { animatedR.compute(); img = animatedR.getImage(); }
	}
}