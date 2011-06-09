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
	
    public Turn(DoubleSensor sensor, Instructor instructor) {
    	this.sensor = sensor;
    	this.instructor = instructor;
//    	String[] settings = {"thresh", "min", "max", "diff"};
//        Configurator conf = new Configurator(this, settings);
//        conf.listen();
    }
    
    private static int greyCount = 0;
    private static int threshhold = 5;
    private static int min = 75;
    private static int max = 140;
    private static int diff = 18;
	
	private int fullPower = 100;
	private int lowPower = -60;

    private long repeatThreshhold = 785;
    private long lastRun = 0;
    
    public boolean takeControl() {
    	int left = sensor.getLeftNormalized();
    	int right = sensor.getRightNormalized();
    	if(Math.abs(left-right) < diff
    	&& min < left+right && left+right < max)
			greyCount++;
    	else if(greyCount > 0)
    		greyCount--;
    	LineDisplayWriter.setLine("Light: "+right+"  "+left, 1, true);
		if(greyCount > threshhold
		&& System.currentTimeMillis() - lastRun < repeatThreshhold) {
	    	lastRun = System.currentTimeMillis();
	    	
			Instruction peek = instructor.peek();
	    	LineDisplayWriter.setLine("Instr.: "+peek, 2, true);
	    	
			if(peek == Instruction.START
			|| peek == Instruction.FORWARD
			|| peek == Instruction.DOT) {
				instructor.next();
				Sound.playTone(400, 100);
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
    
    public void action() {
    	suppressed = false;

    	int left = sensor.getLeftNormalized();
    	int right = sensor.getRightNormalized();
		switch(instructor.next()) {
		case LEFT:
			Car.forward(lowPower, fullPower);
			Sound.playTone(250, 100);
			while(left < 45 || 45 < right) {
		    	left = sensor.getLeftNormalized();
				right = sensor.getRightNormalized();
				sleep(10);
			}
			break;
		case RIGHT:
			Car.forward(fullPower, lowPower);
			Sound.playTone(600, 100);
			while(left > 45 || 45 > right) {
		    	left = sensor.getLeftNormalized();
				right = sensor.getRightNormalized();
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
			sleep(100);
			Sound.playTone(100, 100);
			System.exit(0);
		default:
			Car.stop();
			for(int i = 0; i < 5; i++) {
				Sound.playTone(800, 50);
				sleep(55);
			}
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
		}
	}
}
