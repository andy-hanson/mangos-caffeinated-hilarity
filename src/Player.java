import java.util.ArrayList;
import java.awt.*;

class Player extends CircleObject {
	private final double CHEAT_ACC = 0.5; //Can cheat by pressing Select to make vy exactly this much

	private final double LOW_GRAV = .11, NORM_GRAV = 0.22, HIGH_GRAV = 0.44;
	private double grav;
	//final double LOW_BOUNCE = 2, NORM_BOUNCE = 3.5, HIGH_BOUNCE = 5; //Vels when getting bounced
	private final double BOUNCE_PERP = 5.5; //Bouncing perpendicular to bumper
	private final double BOUNCE_UP = 4.5; //Also bounce a bit straight up
	private final double HORIZ_ACCEL = .22;
	private final double HORIZ_RESIST = 0.97; //Slows it down by a factor each frame
	private final double MAX_VY = 8; //In either direction

	private final double JMAN_BOUNCE = 6.0;

	private final int INVINCIBLE_TIME = 60;
	private final int INVINCIBLE_ANIMATE_TIME = 4; //Number of frames per flicker (either on or off, not for both)
	int invincibleTimeLeft;
	public boolean invincible; //Might be turned true by something other than mercy invincibility

	private final int MAX_ATTACK_TIME = 24;
	private final int MAX_NON_ATTACK_TIME = 24;
	private int attackTime, nonAttackTime;
	private final int ATTACK_HUECIRCLE_ALPHA = 128;
	private Helpers.HueCycler attackHueCycler;
	public boolean attacking;

	// Air jump is the normal ability to jump a second and third time.
	private final int MAX_AIR_JUMPS = 2;
	private int airJumpsLeft;
	private int AIR_JUMP = 5; //Velocity after jumping in the air

	// For multi jump you must attain a MultiJump item.
	private boolean multiJump;
	private final int MULTI_JUMP_AMOUNT = 5;
	private final int MAX_MULTI_JUMP_RECOVER_TIME = 8;
	private int multiJumpRecoverTime = 0;

	private Cannon insideCannon;
	private final int MAX_CANNON_RECOVER_TIME = 8;
	private int cannonRecoverTime;

	//Error prevention: can't hit a line within 6 frames of hitting a line
	private final int MAX_TIME_SINCE_LINE = 6;
	private int timeSinceLine;

	PlayerStatus status;

	public Image normImg, upImg, downImg, attackImg, jumpImg, hurtImg;
	private final int MAX_TIME_SINCE_JUMP = 12; //Frames that jump image is displayed
	private int timeSinceJump;

	public Player(Main m, double ax, double ay) {
		super(m,ax,ay,8);

		normImg = m.helper.getImage(Helpers.pathJoin("player","normal")).getScaledInstance(rad*2,rad*2,Image.SCALE_SMOOTH);
		upImg = m.helper.getImage(Helpers.pathJoin("player","up")).getScaledInstance(rad*2,rad*2,Image.SCALE_SMOOTH);
		downImg = m.helper.getImage(Helpers.pathJoin("player","down")).getScaledInstance(rad*2,rad*2,Image.SCALE_SMOOTH);
		attackImg = m.helper.getImage(Helpers.pathJoin("player","attack")).getScaledInstance(rad*2,rad*2,Image.SCALE_SMOOTH);
		jumpImg = m.helper.getImage(Helpers.pathJoin("player","jump")).getScaledInstance(rad*2,rad*2,Image.SCALE_SMOOTH);
		hurtImg = m.helper.getImage(Helpers.pathJoin("player","hurt")).getScaledInstance(rad*2,rad*2,Image.SCALE_SMOOTH);

		attackHueCycler = new Helpers.HueCycler(main,MAX_ATTACK_TIME,100,100,ATTACK_HUECIRCLE_ALPHA);

		status = main.playerStatus;
		status.getPlayer(this);

		invincible = false;
		invincibleTimeLeft = 0;
		airJumpsLeft = 0;
		timeSinceJump = 0;
		insideCannon = null;
		cannonRecoverTime = 0;
		multiJump = false;
	}
	public Player(Main m) { this(m,10,-100); }

