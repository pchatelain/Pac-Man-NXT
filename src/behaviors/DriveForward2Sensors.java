package behaviors;

import io.Instructions;
import lejos.nxt.MotorPort;
import lejos.robotics.subsumption.Behavior;
import robot.LineFollower;
import sensors.Color;
import sensors.DoubleSensor;
import tools.Car;

public class DriveForward2Sensors implements Behavior {
	private boolean _suppressed;
	private DoubleSensor sensor;
	
	private int leftPower = 70;
	private int rightPower = 70;
	private int minPower = 70;
	private int maxPower = 78;
	
	private Color leftLastColor = Color.WHITE;
	private Color rightLastColor = Color.WHITE;
	private boolean dot = false;
	
	private static MotorPort leftMotor = MotorPort.C;
    private static MotorPort rightMotor = MotorPort.B;
	
	public DriveForward2Sensors(DoubleSensor sensor) {
		this.sensor = sensor;
		this.dot = false;
	}

	public boolean takeControl() {
		return (LineFollower.instruction == Instructions.START);
	}

	public void suppress() {
		_suppressed = true;// standard practice for suppress methods
	}

	public void action() {
		_suppressed = false;
		sensor.reset();
		int rightSpeed = minPower;
		int leftSpeed = minPower;
		while (!_suppressed) {
			Car.forward(leftSpeed, rightSpeed);
			
			int leftVal = sensor.getRightNormalized();
			rightSpeed = Math.round(minPower+(maxPower-minPower)*(Color.WHITE.value-leftVal)/100);
			int rightVal = sensor.getRightNormalized();
			leftSpeed = Math.round(minPower+(maxPower-minPower)*(Color.WHITE.value-rightVal)/100);
			
			
//			if ((leftPower > maxPower) || (rightPower > maxPower)) {
//				leftPower--;
//				rightPower--;
//			}
//			switch(sensor.getRightColor()) {
//			case BLACK:
//				rightMotor.controlMotor((int) (rightPower - 12), 1);
//				if (rightLastColor != Color.BLACK) {
//					leftPower++;
//					rightLastColor = Color.BLACK;
//				}
//				dot = false;
//				break;
//			case WHITE:
//				rightMotor.controlMotor(rightPower, 1);
//				rightLastColor = Color.WHITE;
//				dot = false;
//				break;
//			case GREY:
//				rightMotor.controlMotor((int) (rightPower - 8), 1);
//				rightLastColor = Color.GREY;
//				if (leftLastColor == Color.GREY && !dot) {
//					Car.stop();
//					dot = true;
//					leftPower = minPower;
//					rightPower = minPower;
//					LineFollower.instruction = Instructions.RIGHT;
//				}
//				break;
//			}
//			switch(sensor.getLeftColor()) {
//			case BLACK:
//				leftMotor.controlMotor((int) (leftPower - 12), 1);
//				if (leftLastColor != Color.BLACK) {
//					rightPower++;
//					leftLastColor = Color.BLACK;
//				}
//				dot = false;
//				break;
//			case WHITE:
//				leftMotor.controlMotor(leftPower, 1);
//				leftLastColor = Color.WHITE;
//				dot = false;
//				break;
//			case GREY:
//				leftMotor.controlMotor((int) (leftPower - 8), 1);
//				leftLastColor = Color.GREY;
//				if (rightLastColor == Color.GREY && !dot) {
//					Car.stop();
//					dot = true;
//					leftPower = minPower;
//					rightPower = minPower;
//					LineFollower.instruction = Instructions.RIGHT;
//				}
//			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Car.stop();
				System.exit(1);
			}
		}
	}
	
	public void speedUp() {
		maxPower++;
	}
	
	public void slowDown() {
		maxPower--;
	}
}
