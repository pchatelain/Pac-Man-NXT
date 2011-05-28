package behaviors;

import io.LineDisplayWriter;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import sensors.Color;
import sensors.DoubleSensor;
import tools.Car;

public class Turn implements Behavior {

	private boolean suppressed;
	private DoubleSensor sensor;
	private int fullPower = 100;
	private int lowPower = -40;
	
	private Direction[] sequence = {
			Direction.RIGHT};
	private int seqPos = 0;
	
    public Turn(DoubleSensor sensor) {
    	this.sensor = sensor;
    }
    
    private int greyCount = 0;
    public boolean takeControl() {
		if(sensor.getRightColor() == Color.GREY && sensor.getLeftColor() == Color.GREY)
			greyCount++;
		else
			greyCount = Math.max(0, greyCount-1);
    	if(greyCount > 4) {
        	greyCount = 0;
        	Sound.setVolume(100);
        	Sound.beep();
        	return true;
    	}
    	LineDisplayWriter.addLine(""+greyCount, 5);
    	return false;
    }

    public void suppress() {
    	suppressed = true;
    }
    
    public void action() {
    	if(seqPos == sequence.length)
    		seqPos = 0;
    	suppressed = false;
    	switch(sequence[seqPos]) {
    	case FORWARD:
    		for(int i = 0; i < 20 && !suppressed; i++) {
    			try {
    				Thread.sleep(10);
    			} catch (InterruptedException e) {
    				Car.stop();
    				System.exit(1);
    			}
    		}
    		break;
    	case LEFT:
    		Car.forward(lowPower, fullPower);
    		for(int i = 0; i < 50 && !suppressed; i++) {
    			try {
    				Thread.sleep(10);
    			} catch (InterruptedException e) {
    				Car.stop();
    				System.exit(1);
    			}
    		}
    		break;
    	case RIGHT:
    		Car.forward(fullPower, lowPower);
    		for(int i = 0; i < 50 && !suppressed; i++) {
    			try {
    				Thread.sleep(10);
    			} catch (InterruptedException e) {
    				Car.stop();
    				System.exit(1);
    			}
    		}
    		break;
    	}
		seqPos++;
    }
}
