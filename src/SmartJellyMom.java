public class SmartJellyMom extends JellyMom
{
	public SmartJellyMom(Main m, double x, double y, int timeBetweenShots, double str) {
		super(m,x,y,timeBetweenShots,str);
	}

	public void shoot() {
		double timeToThere = getDistanceTo(main.player) / strength;
		double expectedX = main.player.x + timeToThere*main.player.vx; double expectedY = main.player.y + timeToThere*main.player.vy;
		double ang = Math.atan2(expectedY-y, expectedX-x);
		main.getObject(new Jelly(main,x,y,ang,strength));
	}
}