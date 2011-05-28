package robot;
import io.Instructions;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import sensors.DoubleSensor;
import behaviors.*;

public class LineFollower {
	
	public static int instruction;

	public static void main(String[] args) {
		instruction = Instructions.START;
		DoubleSensor sensor = new DoubleSensor(SensorPort.S2, SensorPort.S1);
		sensor.calibrate();
		Behavior b1 = new DriveForward2Sensors(sensor);
		Behavior b2 = new Turn(sensor);
		Behavior b3 = new ExitBehavior(sensor);
		Behavior[] behaviorList = {b1, b2, b3};
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		arbitrator.start();
	}
}
