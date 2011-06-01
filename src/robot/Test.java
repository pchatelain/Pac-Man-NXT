package robot;

import io.BTReceiver;
import io.LineDisplayWriter;
import io.Instruction;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import sensors.DoubleSensor;
import behaviors.DriveForward2Sensors;
import behaviors.ExitBehavior;
import behaviors.Turn2;


public class Test {

	public static void main(String[] args) {
		Sound.setVolume(80);

		DoubleSensor sensor = new DoubleSensor(SensorPort.S2, SensorPort.S1);
		sensor.calibrate();
		
		BTReceiver receiver = new BTReceiver();
		
		Behavior b1 = new DriveForward2Sensors(sensor);
		Behavior b2 = new Turn2(sensor, receiver);
		Behavior b3 = new ExitBehavior(sensor);
		Behavior[] behaviorList = {b1, b2, b3};
		Arbitrator arbitrator = new Arbitrator(behaviorList);

		LineDisplayWriter.setLine("Waiting for first instruction", 0);
		LineDisplayWriter.setLine("to start", 1);
		LineDisplayWriter.refresh();
		while(!Button.ENTER.isPressed());
		while (!receiver.nextInstruction().equals(Instruction.START));
		LineDisplayWriter.clear();

		arbitrator.start();
	}
}