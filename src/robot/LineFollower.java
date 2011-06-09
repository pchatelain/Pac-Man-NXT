package robot;
import io.LineDisplayWriter;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import sensors.DoubleSensor;
import behaviors.DriveForward2Sensors;
import behaviors.ExitBehavior;
import behaviors.Turn;

public class LineFollower {
	
	public static boolean motor = true;
	public static void main(String[] args) {
    	Sound.setVolume(80);
    	
		DoubleSensor sensor = new DoubleSensor(SensorPort.S2, SensorPort.S1);
		sensor.calibrate();
		Behavior b1 = new DriveForward2Sensors(sensor);
		Behavior b2 = new Turn(sensor);
		Behavior b3 = new ExitBehavior(sensor);
		Behavior[] behaviorList = {b1, b2, b3};
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		
		LineDisplayWriter.setLine("Press ENTER", 0);
		LineDisplayWriter.setLine("to start", 1);
		LineDisplayWriter.refresh();
		while(!Button.ENTER.isPressed());
		LineDisplayWriter.clear();
		
		arbitrator.start();
	}
}
