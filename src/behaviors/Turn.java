package behaviors;

import io.Instruction;
import io.Instructor;
import io.LineDisplayWriter;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import sensors.DoubleSensor;
import tools.Car;
import tools.Configurable;

public class Turn implements Behavior, Configurable {

	private boolean suppressed;
	private DoubleSensor sensor;
	private Instructor instructor;
	
	private int fullPower = 100;
	private int lowPower = -60;
	
    public Turn(DoubleSensor sensor, Instructor instructor) {
    	this.sensor = sensor;
    	this.instructor = instructor;
//    	String[] settings = {"thresh", "min", "max", "diff", "sleep"};
//        Configurator conf = new Configurator(this, settings);
//        conf.listen();
    }
    
    private static int greyCount = 0;
    private static int threshhold = 5;
    private static int min = 75;
    private static int max = 140;
    private static int diff = 18;

    private long shortDist = 785;
    public boolean takeControl() {
    	int left = sensor.getLeftNormalized();
    	int right = sensor.getRightNormalized();
    	if(Math.abs(left-right) < diff
    	&& min < left+right && left+right < max)
			greyCount++;
    	else if(greyCount > 0)
    		greyCount--;
    	LineDisplayWriter.setLine("Light: "+right+"  "+left, 6, true);
		if(greyCount > threshhold) {
			Instruction peek = instructor.peek();
			if(peek == Instruction.START
			|| peek == Instruction.FORWARD
			|| peek == Instruction.DOT) {
		    	greyCount = 0;
		    	return false;
			}
			return true;
		} else {
			return false;
		}
    }
    
    public void suppress() {
    	suppressed = true;
    }
    
    private long lastRun = 0;
    private long repeatThreshhold = shortDist;
    private int sleep = 300;
    public void action() {
    	if(System.currentTimeMillis() - lastRun < repeatThreshhold)
    		return;
    	lastRun = System.currentTimeMillis();
    	suppressed = false;

    	int left = sensor.getLeftNormalized();
    	int right = sensor.getRightNormalized();
		switch(instructor.next()) {
		case LEFT:
			Car.forward(lowPower, fullPower);
			Sound.playTone(250, 100);
			while(left > 40) {
		    	left = sensor.getLeftNormalized();
				sleep(10);
			}
			break;
		case RIGHT:
			Car.forward(fullPower, lowPower);
			Sound.playTone(600, 100);
			while(right > 40) {
				right = sensor.getLeftNormalized();
				sleep(10);
			}
			break;
		case STOP:
			Car.stop();
			Sound.playTone(100, 100);
			break;
		case EOT:
			Car.stop();
			Sound.playTone(500, 100);
			Sound.playTone(100, 100);
			System.exit(0);
		default:
			Car.stop();
			Sound.playTone(800, 50);
			sleep(10);
			Sound.playTone(800, 50);
			sleep(10);
			Sound.playTone(800, 50);
			sleep(10);
			Sound.playTone(800, 50);
			sleep(10);
			Sound.playTone(800, 50);
			sleep(10);
			Sound.playTone(800, 50);
			break;
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
			threshhold--;break;
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
			return threshhold+"";
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
			threshhold++;break;
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
