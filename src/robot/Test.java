package robot;
import io.LineDisplayWriter;
import lejos.nxt.ColorLightSensor;
import lejos.nxt.SensorPort;


public class Test {

	public static void main(String[] args) {
		ColorLightSensor s = new ColorLightSensor(SensorPort.S2, ColorLightSensor.TYPE_REFLECTION);
		s.setFloodlight(true);
		LineDisplayWriter.writeLine("TYPE_REFLECTION", 1);
		double t = System.currentTimeMillis();
		for (int i = 1; i < 5; i++) {
			LineDisplayWriter.writeLine(""+s.readValue(), i+1);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
		t = (System.currentTimeMillis() - t) / 1000;
		LineDisplayWriter.writeLine(t+" seconds", 7);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
	}
}