import java.awt.Graphics2D;

public class EvilOrbFirer extends GameObject {
	protected double x, y, angle, strength;
	public final static double DEFAULT_ANGLE = -Math.PI/2; //Up
	public final static double NORM_STRENGTH = 8;
	public final static int NORM_FREC = 60;
	protected int juggleWaitTime, timeUntilJuggle;

	public EvilOrbFirer(Main m, double ax, double ay, double fireAngle, double fireSpeed, int frequency) {
		//fireAngle in DEGREES!
		super(m); x = ax; y = ay;
		timeUntilJuggle = juggleWaitTime = frequency;
		angle = fireAngle * Math.PI/180;
		strength = fireSpeed;
	}
	public EvilOrbFirer(Main m, double ax, double ay, double fireAngle, double fireSpeed) {
		this(m,ax,ay,fireAngle,fireSpeed,NORM_FREC);
	}
	public EvilOrbFirer(Main m, double ax, double ay, double fireAngle) {
		this(m,ax,ay,fireAngle,NORM_STRENGTH);
	}
	public EvilOrbFirer(Main m, double ax, double ay) {
		this(m,ax,ay,DEFAULT_ANGLE);
	}

	public void compute() {
		timeUntilJuggle--;
		if (timeUntilJuggle <= 0) {
			main.getObject(new EvilOrb(main,x,y,angle,strength));
			timeUntilJuggle = juggleWaitTime;
		}
	}

	public void draw(Graphics2D g) { /*pass*/ }
}