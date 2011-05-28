package behaviors;

import io.Instructions;
import lejos.robotics.subsumption.Behavior;
import robot.LineFollower;
import sensors.Color;
import sensors.DoubleSensor;
import tools.Car;

public class DriveForward2Sensors implements Behavior {
	private boolean suppressed;
	private DoubleSensor sensor;
	
	private int leftPower = 70;
	private int rightPower = 70;
	private int minPower = 75;
	private int maxPower = 90;
	
	public DriveForward2Sensors(DoubleSensor sensor) {
		this.sensor = sensor;
	}

	public boolean takeControl() {
		return true;
	}

	public void suppress() {
		suppressed = true;// standard practice for suppress methods
	}

	public void action() {
		suppressed = false;
		int meanPower = maxPower-(maxPower-minPower)/2;
		while (!suppressed) {
			Car.forward(leftPower, rightPower);
			if(sensor.getRightColor() == Color.GREY
			&& sensor.getLeftColor() == Color.GREY) {
				LineFollower.instruction = Instructions.RIGHT;
				break;
			} else {
				int balance = sensor.getRightNormalized()-sensor.getLeftNormalized();
				leftPower = meanPower-(maxPower-minPower)*balance/130;
				rightPower = meanPower+(maxPower-minPower)*balance/130;
			}
			
			try {
				Thread.sleep(5);
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
