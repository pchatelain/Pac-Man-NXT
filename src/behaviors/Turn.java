package behaviors;

import io.Instruction;
import io.Instructor;
import io.LineDisplayWriter;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import sensors.DoubleSensor;
import tools.Car;
import tools.Configurable;
import tools.Configurator;

public class Turn implements Behavior, Configurable {

	private boolean suppressed;
	private DoubleSensor sensor;
	private Instructor instructor;
	
    public Turn(DoubleSensor sensor, Instructor instructor) {
    	this.sensor = sensor;
    	this.instructor = instructor;
    	String[] settings = {"low", "high", "thresh", "min", "max", "diff"};
        Configurator conf = new Configurator(this, settings);
        conf.listen();
    }
    
    private static int greyCount = 0;
    private static int threshhold = 5;
    private static int min = 75;
    private static int max = 150;
    private static int diff = 18;
	
	private int highPower = 85;
	private int lowPower = -50;

    private long repeatThreshhold = 650;
    private long lastRun = 0;
    
    public boolean takeControl() {
    	if(running)
    		return true;
    	if(System.currentTimeMillis() - lastRun < repeatThreshhold)
    		return false;
    	int left = sensor.getLeftNormalized();
    	int right = sensor.getRightNormalized();
    	LineDisplayWriter.setLine("Light: "+right+"  "+left, 0, true);
    	
    	if(Math.abs(left-right) < diff
    	&& min < left+right && left+right < max)
			greyCount++;
    	else if(greyCount > 0)
    		greyCount--;
    	
		if(greyCount < threshhold)
			return false;
    	greyCount = 0;
		
    	lastRun = System.currentTimeMillis();
    	
		Instruction peek = instructor.peek();
    	LineDisplayWriter.setLine("Instr: "+peek, 1, true);
    	
    	switch(peek) {
    	case START:
    	case FORWARD:
    	case DOT:
			instructor.next();
			Sound.playTone(500, 100);
	    	return false;
    	default:
    		return running = true;
    	}
    }
    
    public void suppress() {
    	suppressed = true;
    }
    
    public boolean running = false;
    public long minTurn = 250;
    public void action() {
    	suppressed = false;
    	
    	int left = sensor.getLeftNormalized();
    	int right = sensor.getRightNormalized();
    	boolean seenBlack = false;
		switch(instructor.next()) {
		case LEFT:
			Car.forward(lowPower, highPower);
			Sound.playTone(250, 100);
			while(!suppressed) {
		    	left = sensor.getLeftNormalized();
				right = sensor.getRightNormalized();
				seenBlack = seenBlack || left < 30;
				if(seenBlack && left > 70 && (right < 30 || 70 < right))
					break;
				sleep(5);
			}
			break;
		case RIGHT:
			Car.forward(highPower, lowPower);
			Sound.playTone(750, 100);
			while(!suppressed) {
		    	left = sensor.getLeftNormalized();
				right = sensor.getRightNormalized();
				seenBlack = seenBlack || right < 30;
				if(seenBlack && right > 70 && (left < 30 || 70 < left))
					break;
				sleep(5);
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
		running = false;
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
			lowPower--;break;
		case 1:
			highPower--;break;
		case 2:
			threshhold--;break;
		case 3:
			min--;break;
		case 4:
			max--;break;
		case 5:
			diff--;break;
		}
	}

	@Override
	public String getSettingValue(int setting) {
		switch(setting) {
		case 0:
			return lowPower+"";
		case 1:
			return highPower+"";
		case 2:
			return threshhold+"";
		case 3:
			return min+"";
		case 4:
			return max+"";
		case 5:
			return diff+"";
		}
		return null;
	}

	public void increaseSetting(int setting) {
		switch(setting) {
		case 0:
			lowPower++;break;
		case 1:
			highPower++;break;
		case 2:
			threshhold++;break;
		case 3:
			min++;break;
		case 4:
			max++;break;
		case 5:
			diff++;break;
		}
	}
}
