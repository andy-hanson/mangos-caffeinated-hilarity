import java.awt.Graphics2D;

public class EvilOrb extends CircleObject {
	private static final double GRAV = 0.08;
	private static final int COLLIDE_RAD = 10, DRAW_RAD = 20;
	AnimatedImage animatedImage;
	public EvilOrb(Main m, double x, double y, double ang, double mag) {
		super(m,x,y,COLLIDE_RAD); setVelPolar(ang,mag);
		animatedImage = new AnimatedImage(main,"evilOrb",4,3,true);
		main.getObject(animatedImage);
	}

	public void compute() {
		//Do NOT call super.comput(), we do not want x to wrap
		x += vx; y += vy;
		vy += GRAV;
		dead = getRight() < main.LEFT || getLeft() > main.RIGHT || getBottom() < -main.CUR_LEVEL_HEIGHT
					|| (getTop() > 0 && vy > 0); //Can't die of being too low if are moving up!
		if (main.player.collideCircle(this))
			main.player.getHurt();
		img = animatedImage.getImage();
	}

	public void draw(Graphics2D g) {
		//Has a bigger radius for drawing than for collision
		g.drawImage(img,Helpers.round(x-DRAW_RAD),Helpers.round(y-DRAW_RAD),main);
	}
}