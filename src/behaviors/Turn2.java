package behaviors;

import java.io.IOException;

import io.BTReceiver;
import io.LineDisplayWriter;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import sensors.DoubleSensor;
import tools.Car;
import tools.Configurable;
import tools.Configurator;

public class Turn2 implements Behavior, Configurable {

	private boolean suppressed;
	private DoubleSensor sensor;
	private BTReceiver receiver;
	private int fullPower = 100;
	private int meanPower = 85;
	private int lowPower = -60;

    public Turn2(DoubleSensor sensor, BTReceiver receiver) {
    	this.sensor = sensor;
    	this.receiver = receiver;
    	String[] settings = {"thresh", "min", "max", "diff", "sleep"};
        Configurator conf = new Configurator(this, settings);
        conf.listen();
    }
    
    private int greyCount = 0;
    private int thresh = 6;
    private int min = 75;
    private int max = 130;
    private int diff = 18;

    public boolean takeControl() {
    	int left = sensor.getLeftNormalized();
    	int right = sensor.getRightNormalized();
		greyCount--;
    	if(Math.abs(left-right) < diff)
    		if(min < left+right && left+right < max)
    			greyCount += 2;
		greyCount = Math.max(0, greyCount);
    	LineDisplayWriter.setLine("Light: "+right+"  "+left, 6, true);
    	int threshhold = thresh;
		return greyCount > threshhold;
    }

    public void suppress() {
    	suppressed = true;
    }
    
    private long lastRun = 0;
    private long repeatThreshhold = 785;
    private int sleep = 300;
    public void action() {
    	if(System.currentTimeMillis() - lastRun < repeatThreshhold)
    		return;
    	lastRun = System.currentTimeMillis();
    	suppressed = false;
    	try {
			switch(receiver.nextInstruction()) {
			case START:
			case DOT:
				Car.forward(meanPower, meanPower);
				Sound.playTone(400, 100);
				sleep(50);
				break;
			case LEFT:
				Car.forward(lowPower, fullPower);
				Sound.playTone(250, 100);
				sleep(sleep);
				break;
			case RIGHT:
				Car.forward(fullPower, lowPower);
				Sound.playTone(600, 100);
				sleep(sleep);
				break;
			case STOP:
				Sound.playTone(600, 100);
				Car.stop();
				break;
			case EOT:
				Car.stop();
				System.exit(0);
			default:
				Car.forward(meanPower, meanPower);
				Sound.playTone(400, 100);
				sleep(50);
				break;
			}
		} catch (IOException e) {
			System.exit(1);
		}
    	greyCount = 0;
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
