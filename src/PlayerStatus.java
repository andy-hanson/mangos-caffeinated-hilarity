import java.awt.*;

public class PlayerStatus extends GameObject {
	private Player player;

	private final int MAX_COMBO_SOUND = 15; //No combo sounds after this, so just plays sound combo15 over and over after that.
	private final int HURT_SCORE_LOSS = 5;
	private int MAX_HEALTH;
	public int health;
	private Image heartFullImage, heartEmptyImage;
	private final int HEART_IMAGE_X_SPACE = 26;
	private int combo;

	int score;

	private final double LEVEL_START_TIME = 100; //Seconds
	public double timeLeft;
	private Helpers.HueCycler timeHueCycler;

	public PlayerStatus(Main m) {
		super(m);
		heartFullImage = main.helper.getImage("heartFull");
		heartEmptyImage = main.helper.getImage("heartEmpty");
		combo = 1;
		score = 0;

		int S = 50; int V = 50;
		timeHueCycler = new Helpers.HueCycler(main,main.FPS,S,V); //Cycles once per second.
	}

	public void setChallengeMode(boolean _) {
		MAX_HEALTH = _ ? 4 : 9;
		health = MAX_HEALTH;
	}

	public void getPlayer(Player p) { player = p; }

	public void compute() {
		timeLeft -= 1.0/main.FPS;
		timeHueCycler.compute();

		if (main.controller.Start)
			main.getPlayerDied(); //Suicide button!
	}

	public void draw(Graphics2D g) {
		// Draw the health.
		Image img; int x; int blitY = Helpers.round(main.scrollY);
		for(int i = 1; i <= MAX_HEALTH; i++) {
			x = (i-1)*HEART_IMAGE_X_SPACE; img = (i<=health)? heartFullImage : heartEmptyImage;
			g.drawImage(img,x,blitY,main);
		}
	}

	private void getScore() {
		main.helper.playSound("combo" + Math.min(combo,MAX_COMBO_SOUND));
		score += 1*combo;
		main.specialGraphics.getScore(player.x,player.y,1*combo,player.vx,player.vy);
		combo++;
	}

	private void getAction() {
		main.backgrounder.getAction(player.x,player.y,combo,player.vx,player.vy);
	}

	public void getHurt() {
		main.helper.playSound("hurt");
		score -= HURT_SCORE_LOSS;
		//loseCombo();
		main.specialGraphics.getScore(player.x,player.y,-HURT_SCORE_LOSS,player.vx,player.vy);

		health--;
		if (health <= 0)
			main.getPlayerDied();
	}

	public void loseCombo() {
		combo = 1;
		main.specialGraphics.getComboLost();
	}

	public void getLine(Line line) {
		if (line.beenHit) { loseCombo(); getAction(); }
		else { getScore(); getAction(); }
		line.getHit();
		main.helper.playSound("bumper"); main.getSpecialMessage("boing!");
	}

	public boolean hasCombo() {
		return combo > 1;
	}
	public int combo() {
		return combo;
	}

	public void getBonus(Bonus b) {
		main.helper.playSound("bonus");
		getScore();
		getAction();
		main.getSpecialMessage("bonus");
	}
	public void getMadeJManHappy(JMan j) {
		main.helper.playSound("MadeJManHappy");
		getScore();
		getAction();
		main.getSpecialMessage("JOY");
	}
	public void getHitJMan(JMan j) {
		getAction();
		main.getSpecialMessage("woo");
	}
	public void getCannonCaught() {
		main.helper.playSound("caughtInCannon");
		getAction();
		main.getSpecialMessage("Loaded");
	}
	public void getCannonFiredFirstTime() {
		main.helper.playSound("cannonFired");
		getScore();
		getAction();
		main.getSpecialMessage("Fire!");
	}
	public void getCannonFire() {
		main.helper.playSound("cannonFired");
		getAction();
		main.getSpecialMessage("boom");
	}
	public void getMultiJump(MultiJump mj) {
		getAction();
		main.getSpecialMessage("POWER!");
	}

	public void reset() {
		combo = 1;
		score = 0;
		health = MAX_HEALTH;
		timeLeft = LEVEL_START_TIME;
	}
}