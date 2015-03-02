import java.awt.Graphics2D; import java.awt.Image;
import java.awt.geom.AffineTransform;

public class Cannon extends CircleObject {
	final private static int IMAGE_WIDTH = 44;
	final private static int IMAGE_HEIGHT = 32;
	final protected static double NORM_STRENGTH = 12;

	//Flashes when player inside it and has a hot image for a short time after firing
	private Image normImg, firingImg, flashImg;
	private boolean playerInside, flashOn;
	private final int MAX_FIRE_ANIMATE_TIME=4;
	private int fireAnimateTime;

	protected double strength, dir; public boolean used;

	public Cannon(Main m, double x, double y, double ang, double str) {
		//ANG IN DEGREES!
		super(m,x,y,16);

		normImg = main.helper.getImage("cannon norm");
		firingImg = main.helper.getImage("cannon firing"); //For after fire
		flashImg = main.helper.getImage("cannon flash"); //For when player inside me

		dir = ang*Math.PI/180;
		strength = str;
		used = false;

		playerInside = false; fireAnimateTime = 0;
		flashOn = false; //Turned on and off rapidly
	}
	public Cannon(Main m, double x, double y, double ang) {
		this(m,x,y,ang,NORM_STRENGTH);
	}
	public Cannon(Main m, double x, double y) {
		this(m,x,y,0);
	}

	public void compute() {
		if (used) img = main.helper.getImage("cannon used");
		else if (playerInside) { flashOn = !flashOn; img = flashOn ? flashImg : normImg; }
		else if (fireAnimateTime>0) { img = firingImg; fireAnimateTime--; }
		else img = normImg;
	}

	public void catchPlayer(Player p) {
		playerInside = true;
	}

	public void shootPlayer(Player p) {
		p.vx = strength*Math.cos(dir); p.vy = strength*Math.sin(dir);
		if (!used) { p.status.getCannonFiredFirstTime(); used = true; }
		else { p.status.getCannonFire(); }

		fireAnimateTime = MAX_FIRE_ANIMATE_TIME; playerInside = false;
	}

	public void draw(Graphics2D g) { //The beak pokes out, so we need a special draw function.
		AffineTransform prevTransform = g.getTransform();

		g.translate(x,y);
		g.rotate(dir);
		g.drawImage(img,-rad,-rad,main); //though IMAGE_WIDTH/2 > rad so beak sticks out

		g.setTransform(prevTransform);
	}
}