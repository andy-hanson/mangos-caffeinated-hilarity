import java.awt.*;
import java.awt.image.BufferedImage;
import java.applet.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;

/* TODO
fix byType and Player's loop of Lines
*/

/* <applet code="Main" width="500" height="500"></applet> */
public class Main extends Applet implements Runnable {
	private final int START_LEVEL = 1;
	public final boolean CHEAT_ON = false;
	public final int FPS = 30;

	BufferedImage buffer;
	Graphics2D bg; //buffer graphics
	Thread myThread;
	Controller controller;

	FPSControl myFPSControl;
	SpecialGraphics specialGraphics;

	public Player player;
	public PlayerStatus playerStatus; //Player also stores a pointer to it, but since player destroyed after every level, my pointer is more important.
	private LevelCreator levelCreator;

	private String specialMessage; //Objects can give me a special message to show on the  bottom

	public boolean challengeMode;
	private boolean finishedChallengeMode;

	public final int WIDTH = 500;
	public final int HEIGHT = 500;
	public final int BOTTOM_BAR_SIZE = 12;
	public final int PLAY_HEIGHT = HEIGHT - BOTTOM_BAR_SIZE;
	public final double X_BUF = 20; //Must be this far offscreen before x-warping
	public final double LEFT = -X_BUF;
	public final double RIGHT = WIDTH+X_BUF;
	public final double PLAY_WIDTH = WIDTH+2*X_BUF;
	private final int SCORE_FONT_SIZE=20;
	private final int END_GAME_FONT_SIZE = 20;
	private final int END_CHALLENGE_MODE_FONT_SIZE = 30;

	private final int BOTTOM_BAR_IMAGE_HEIGHT = 20;
	Image bottomBarImage;

	private final int MAX_LEVEL = 9; //Levels are 1-9
	private int[] levelScores;
	private double[] levelTimes;
	private int[] levelHealths;

	int CUR_LEVEL;
	double CUR_LEVEL_HEIGHT;
	double scrollY; //Always <= 0 - is amount Mango has moved up, so everything is scrolled up as well to keep Mango centered. No scrollX.

	private boolean onStartScreen;
	private final int NEW_LEVEL_FONT_SIZE = 20;
	private final int NEW_LEVEL_SCREEN_MIN_TIME = 20;
	boolean onNewLevelScreen; int newLevelScreenTime;

	boolean onPauseScreen; Image beforePauseImage, pauseImage;
	//On pause screen, pauseImage is drawn on top of beforePauseImage, which is just the screen from before pause was pressed!

	MainHelper helper;

	ArrayList<GameObject> gameObjects;
	Map<Class,ArrayList<GameObject>> byType; //For example, to get all Lines use byType.get(Line)

	Image background, titleImage;
	Helpers.HueCycler textDisplayHue;

	Backgrounder backgrounder;

	private boolean finishedGame;
	private boolean justDied; //If so, we reset after compute() is finished.

	public void init() {
		scrollY = 0;

		helper = new MainHelper(this);

		buffer = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB); //No transparancy needed for final product!
		bg = buffer.createGraphics();

		backgrounder = new Backgrounder(this);

		controller = new Controller();
		addKeyListener(controller);

		gameObjects = new ArrayList<GameObject>();
		byType = new HashMap<Class,ArrayList<GameObject>>();

		myFPSControl = new FPSControl(this); //This is NOT in GameObjects!

		specialMessage = "Welcome!^.^";
		specialGraphics = new SpecialGraphics(this);
		getObject(specialGraphics);

		levelScores = new int[MAX_LEVEL+1]; levelTimes = new double[MAX_LEVEL+1]; levelHealths = new int[MAX_LEVEL+1];
		//For ease none of these arrays have an entry at index 0.

		//Tell Java graphics to play nice
		Map<RenderingHints.Key,Object> renderingHints = new HashMap<RenderingHints.Key,Object>();
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		bg.setRenderingHints(renderingHints);

		textDisplayHue = new Helpers.HueCycler(this,FPS,25,100); //S=25, V=100

		bottomBarImage = helper.getImage("bottomBar").getScaledInstance(WIDTH,BOTTOM_BAR_IMAGE_HEIGHT,Image.SCALE_SMOOTH);

