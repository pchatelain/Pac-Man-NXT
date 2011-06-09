package robot;
import io.BTReceiver;
import io.Instruction;
import io.Instructor;
import io.LineDisplayWriter;
import io.StaticInstructor;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import sensors.DoubleSensor;
import behaviors.DriveForward2Sensors;
import behaviors.ExitBehavior;
import behaviors.Turn;

public class Robot {
	
	public static void main(String[] args) {
    	Sound.setVolume(80);
    	
		DoubleSensor sensor = new DoubleSensor(SensorPort.S2, SensorPort.S1);
		sensor.calibrate();
		
		BTReceiver receiver = new BTReceiver();
		
		LineDisplayWriter.setLine("Press ENTER to", 0);
		LineDisplayWriter.setLine("start or wait for", 1);
		LineDisplayWriter.setLine("Bluetooth conn.", 2);
		LineDisplayWriter.refresh();
		boolean bluetooth = false;
		while(!Button.ENTER.isPressed() || (bluetooth = receiver.waitForConnection(100)));
		LineDisplayWriter.clear();
		
		Instructor instructor; 
		if(bluetooth) {
			while(!receiver.next().equals(Instruction.START));
			instructor = receiver;
		} else {
			instructor = new StaticInstructor(0);
		}
		
		Behavior b1 = new DriveForward2Sensors(sensor);
		Behavior b2 = new Turn(sensor, instructor);
		Behavior b3 = new ExitBehavior(sensor);
		Behavior[] behaviorList = {b1, b2, b3};
		
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		arbitrator.start();
	}
}
