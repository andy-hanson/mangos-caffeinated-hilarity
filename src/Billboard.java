import java.awt.Image;
public class Billboard extends RectObject
{
	public Billboard(Main m, double ax, double ay, int width, int height, String imgName) {
		super(m,ax,ay,width,height);
		img = main.helper.getImage(imgName).getScaledInstance(width,height,Image.SCALE_SMOOTH);
	}
}