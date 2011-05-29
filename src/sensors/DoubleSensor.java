package sensors;

import io.LineDisplayWriter;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;

public class DoubleSensor {
	private BlackWhiteSensor rightSensor;
	private BlackWhiteSensor leftSensor;

	public DoubleSensor(SensorPort leftPort, SensorPort rightPort) {
		leftSensor = new BlackWhiteSensor(leftPort);
		rightSensor = new BlackWhiteSensor(rightPort);
	}

	public void calibrate() {

		int leftValue=0;
		int rightValue=0;
		
		while (Button.ENTER.isPressed());
		
		LineDisplayWriter.clear();
		LineDisplayWriter.setLine("Press ENTER", 0);
		LineDisplayWriter.setLine("to calibrate", 1);
		LineDisplayWriter.setLine("black", 2);
		while( !Button.ENTER.isPressed() ){
			leftValue = leftSensor.getLightValue();
			rightValue = rightSensor.getLightValue();
			LineDisplayWriter.setLine(leftValue+"   "+rightValue, 3);
			LineDisplayWriter.refresh();
		}
		leftSensor.blackLightValue = leftValue;
		rightSensor.blackLightValue = rightValue;
		
		while (Button.ENTER.isPressed());
		
		LineDisplayWriter.clear();
		LineDisplayWriter.setLine("Press ENTER", 0);
		LineDisplayWriter.setLine("to calibrate", 1);
		LineDisplayWriter.setLine("white", 2);
		while( !Button.ENTER.isPressed() ){
			leftValue = leftSensor.getLightValue();
			rightValue = rightSensor.getLightValue();
			LineDisplayWriter.setLine(leftValue+"   "+rightValue, 3);
			LineDisplayWriter.refresh();
		}
		leftSensor.whiteLightValue = leftValue;
		rightSensor.whiteLightValue = rightValue;
		
		while (Button.ENTER.isPressed());
		LineDisplayWriter.clear();
	}
	
	public int getLeftNormalized() {
		return leftSensor.getNormalizedLightValue();
	}
	
	public int getRightNormalized() {
		return rightSensor.getNormalizedLightValue();
	}
}
	
