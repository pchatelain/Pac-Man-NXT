package behaviors;

import io.Instructions;
import io.LineDisplayWriter;
import lejos.robotics.subsumption.Behavior;
import robot.LineFollower;
import sensors.Color;
import sensors.DoubleSensor;
import tools.Car;

public class TurnRight implements Behavior {

	private boolean _suppressed;
	private DoubleSensor sensor;
	private int leftPower;
	private int rightPower;
	
    public TurnRight(DoubleSensor sensor) {
    	this.sensor = sensor;
    	leftPower = 100;
    	rightPower = -40;
    }

    public boolean takeControl() {
    	return (LineFollower.instruction == Instructions.RIGHT);
    }

    public void suppress() {
    	_suppressed = true;
    }
    
    int calls = 0;
    public void action() {
    	_suppressed = false;
    	int greyCount = 0;
    	for(int i = 0; i < 10 && !_suppressed; i++) {
    		if(sensor.getRightColor() == Color.GREY && sensor.getLeftColor() == Color.GREY)
    			greyCount++;
    		if(greyCount > 5) {
        		Car.forward(leftPower, rightPower);
    			try {
    				Thread.sleep(500);
    			} catch (InterruptedException e) {
    				Car.stop();
    				System.exit(1);
    			}
    			break;
    		}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Car.stop();
				System.exit(1);
			}
    	}
		Car.forward(leftPower, leftPower);
    	LineFollower.instruction = Instructions.START;
    }
}
