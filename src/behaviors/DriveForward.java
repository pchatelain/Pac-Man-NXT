package behaviors;

import io.Instructions;
import lejos.nxt.MotorPort;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import robot.LineFollower;
import sensors.Color;
import sensors.DoubleSensor;
import tools.Car;

public class DriveForward implements Behavior {
	private boolean _suppressed;
	private DoubleSensor sensor;
	private int leftPower;
	private int rightPower;
	private int minPower;
	private int maxPower;
	private Color leftLastColor;
	private Color rightLastColor;
	private boolean dot;
	
	private static MotorPort leftMotor = MotorPort.C;
    private static MotorPort rightMotor = MotorPort.B;
	
	public DriveForward(DoubleSensor sensor) {
		this.sensor = sensor;
		this.leftPower = 75;
		this.rightPower = 75;
		this.minPower = 70;
		this.maxPower = 80;
		this.leftLastColor = Color.WHITE;
		this.rightLastColor = Color.WHITE;
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
		Car.forward(minPower, minPower);
		while (!_suppressed) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Car.stop();
				System.exit(1);
			}
			if ((leftPower > maxPower) || (rightPower > maxPower)) {
				leftPower--;
				rightPower--;
			}
			switch(sensor.getRightColor()) {
			case BLACK:
				rightMotor.controlMotor((int) (rightPower - 13), 1);
				if (rightLastColor != Color.BLACK) {
					leftPower++;
					rightLastColor = Color.BLACK;
				}
				dot = false;
				break;
			case WHITE:
				rightMotor.controlMotor(rightPower, 1);
				rightLastColor = Color.WHITE;
				dot = false;
				break;
			case GREY:
				rightMotor.controlMotor((int) (rightPower - 8), 1);
				rightLastColor = Color.GREY;
				if (leftLastColor == Color.GREY && !dot) {
					Car.stop();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					
					}
					dot = true;
					LineFollower.instruction = Instructions.RIGHT;
				}
				break;
			}
			switch(sensor.getLeftColor()) {
			case BLACK:
				leftMotor.controlMotor((int) (leftPower - 13), 1);
				if (leftLastColor != Color.BLACK) {
					rightPower++;
					leftLastColor = Color.BLACK;
				}
				dot = false;
				break;
			case WHITE:
				leftMotor.controlMotor(leftPower, 1);
				leftLastColor = Color.WHITE;
				dot = false;
				break;
			case GREY:
				leftMotor.controlMotor((int) (leftPower - 8), 1);
				leftLastColor = Color.GREY;
				if (rightLastColor == Color.GREY && !dot) {
					Car.stop();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					
					}
					dot = true;
					LineFollower.instruction = Instructions.LEFT;
				}
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