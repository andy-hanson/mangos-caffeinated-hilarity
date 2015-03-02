import java.awt.*;

public class ScoreFX extends GameObject implements GraphicsFX {
	public static Font scoreFont = new Font("Serif",Font.ITALIC,20); //Font f = Font.createFont( Font.TRUETYPE_FONT, new FileInputStream("font.ttf") );
	public double x, y;
	public int score;
	Helpers.HueCycler hueCycler;

	public ScoreFX(Main m, double ax, double ay, int amount) {
		super(m);
		x = ax; y = ay; score = amount;
		int hueCycleTime = 80 - 4*amount;
		if (hueCycleTime < 20) hueCycleTime = 20;

		hueCycler = new Helpers.HueCycler(main,hueCycleTime);
		main.getObject(hueCycler);
	}

	public void compute() { /* pass */ }

	public void draw(Graphics2D g) {
		g.setFont(scoreFont);
		g.setColor(hueCycler.getColor());
		if (score >= 0)
			Helpers.drawStringCentered(g,"+"+score,x,y);
		else
			Helpers.drawStringCentered(g,""+score,x,y); //"-" sign will appear anyway
	}

	public boolean isDead() {
		if (score < 0) return  main.playerStatus.hasCombo(); //Negative ScoreFX disappear when points are gained again
		return false; /*Otherwise, only die once the combo is lost, and told to die by superclass.*/
	}
}