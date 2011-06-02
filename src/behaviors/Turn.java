package behaviors;

import io.LineDisplayWriter;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import robot.LineFollower;
import sensors.DoubleSensor;
import tools.Car;
import tools.Configurable;

public class Turn implements Behavior, Configurable {

	private boolean suppressed;
	private DoubleSensor sensor;
	private int fullPower = 100;
	private int meanPower = 85;
	private int lowPower = -60;

	private Direction[] sequence = {Direction.FORWARD};
//	private Direction[] sequence = {
//			Direction.FORWARD, Direction.FORWARD,
//			Direction.RIGHT,
//			Direction.FORWARD, Direction.FORWARD, Direction.FORWARD, Direction.FORWARD,
//			Direction.FORWARD, Direction.LEFT, Direction.LEFT, Direction.FORWARD, 
//			Direction.FORWARD, Direction.FORWARD, Direction.FORWARD, Direction.FORWARD,
//			Direction.FORWARD, Direction.LEFT, Direction.FORWARD,
//			Direction.FORWARD, Direction.FORWARD,
//			Direction.FORWARD, Direction.LEFT, Direction.FORWARD,
//			Direction.FORWARD, Direction.FORWARD, Direction.FORWARD, Direction.FORWARD,
//			Direction.FORWARD, Direction.LEFT, Direction.LEFT,Direction.FORWARD, 
//			Direction.FORWARD, Direction.FORWARD, Direction.FORWARD, Direction.FORWARD,
//			Direction.RIGHT};
	private int seqPos = 0;
	
    public Turn(DoubleSensor sensor) {
    	this.sensor = sensor;
//    	String[] settings = {"thresh", "min", "max", "diff", "sleep"};
//        Configurator conf = new Configurator(this, settings);
//        conf.listen();
    }
    
    private static int greyCount = 0;
    private static int thresh = 5;
    private static int min = 95;
    private static int max = 180;
    private static int diff = 15;

    private long longDist = 1900;
    private long shortDist = 785;
    public boolean takeControl() {
    	int left = sensor.getLeftNormalized();
    	int right = sensor.getRightNormalized();
    	if(isGrey(left, right))
			greyCount++;
    	else if(greyCount > 0)
    		greyCount--;
    	LineDisplayWriter.setLine("Light: "+right+"  "+left, 6, true);
    	int threshhold = thresh;
    	if(Math.abs(System.currentTimeMillis() - lastRun - longDist) < 50)
    		threshhold /= 2;
		return greyCount > threshhold;
    }
    
    public static boolean isGrey(int left, int right) {
    	return Math.abs(left-right) < diff && min < left+right && left+right < max;
    }
    
    public void suppress() {
    	suppressed = true;
    }
    
    private long lastRun = 0;
    private long repeatThreshhold = shortDist;
    private int sleep = 360;
    public void action() {
    	if(System.currentTimeMillis() - lastRun < repeatThreshhold)
    		return;
//    	LineDisplayWriter.setLine(Long.toString(System.currentTimeMillis() - lastRun), 2, true);
    	lastRun = System.currentTimeMillis();
    	if(seqPos == sequence.length)
    		seqPos = 0;
    	suppressed = false;
    	switch(sequence[seqPos]) {
    	case FORWARD:
    		if(LineFollower.motor)
    			Car.forward(meanPower, meanPower);
    		Sound.playTone(400, 100);
    		sleep(50);
    		break;
    	case LEFT:
    		if(LineFollower.motor)
    			Car.forward(lowPower, fullPower);
    		Sound.playTone(250, 100);
    		sleep(sleep);
    		break;
    	case RIGHT:
    		if(LineFollower.motor)
    			Car.forward(fullPower, lowPower);
    		Sound.playTone(600, 100);
    		sleep(sleep);
    		break;
    	}
    	greyCount = 0;
		seqPos++;
    }
    
    private void sleep(int duration) {
		for(int i = 0; i < duration/5 && !suppressed; i++) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				Car.stop();
				System.exit(1);
			}
		}
    }

	public void decreaseSetting(int setting) {
		switch(setting) {
		case 0:
			thresh--;break;
		case 1:
			min--;break;
		case 2:
			max--;break;
		case 3:
			diff--;break;
		case 4:
			sleep--;break;
		}
	}

	@Override
	public String getSettingValue(int setting) {
		switch(setting) {
		case 0:
			return thresh+"";
		case 1:
			return min+"";
		case 2:
			return max+"";
		case 3:
			return diff+"";
		case 4:
			return sleep+"";
		}
		return null;
	}

	public void increaseSetting(int setting) {
		switch(setting) {
		case 0:
			thresh++;break;
		case 1:
			min++;break;
		case 2:
			max++;break;
		case 3:
			diff++;break;
		case 4:
			sleep++;break;
		}
	}
}