		titleImage = helper.getImage("title").getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);

		pauseImage = helper.getImage("pause").getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
		beforePauseImage = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		onPauseScreen = false;

		playerStatus = new PlayerStatus(this);

		onStartScreen = true;
	}

	private void winLevel() {
		levelScores[CUR_LEVEL] = playerStatus.score;
		levelTimes[CUR_LEVEL] = Helpers.nearestTenth(playerStatus.timeLeft);
		levelHealths[CUR_LEVEL] = playerStatus.health*10;
		if (challengeMode)
			endChallengeMode();
		else {
			CUR_LEVEL += 1;
			loadLevel();
		}
	}

	private void loadLevel() {
		backgrounder.reset();
		specialGraphics.getNewLevel();

		switch(CUR_LEVEL) {
			case 1: levelCreator = new Level1(); break;
			case 2: levelCreator = new Level2(); break;
			case 3: levelCreator = new Level3(); break;
			case 4: levelCreator = new Level4(); break;
			case 5: levelCreator = new Level5(); break;
			case 6: levelCreator = new Level6(); break;
			case 7: levelCreator = new Level7(); break;
			case 8: levelCreator = new Level8(); break;
			case 9: levelCreator = new Level9(); break;
			case MAX_LEVEL+1: endGame(); return; //Nothing to load, so end function.
			default:
				System.out.println("BAD LEVEL");
				CUR_LEVEL = 1;
				loadLevel();
				return;
		}

		gameObjects = new ArrayList<GameObject>();
		byType = new HashMap<Class, ArrayList<GameObject>>();

		java.util.List<GameObject> levelObjects = levelCreator.makeObjects(this);
		levelCreator = null; //Turning it to null alerts other functions (such as getObject) that we're done loading the level
		for (GameObject o : levelObjects)
			getObject(o);

		// In challenge mode, jellies chase you! :O
		// Actually, that's too hard.
		// if (challengeMode)
		//	getObject(new HomingJellySpawner(this));

		//Put the old steadies back in gameObjects where they always belonged. :)
		playerStatus.reset(); //No keeping combos. Score+time stored in array, reset for each level
		getObject(playerStatus);
		getObject(specialGraphics);

		justDied = false;

		background = helper.getImage("bg"+CUR_LEVEL).getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);

		onNewLevelScreen = true;
		newLevelScreenTime = 0; //EDITed out so can test faster
	}


	public void getObject(GameObject o) {
		if (levelCreator != null) {
			//levelCreator is still running, give it to that.
			levelCreator.get(o);
		}
		else {
			/* Add o to list of objects */
			Class objClass = o.getClass();
			gameObjects.add(o);
			if (byType.get(objClass) != null)
				byType.get(objClass).add(o);
			else {
				ArrayList<GameObject> al = new ArrayList<GameObject>();
				al.add(o);
				byType.put(o.getClass(),al);

			}
		}
	}

	public void start() {
		if (myThread == null) {
			myThread = new Thread(this);
			myThread.start();
		}
	}

	//Wait for a while
	public void sleep(int millis) {
		try {
			myThread.sleep(millis);
		} catch(InterruptedException e) {
			System.out.println("Thread interrupted!");
			stop();
		}
	}

	volatile boolean stopped;
	public void stop() {
		stopped = true;
	}

	public void run() {
		while (!stopped) {
			myFPSControl.update();
			if (onStartScreen)
				startScreenAct();
			else if (onPauseScreen)
				pauseScreenAct();
			else if (onNewLevelScreen)
				newLevelScreenAct();
			else if (finishedGame)
				finishedGameAct();
			else if (finishedChallengeMode)
				finishedChallengeModeAct();
			else {
				computeAll();
				drawAll();
			}
			repaint();
		}
	}

	//GRAPHICS UPDATING
	public void update(Graphics g) {
		paint(g);
	}
	public void paint(Graphics g) {
		g.drawImage(buffer,0,0,this);
	}

	//MAIN PORTION: COMPUTING AND DRAWING
	private void computeAll()
	{
		for(int i=0; i < gameObjects.size(); i++) {
			gameObjects.get(i).compute();
			if (gameObjects.get(i).dead) {
				gameObjects.remove(i);
				i--;
			}
		}

		if (controller.Pause)
			pause();

		//Set scrollY to the amount camera has moved from the 0 position.
		scrollY = player.y - PLAY_HEIGHT/2;
		if (scrollY > -PLAY_HEIGHT)
			scrollY = -PLAY_HEIGHT;
		else if (scrollY < -CUR_LEVEL_HEIGHT)
			scrollY = -CUR_LEVEL_HEIGHT;

		controller.compute(); //Done AFTER others so that upPressedThisFrame is not set to false immediately after getting set to true
		backgrounder.compute();
		textDisplayHue.compute();

		if (justDied)
			loadLevel();
		else if (player.y < -CUR_LEVEL_HEIGHT)
			winLevel();
	}

	private void drawAll() {
		//bg.setAfflineTransform(new AfflineTransform()); EDIT!

		bg.drawImage(background,0,0,this);

		backgrounder.drawBeforeYScroll(bg);
		bg.translate(0,-scrollY);
		backgrounder.drawAfterYScroll(bg);
		for (GameObject go : gameObjects)
			go.draw(bg);
		bg.translate(0,scrollY);

  		bg.drawImage(bottomBarImage,0,500-BOTTOM_BAR_IMAGE_HEIGHT,this);
		bg.setFont(new Font("MONOSPACED",Font.BOLD,BOTTOM_BAR_SIZE*3/2));

		bg.setColor(new Color(64,255,192));
		//Quad 1: Level
		Helpers.drawStringCentered(bg,"Level "+CUR_LEVEL,WIDTH/8,HEIGHT);
		//Quad 2: Combo
		Helpers.drawStringCentered(bg,"Combo "+playerStatus.combo(),WIDTH*3/8,HEIGHT);
		//Quad 3: Special
		Helpers.drawStringCentered(bg,specialMessage,WIDTH*5/8,HEIGHT);
		//Quad 4: Framerate
		int realFramerate = Helpers.round(myFPSControl.getFPS());
		//if ((double)realFramerate)/FPS < 0.75) { //If the achieved framerate is less than 3/4 of desired framerate:
		Helpers.drawStringCentered(bg,"FPS "+realFramerate+"/"+FPS,WIDTH*7/8,HEIGHT);

		//DRAW SCORE AND TIME
		bg.setColor(textDisplayHue.getColor());
		bg.setFont(new Font("SansSerif",Font.BOLD,SCORE_FONT_SIZE));
		Helpers.drawStringCentered(bg,""+playerStatus.score,WIDTH/2,SCORE_FONT_SIZE);
		int intPart = (int) playerStatus.timeLeft;
		int afterDecimal = (int) ((playerStatus.timeLeft%1.0)*10);
		Helpers.drawStringRightAligned(bg,""+intPart+"."+afterDecimal,WIDTH,SCORE_FONT_SIZE);
	}

	private void pause() {
		onPauseScreen = true;
		beforePauseImage.getGraphics().drawImage(buffer,0,0,this);
	}

	private void startScreenAct() {
		if (controller.anyButtonDown()) {
			setChallengeMode(false);
			CUR_LEVEL = START_LEVEL;
			loadLevel();
			onStartScreen = false;
		}
		else if (controller.otherKeyDown >= 49 && controller.otherKeyDown <= 57)
		{
			CUR_LEVEL = controller.otherKeyDown - 49 + 1; //If user presses 1, this will be 1.
			setChallengeMode(true);
			specialMessage = "challenge";
			//Code for 1 key is 49. Goes up until code for 9 key is 49 + 9-1 = 57
			loadLevel();
			onStartScreen = false;
		}
		bg.drawImage(titleImage,0,0,this);
	}

	private void pauseScreenAct() {
		if (controller.anyButtonDown())
			onPauseScreen = false;
		bg.drawImage(beforePauseImage,0,0,this);
		bg.drawImage(pauseImage,0,0,this);
	}

	private void newLevelScreenAct() {
		//The new level screen is used to give the images time to load.
		//After a short time, it will display the background, which contains a huge share of loaded image data.
		//It seems that displaying an image early makes it easier to display later on.

		//Computation
		//Press enter to start game, press a number to go to that level's challenge mode.
		newLevelScreenTime++;
		if (controller.anyButtonDown() && newLevelScreenTime>NEW_LEVEL_SCREEN_MIN_TIME)
			onNewLevelScreen = false;

		//Drawing
		if (newLevelScreenTime<NEW_LEVEL_SCREEN_MIN_TIME) {
			//Don't draw the big background yet
			bg.setColor(Color.BLACK);
			bg.fillRect(0,0,WIDTH,HEIGHT);
		}
		else
			bg.drawImage(background,0,0,this);

		bg.setFont(new Font("MONOSPACED",Font.PLAIN,NEW_LEVEL_FONT_SIZE));
		bg.setColor(Color.RED);

		int cols=3, rows = HEIGHT/NEW_LEVEL_FONT_SIZE;
		int H,S,V=100;
		int x,y;
		int baseH = newLevelScreenTime/2;
		for(int col=0; col<cols; col++) {
			if (col==1) S=100; else S=50;
			x = WIDTH*(col*3+2)/(cols+7);
			for(int row = 0; row < rows; row++) {
				H = baseH + 360*row/rows; while (H >= 360) H -= 360;
				bg.setColor(Helpers.HSVtoRGB(H,S,V));
				y = (int) (HEIGHT*(row+0.5)/rows);
				Helpers.drawStringCentered(bg,"^.^ LEVEL " + CUR_LEVEL + "!",x,y);
			}
		}
	}

	public void getSpecialMessage(String s) {
		specialMessage = s;
	}

	public void endGame() {
		background = helper.getImage("end").getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
		backgrounder.getBackground(new SpeedyStarsBackground(this));
		finishedGame = true;
	}
	public void endChallengeMode() {
		background = helper.getImage("end").getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
		backgrounder.getBackground(new SpeedyStarsBackground(this));
		finishedChallengeMode = true;
	}

	private void setChallengeMode(boolean _) {
		challengeMode = _;
		playerStatus.setChallengeMode(_);
	}

	public void finishedGameAct() {
		textDisplayHue.compute();

		bg.drawImage(background,0,0,this);
		backgrounder.compute();
		backgrounder.drawBeforeYScroll(bg);
		backgrounder.drawAfterYScroll(bg);

		bg.setFont(new Font("MONOSPACED",Font.PLAIN,END_GAME_FONT_SIZE));
		bg.setColor(textDisplayHue.getColor());

		int xLevel = 50, xScore = 150, xTime = 250, xHealth = 350, xNet = 450;
		int yHeader = 100;
		Helpers.drawStringCentered(bg,"LEVEL",xLevel,yHeader);
		Helpers.drawStringCentered(bg,"SCORE",xScore,yHeader);
		Helpers.drawStringCentered(bg,"TIME",xTime,yHeader);
		Helpers.drawStringCentered(bg,"HEALTH",xHealth,yHeader);
		Helpers.drawStringCentered(bg,"NET",xNet,yHeader);
		int minY = 150; int maxY = 400;
		for (int lev = 1; lev <= MAX_LEVEL; lev++) {
			int y = minY + (maxY-minY) * (lev-1)/MAX_LEVEL;
			Helpers.drawStringCentered(bg,""+lev,xLevel,y);
			Helpers.drawStringCentered(bg,""+levelScores[lev],xScore,y);
			Helpers.drawStringCentered(bg,""+levelTimes[lev],xTime,y);
			Helpers.drawStringCentered(bg,""+levelHealths[lev],xHealth,y);
			Helpers.drawStringCentered(bg,""+(levelScores[lev]+levelTimes[lev]+levelHealths[lev]),xNet,y);
		}
		//Draw totals
		int y3 = 450;
		Helpers.drawStringCentered(bg,"TOTAL",xLevel,y3);
		Helpers.drawStringCentered(bg,""+Helpers.arraySum(levelScores),xScore,y3);
		Helpers.drawStringCentered(bg,""+Helpers.arraySum(levelTimes),xTime,y3);
		Helpers.drawStringCentered(bg,""+Helpers.arraySum(levelHealths),xHealth,y3);
		Helpers.drawStringCentered(bg,""+(Helpers.arraySum(levelScores)+Helpers.arraySum(levelTimes)+Helpers.arraySum(levelHealths)),xNet,y3);
	}

	public void finishedChallengeModeAct() {
		textDisplayHue.compute();

		bg.drawImage(background,0,0,this);
		backgrounder.compute();
		backgrounder.drawBeforeYScroll(bg);
		backgrounder.drawAfterYScroll(bg);

		bg.setFont(new Font("MONOSPACED",Font.PLAIN,END_CHALLENGE_MODE_FONT_SIZE));
		bg.setColor(textDisplayHue.getColor());

		//No health score for challenge mode.
		Helpers.drawStringCentered(bg,"FINISHED CHALLENGE MODE!",250,100);
		Helpers.drawStringCentered(bg,"LEVEL " + CUR_LEVEL,250,200);
		Helpers.drawStringCentered(bg,"SCORE " + levelScores[CUR_LEVEL],250,300);
		Helpers.drawStringCentered(bg,"TIME " + levelTimes[CUR_LEVEL],250,350);
		Helpers.drawStringCentered(bg,"NET " + (levelScores[CUR_LEVEL]+levelTimes[CUR_LEVEL]),250,400);
		Helpers.drawStringCentered(bg,"^.^ Nice! ^.^",250,500);
	}

	public void getPlayerDied() {
		//When the player dies, pause for 2 seconds, then restart the level.
		helper.playSound("died");
		specialMessage = "Try Again!";
		sleep(2000);
		justDied = true;
	}
}