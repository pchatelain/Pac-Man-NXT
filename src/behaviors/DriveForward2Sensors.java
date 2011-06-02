package behaviors;

import io.LineDisplayWriter;
import lejos.robotics.subsumption.Behavior;
import robot.LineFollower;
import sensors.DoubleSensor;
import tools.Car;
import tools.Configurable;
import tools.Configurator;

public class DriveForward2Sensors implements Behavior, Configurable {
	private boolean suppressed;
	private DoubleSensor sensor;
	
	public DriveForward2Sensors(DoubleSensor sensor) {
		this.sensor = sensor;
	}

	public boolean takeControl() {
		return true;
	}

	public void suppress() {
		suppressed = true;
	}

	private int minPower = 75;
	private int maxPower = 90;
	public void action() {
		suppressed = false;
		int meanPower = maxPower-(maxPower-minPower)/2;
		while (!suppressed) {
			int balance = sensor.getRightNormalized()-sensor.getLeftNormalized();
			int leftPower = (int) (meanPower-(maxPower-minPower)*balance/130);
			int rightPower =(int) (meanPower+(maxPower-minPower)*balance/130);
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
