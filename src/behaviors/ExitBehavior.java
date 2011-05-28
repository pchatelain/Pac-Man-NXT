package behaviors;

import sensors.DoubleSensor;
import tools.Car;
import lejos.nxt.Button;
import lejos.robotics.subsumption.Behavior;

public class ExitBehavior implements Behavior {

	private DoubleSensor sensor;
	
	public ExitBehavior(DoubleSensor sensor) {
		this.sensor = sensor;
	}

	public boolean takeControl() {
		return Button.ESCAPE.isPressed();
	}

	public void suppress() {
		// Since this is highest priority behavior, suppress will never be
		// called.
	}

	public void action() {
		Car.stop();
		sensor.close();
		System.exit(0);
	}
}
