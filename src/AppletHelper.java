import java.awt.*;
import java.applet.*;
import java.net.*;

public class AppletHelper {
	//Has functions to help an applet A.
	//Needs A's data for the functions.
	Applet A;
	public AppletHelper(Applet a)
	{
		A = a;
	}

	public Image loadPNG(String name) {
		URL base = A.getDocumentBase();
		Image img = A.getImage(base,name+".png");
		return img;
	}
}