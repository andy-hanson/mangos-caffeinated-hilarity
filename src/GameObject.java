import java.awt.*;
import java.applet.*;

public abstract class GameObject {
	protected Main main;
	public boolean dead;
	public GameObject(Main m) { main = m; dead = false; }
	abstract void compute();
	abstract void draw(Graphics2D g);
}