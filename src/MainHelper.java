import java.awt.*;
import java.applet.*;
import java.net.*;
import java.awt.image.*;
import java.util.*;

public class MainHelper {
	//Has functions to help an applet A.
	//Needs A's data for the functions.
	Main main;

	MediaTracker mediaTracker;
	int trackerID;
	private Map<String,Image> loadedImages; //All images are hosted here!
	private Map<String, AudioClip> sounds;

	private final String DATA_DIRECTORY = "data";
	private final String IMAGE_DIRECTORY = Helpers.pathJoin(DATA_DIRECTORY,"images");
	private final String SOUND_DIRECTORY = Helpers.pathJoin(DATA_DIRECTORY,"sounds");

	private final String[] ALL_SOUNDS = {
		"airJump.wav", "attack.wav", "bonus.wav", "bumper.wav",
		"cannonFired.wav", "caughtInCannon.wav",
		"combo1.wav", "combo2.wav", "combo3.wav", "combo4.wav", "combo5.wav", "combo6.wav", "combo7.wav", "combo8.wav",
		"combo9.wav", "combo10.wav", "combo11.wav", "combo12.wav", "combo13.wav", "combo14.wav", "combo15.wav",
		"died.wav", "getMultiJump.wav", "hurt.wav", "jellyKilled.wav",
		"JManBounce.wav", "MadeJManHappy.wav"
	};

	public MainHelper(Main m) {
		main = m;
		mediaTracker = new MediaTracker(main);
		trackerID = 0;

		loadedImages = new HashMap<String,Image>();
		sounds = new HashMap<String,AudioClip>();

		//Load all sounds to sounds now so they load quicker later.
		for (String name : ALL_SOUNDS) {
			String withoutExtension = new StringTokenizer(name,".").nextToken();
			AudioClip ac = main.getAudioClip(main.getDocumentBase(),Helpers.pathJoin(SOUND_DIRECTORY,name));
			sounds.put(withoutExtension,ac);
		}
	}

	// IMAGE STUFF

	public Image getImage(String name) {
		Image img;
		if (loadedImages.containsKey(name))
			img = loadedImages.get(name);
		else {
			//Load from file
      		img = main.getImage(main.getDocumentBase(), Helpers.pathJoin(IMAGE_DIRECTORY,name+".png"));
			trackerID++;
			mediaTracker.addImage(img,trackerID);
			try { mediaTracker.waitForID(trackerID); } //ENSURE THAT IS LOADED!
			catch (InterruptedException e) { System.out.println("Image loading interrupted in mainainHelper"); }
			if (mediaTracker.isErrorAny()) {
				throw new RuntimeException("Error loading " + name);
			}
			/*while (!mediaTracker.checkAll()) {
      		System.out.println("Please wait...");
			} This basically never works. Oh well. */
			/*System.out.println("Loaded image " + name + "with ID " + trackerID);
			System.out.println("Check: " + mediaTracker.checkID(trackerID));
			System.out.println("Error: " + mediaTracker.isErrorID(trackerID));
			System.out.println("Status: " + mediaTracker.statusID(trackerID,false));
			System.out.println("(LOADING " + mainediaTracker.LOADING + ", ABORTED " + mainediaTracker.ABORTED +
										", ERRORED " + mainediaTracker.ERRORED + ", COmainPLETE " + mainediaTracker.COmainPLETE);*/
			loadedImages.put(name,img);
		}
		return img;
	}

	public boolean storeImage(String name, Image img) {
		//Classes can ask me to store an image for them.
		//This becomes useful in the Line class, where the same image can be stored for all length 200 lines.
		//Note: mainake sure the name is unique! Otherwise will do nothing!
		//Returns whether or not it stored the image
		if (loadedImages.containsKey(name))
			return false;

		loadedImages.put(name,img);
		return true;
	}

	public boolean hasImage(String name) {
		return loadedImages.containsKey(name);
	}

	public BufferedImage tileImage(Image img, int width, int height) {
		//Tile img over an area (width,height)
		BufferedImage tiled = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics tg = tiled.getGraphics();
		for(int x = 0; x < width; x += img.getWidth(main))
			for(int y = 0; y < height; y+= img.getHeight(main))
				tg.drawImage(img,x,y,main);
		return tiled;
	}

	public String imagesString() {
		String s = "";
		for (String k : loadedImages.keySet())
		{
			s += k + ":			" + loadedImages.get(k) + "\n";
		}
		return s;
	}

	/* POTENTIALLY VERY SLOW!!!*/
	//Not used by anything yet
	public BufferedImage makeBufferedImage(Image i) {
		BufferedImage bi = new BufferedImage(i.getWidth(main),i.getHeight(main),BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.drawImage(i,0,0,main);
		return bi;
	}

	//SOUND STUFF
	public void playSound(String name) { sounds.get(name).play(); }
	public void stopSound(String name) { sounds.get(name).stop(); }
}