	public void compute() {
		super.compute();

		//CONTROLS AND ANIMATION
		if (main.controller.up) {
			grav = LOW_GRAV;
			img = upImg;
		}
		else if (main.controller.down) {
			grav = HIGH_GRAV;
			img = downImg;
		}
		else {
			grav = NORM_GRAV;
			img = normImg;
		}
		vy += grav;
		if (attackTime>0) {
			attacking = true;
			attackTime--;
			if (attackTime==0)
				nonAttackTime = MAX_NON_ATTACK_TIME;
			img = attackImg;
			attackHueCycler.compute();
		}
		else {
			attacking = false;
			if (nonAttackTime>0)
				nonAttackTime--;
			else if (main.controller.B) {
				//Attack!
				main.helper.playSound("attack");
				attacking = true;
				attackTime = MAX_ATTACK_TIME;
			}
		}
		if (invincible)
			img = hurtImg;
		if (insideCannon != null)
			img = null; //Don't display if inside a cannon!

		//AIR JUMP!
		//Can't airjump if just bounced off line or just shot out of cannon.
		//OK if just hit JMan since that does not require Z button and there's no problem going through one.
		if (multiJump) {
			if (multiJumpRecoverTime > 0)
				multiJumpRecoverTime--;
			else if (main.controller.A && timeSinceLine == 0 && cannonRecoverTime == 0) {
				main.helper.playSound("airJump");
				vy = -MULTI_JUMP_AMOUNT;
				multiJumpRecoverTime = MAX_MULTI_JUMP_RECOVER_TIME;
			}
		}
		else {
			if (main.controller.A && timeSinceLine == 0 && cannonRecoverTime == 0 && timeSinceJump == 0 && airJumpsLeft > 0) {
				main.helper.playSound("airJump");
				vy = -AIR_JUMP;
				airJumpsLeft--;
				timeSinceJump = MAX_TIME_SINCE_JUMP;
			}
			if (timeSinceJump > 0) {
				timeSinceJump--;
				if (!attacking)
					img = jumpImg;
			}
		}

		//Invincibility
		if (invincibleTimeLeft>0) {
			//Count down invincibility
			invincible = true;
			invincibleTimeLeft--;
		}
		else invincible = false;

		//Horizontal and 'cheat' movement
		if (main.controller.left)
			vx -= HORIZ_ACCEL;
		if (main.controller.right)
			vx += HORIZ_ACCEL; //If both, then they cancel.
		if (main.CHEAT_ON && main.controller.Select) {
			vy -= CHEAT_ACC;
			invincible = true;
		}

		//Movement restrictions
		if (vy > MAX_VY)
			vy = MAX_VY;
		if (vy < -MAX_VY)
			vy = -MAX_VY;
		vx *= HORIZ_RESIST;

		checkForLines();
		if (cannonRecoverTime > 0)
			cannonRecoverTime--;
		if (insideCannon == null && cannonRecoverTime == 0)
			checkForCannons();

		if (insideCannon != null) {
			vx = vy = 0; x = insideCannon.x; y = insideCannon.y;
			if (main.controller.A) {
				//Fire the cannon!
				insideCannon.shootPlayer(this);
				insideCannon = null;
				cannonRecoverTime = MAX_CANNON_RECOVER_TIME;
				airJumpsLeft = MAX_AIR_JUMPS;
			}
			invincible = true;
		}
	}

	public void draw(Graphics2D g) {
		//Flickers when invincible
		if ((invincibleTimeLeft/INVINCIBLE_ANIMATE_TIME)%2 == 0) super.draw(g);
		//Has colors drawn when attacking
		if (attacking) { g.setColor(attackHueCycler.getColor()); drawFilled(g); }
	}

	private void checkForLines() {
		if (timeSinceLine > 0)
			timeSinceLine--;
		else {
			//for (Line l : (ArrayList<Line>) main.byType.get(lineClass)) NEEDS DEBUGGING
			for(int i=0; i < main.gameObjects.size(); i++) {
				GameObject go = main.gameObjects.get(i);
				if (go instanceof Line) {
					Line l = (Line) go;
					LineCollisionData lcd = collideLine(l); //CollideLine inherited from CircleObject
					if (lcd.collided)
						reactToLine(l,lcd.above);
				}
			}
		}
	}
	private void reactToLine(Line line, boolean above) {
		status.getLine(line);
		timeSinceLine = MAX_TIME_SINCE_LINE;

		//If above, the I'm above the line
		double mag = Math.sqrt(vx*vx + vy*vy);
		double dir = Math.atan2(vy,vx);
		double perp;
		if (above) 	perp = mag*Math.sin(-line.angle+dir); //My movement perpendicular to the line.
		else			perp = mag*Math.sin(Math.PI - line.angle + dir);
		double bump = BOUNCE_PERP + perp;
		if (above)	accel(line.angle-Math.PI/2,bump);
		else			accel(line.angle+Math.PI/2,bump);

		if (above || line.x1 == line.x2) {
			vy = -BOUNCE_UP;
		}

		airJumpsLeft = MAX_AIR_JUMPS;
	}

	private void checkForCannons() {
		//for (Cannon l : (ArrayList<Cannon>) main.byType.get(lineClass)) NEEDS DEBUGGING
		for(int i=0; i < main.gameObjects.size(); i++) {
			GameObject go = main.gameObjects.get(i);
			if (go instanceof Cannon && collideCircle((Cannon)go)) {
				insideCannon = (Cannon) go; status.getCannonCaught();
				((Cannon)go).catchPlayer(this); }//cannon catches me
		}
	}

	public void getMadeJManHappy(JMan j) { status.getMadeJManHappy(j); }
	public void getHitJMan(JMan j) {
		status.getHitJMan(j);
		double ang = Math.atan2(-Math.abs(y-j.y), x-j.x); //Angle from it to me, flipped about x axis if it was pointed down
		vx = JMAN_BOUNCE*Math.cos(ang);
		vy = JMAN_BOUNCE*Math.sin(ang);
		airJumpsLeft = MAX_AIR_JUMPS;
	}

	private void accel(double dir, double mag) {
		vx += mag*Math.cos(dir);
		vy += mag*Math.sin(dir);
	}

	public void getHurt() {
		if (invincible || attacking) { /* Do nothing */ }
		else {
			vx = -vx; vy = -vy; //Recoil from damage
			invincibleTimeLeft = INVINCIBLE_TIME;
			status.getHurt();
		}
	}

	public void getMultiJump(MultiJump mj) {
		main.helper.playSound("getMultiJump");
		multiJump = true;
		status.getMultiJump(mj);
	}
}