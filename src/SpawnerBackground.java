import java.util.*;
import java.awt.*;

public abstract class SpawnerBackground extends BackgroundGraphics {
	protected abstract double SPAWN_RAND_CHANCE();

	public boolean YScrolling() { return true; }

	protected static abstract class Spawned {
		public boolean dead;
		public abstract void draw(Graphics2D g); public abstract void compute();
	}

	protected ArrayList<Spawned> spawneds;

	public SpawnerBackground(Main m) { super(m); spawneds = new ArrayList<Spawned>(); }

	public void compute() {
		//Formula so that if SPAWN_RAND_CHANCE is 2.5, it will happen 2 times or 3 times randomly
		double d = SPAWN_RAND_CHANCE();
		while (d > 1) { getNew(); d--; }
		if (Math.random() < d) getNew();

		for(int i = 0; i < spawneds.size(); i++) {
			spawneds.get(i).compute();
			if (spawneds.get(i).dead) { spawneds.remove(i); i--; }
		}
	}

	protected abstract void getNew();

	public void draw(Graphics2D g) {
		for (Spawned s : spawneds)
			s.draw(g);
	}

	public abstract void getAction(double x, double y, int score, double playerVX, double playerVY);
}