public class Jelly extends RectObject {
	AnimatedImage animatedImage;

	protected static boolean xWraps = false; //Dies when goes off side!

	public Jelly(Main m, double x, double y, double ang, double mag) {
		super(m,x,y,28,14); setVelPolar(ang,mag);
		String name = (Math.cos(ang)>0)? "jellyR" : "jellyL";
		animatedImage = new AnimatedImage(main,Helpers.pathJoin("enemies",name),2,4);
		m.getObject(animatedImage);
	}
	protected Jelly(Main m, double x, double y) { this(m,x,y,0,0); } //Protected becuase only subclasses have a use for this!

	public void compute() {
		//No super.compute() because we don't want x to wrap
		x1 += vx; y1 += vy; calcCenter();
		dead = getRight() < main.LEFT || getLeft() > main.RIGHT || getBottom() < -main.CUR_LEVEL_HEIGHT || getTop() > 0;
		if (main.player.collideRect(this)) {
			if (main.player.attacking) {
				main.helper.playSound("jellyKilled");
				dead = true;
				main.backgrounder.getAction(main.player.x,main.player.y, 1, main.player.vx,main.player.vy); //No actual score, but some animation
			}
			else main.player.getHurt();
		}
		img = animatedImage.getImage();
	}
}