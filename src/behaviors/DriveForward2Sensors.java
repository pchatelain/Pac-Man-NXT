package behaviors;

import lejos.robotics.subsumption.Behavior;
import sensors.DoubleSensor;
import tools.Car;

public class DriveForward2Sensors implements Behavior {
	private boolean suppressed;
	private DoubleSensor sensor;
	
	private int minPower = 75;
	private int maxPower = 90;
	
	public DriveForward2Sensors(DoubleSensor sensor) {
		this.sensor = sensor;
	}

	public boolean takeControl() {
		return true;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		int meanPower = maxPower-(maxPower-minPower)/2;
		while (!suppressed) {
			int balance = sensor.getRightNormalized()-sensor.getLeftNormalized();
			int leftPower = meanPower-(maxPower-minPower)*balance/130;
			int rightPower = meanPower+(maxPower-minPower)*balance/130;
			Car.forward(leftPower, rightPower);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				Car.stop();
				System.exit(1);
			}
		}
	}
}
