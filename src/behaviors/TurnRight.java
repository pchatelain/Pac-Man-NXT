package behaviors;

import robot.LineFollower;
import sensors.Color;
import sensors.DoubleSensor;
import tools.Car;
import io.Instructions;
import lejos.nxt.MotorPort;
import lejos.robotics.subsumption.Behavior;

public class TurnRight implements Behavior {

	private boolean _suppressed;
	private DoubleSensor sensor;
	private int leftPower;
	private int rightPower;
	private int power;
	private Color leftLastColor;
	private Color rightLastColor;
	private boolean dot;
	
	private static MotorPort leftMotor = MotorPort.C;
    private static MotorPort rightMotor = MotorPort.B;
	
    public TurnRight(DoubleSensor sensor) {
    	this.sensor = sensor;
    	this.leftPower = 75;
    	this.rightPower = 45;
    	this.power = 60;
    	this.leftLastColor = Color.WHITE;
    	this.rightLastColor = Color.WHITE;
    	this.dot = false;
    }

    public boolean takeControl() {
    	return (LineFollower.instruction == Instructions.RIGHT);
    }

    public void suppress() {
    	_suppressed = true;
    }

    public void action() {
    	_suppressed = false;
    	Car.forward(leftPower, 0);
    	while (!_suppressed) {
    		Car.forward(leftPower, 0);
    		switch(sensor.getLeftColor()) {
    		case BLACK:
    			_suppressed = true;
    			break;
    		case WHITE:
    		case GREY:
    			break;
    		}
    		/*switch(sensor.getRightColor()) {
    		case BLACK:
    			_suppressed = true;
    			break;
    		case WHITE:
    		case GREY:
    			break;
    		}*/
    		if (!_suppressed) {
    			try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				Car.stop();
    				System.exit(1);
    			}
    		}
    	}
    	LineFollower.instruction = Instructions.START;
    	leftPower = 75;
    	rightPower = 45;
    }
}
