import java.awt.*;

class AnimatedImage extends GameObject {
	int frame, frameTime, timeUntilFrame, frames;
	boolean turnBack; //Whether go 012321012... or 01230123012...
	boolean goingBack; //Used if turnBack on; whether frame going up or down.
	Image[] animation;

	public AnimatedImage(Main m, String name, int numFrames, int timeBetweenFrames, boolean turnsBack) {
		super(m);
		frames = numFrames;
		animation = new Image[frames];
		for (int i = 0; i < frames; i++)
			animation[i] = main.helper.getImage(name+i);
		frameTime = timeBetweenFrames;
		frame = 0; timeUntilFrame = 0;

		turnBack = turnsBack; goingBack = false;
	}
	public AnimatedImage(Main m, String name, int numFrames, int timeBetweenFrames) {
		this(m,name,numFrames,timeBetweenFrames,false);
	}

	public void compute() {
		timeUntilFrame++;
		if (timeUntilFrame >= frameTime) {
			timeUntilFrame = 0;
			if (turnBack) {
				if (goingBack) { frame--; if (frame <= 0) goingBack = false; }
				else { frame++; if (frame >= frames-1) goingBack = true; }
			}
			else { frame++; if (frame >= frames) frame -= frames; }
		}
	}

	public void draw(Graphics2D g) { /*pass*/ }

	public Image getImage() { return animation[frame]; }
}