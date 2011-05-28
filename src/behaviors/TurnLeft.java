package behaviors;

import robot.LineFollower;
import sensors.Color;
import sensors.DoubleSensor;
import tools.Car;
import io.Instructions;
import lejos.nxt.MotorPort;
import lejos.robotics.subsumption.Behavior;

public class TurnLeft implements Behavior {

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
	
	public TurnLeft(DoubleSensor sensor) {
		this.sensor = sensor;
		this.leftPower = 45;
		this.rightPower = 75;
		this.power = 60;
		this.leftLastColor = Color.WHITE;
		this.rightLastColor = Color.WHITE;
		this.dot = false;
	}

	public boolean takeControl() {
		return (LineFollower.instruction == Instructions.LEFT);
	}

	public void suppress() {
		_suppressed = true;
	}

	public void action() {
		_suppressed = false;
		Car.forward(0, rightPower);
		while (!_suppressed) {
			Car.forward(0, rightPower);
			switch(sensor.getRightColor()) {
			case BLACK:
				_suppressed = true;
				break;
			case WHITE:
			case GREY:
				break;
			}
			/*if (leftPower > power)
				leftPower--;*/
			/*if (rightPower < power)
				rightPower++;*/
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
		leftPower = 45;
		rightPower = 75;
	}
}
