package behaviors;

import lejos.nxt.Button;
import lejos.robotics.subsumption.Behavior;
import sensors.DoubleSensor;
import tools.Car;

public class ExitBehavior implements Behavior {

	public ExitBehavior(DoubleSensor sensor) {
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
		System.exit(0);
	}
}
