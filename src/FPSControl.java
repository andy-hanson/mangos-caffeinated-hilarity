class FPSControl {
	/* Stores time difference between frames and puts it in an array. Also waits for the next frame if have extra time. */
	final int STORED_TIMES = 20; //Number of frame time differences kept
	int[] frameTimes; int nextFrameTime;
	long lastTime; //The last clock time, NOT the time difference between frames
	int timeLatestFrameTook; //The time in nanos that the last frame took
	int waitTime; //Time I will wait until the next frame. Adjusts itself over time.
	Main main;
	public FPSControl(Main m) {
		main = m;
		lastTime = -1; //Not set until first update()
		nextFrameTime = 0;
		frameTimes = new int[STORED_TIMES];
		timeLatestFrameTook = 0;

		waitTime = 0;
	}

	public void update() {
		if (lastTime == -1)
			lastTime = System.nanoTime();
		else
		{
			nextFrameTime++;
			if (nextFrameTime >= STORED_TIMES)
				nextFrameTime = 0;
			long thisTime = System.nanoTime();
			timeLatestFrameTook = (int) (thisTime - lastTime);
			frameTimes[nextFrameTime] = timeLatestFrameTook;
			lastTime = thisTime;

			//Adjust waitTime, then wait.
			if (timeLatestFrameTook <= 1000000000/main.FPS) waitTime++;
			else { waitTime--; if (waitTime < 0) waitTime = 0; }
			main.wait(waitTime);
			//System.out.println(timeLatestFrameTook + "	" + waitTime);
		}
	}

	public double getAverageWaitTime() {
		long netTime = 0;
		for (long individualTime : frameTimes)
			netTime += individualTime;
		return netTime/STORED_TIMES;
	}

	public double getFPS() {
		long netTime = 0;
		for (long individualTime : frameTimes)
			netTime += individualTime;
		return STORED_TIMES/((double)netTime/1000000000); //AKA # frames (STORED_TIMES) divided by # seconds (nanoseconds/1000000000)
	}
}