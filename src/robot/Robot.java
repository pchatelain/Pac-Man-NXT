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
		
		LineDisplayWriter.setLine("Press <ENTER> to", 0);
		LineDisplayWriter.setLine("start.", 1);
		LineDisplayWriter.setLine("Press <LEFT> for", 2);
		LineDisplayWriter.setLine("Bluetooth conn.", 3);
		LineDisplayWriter.refresh();
		boolean bluetooth = false;
		while(!Button.ENTER.isPressed() && !(bluetooth = Button.LEFT.isPressed()));
		LineDisplayWriter.clear();
		
		Instructor instructor; 
		if(bluetooth) {
			boolean escape = false;
			LineDisplayWriter.setLine("Waiting for", 0);
			LineDisplayWriter.setLine("Bluetooth conn.", 1);
			LineDisplayWriter.setLine("Press <ESCAPE>", 2);
			LineDisplayWriter.setLine("to cancel.", 3);
			LineDisplayWriter.refresh();
			while(!(escape = Button.ESCAPE.isPressed()) || receiver.waitForConnection(100));
			if(escape)
				System.exit(0);
			while(!receiver.next().equals(Instruction.START));
			instructor = receiver;
		} else {
			instructor = new StaticInstructor(1);
			for(int i = 3; i > 0; i--) {
				Sound.playTone(800, 200, 40);
				LineDisplayWriter.setLine("Starting in "+i+"s", 0, true);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.exit(1);
				}
			}
			Sound.playTone(800, 800, 40);
			LineDisplayWriter.clear();
		}
		
		Behavior b1 = new DriveForward2Sensors(sensor);
		Behavior b2 = new Turn(sensor, instructor);
		Behavior b3 = new ExitBehavior(sensor);
		Behavior[] behaviorList = {b1, b2, b3};
		
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		arbitrator.start();
	}
}
