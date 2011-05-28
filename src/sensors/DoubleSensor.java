package sensors;

import tools.Car;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class DoubleSensor {
	private BlackWhiteGreySensor rightSensor;
	private BlackWhiteGreySensor leftSensor;

	public DoubleSensor(SensorPort leftPort, SensorPort rightPort) {
		leftSensor = new BlackWhiteGreySensor(leftPort);
		rightSensor = new BlackWhiteGreySensor(rightPort);
	}

	public void calibrate() {

		int leftValue=0;
		int rightValue=0;

		while (Button.ENTER.isPressed());

		LCD.clear();
		LCD.drawString("Press ENTER", 0, 0);
		LCD.drawString("to calibrate", 0, 1);
		LCD.drawString("black", 0, 2);
		while( !Button.ENTER.isPressed() ){
			leftValue = leftSensor.getLightValue();
			rightValue = rightSensor.getLightValue();
			LCD.drawInt(leftValue, 2, 0, 3);
			LCD.drawInt(rightValue, 2, 10, 3);
			LCD.refresh();
		}
		leftSensor.setBlackLightValue(leftValue);
		rightSensor.setBlackLightValue(rightValue);
		
		while (Button.ENTER.isPressed());

		LCD.clear();
		LCD.drawString("Press ENTER", 0, 0);
		LCD.drawString("to calibrate", 0, 1);
		LCD.drawString("white", 0, 2);
		while( !Button.ENTER.isPressed() ){
			leftValue = leftSensor.getLightValue();
			rightValue = rightSensor.getLightValue();
			LCD.drawInt(leftValue, 2, 0, 3);
			LCD.drawInt(rightValue, 2, 10, 3);
			LCD.refresh();
		}
		leftSensor.setWhiteLightValue(leftValue);
		rightSensor.setWhiteLightValue(rightValue);
		leftSensor.computeThresholds();
		rightSensor.computeThresholds();
	}
	
	public void autoCalibrate() {
		int minLeft = Integer.MAX_VALUE;
		int maxLeft = 0;
		int minRight = Integer.MAX_VALUE;
		int maxRight = 0;
		int lightValue;
		
		LCD.drawString("Calibrating...", 0, 0);
		Car.forward(70, 0);
		
		for (int i = 0; i < 50; i++) {
			lightValue = leftSensor.getLightValue();
			if (lightValue < minLeft)
				minLeft = lightValue;
			if (lightValue > maxLeft)
				maxLeft = lightValue;
			lightValue = rightSensor.getLightValue();
			if (lightValue < minRight)
				minRight = lightValue;
			if (lightValue > maxRight)
				maxRight = lightValue;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Car.stop();
				System.exit(1);
			}
		}
	
		Car.stop();
		
		LCD.drawInt(minLeft, 2, 0, 3);
		LCD.drawInt(minRight, 2, 10, 3);
		LCD.drawInt(maxLeft, 2, 0, 4);
		LCD.drawInt(maxRight, 2, 10, 4);
		
		leftSensor.setBlackLightValue(minLeft);
		rightSensor.setBlackLightValue(minRight);
		leftSensor.setWhiteLightValue(maxLeft);
		rightSensor.setWhiteLightValue(maxRight);
		leftSensor.computeThresholds();
		rightSensor.computeThresholds();
		
	}

	public Color getLeftColor() {
		return leftSensor.getColor();
	}
	
	public Color getRightColor() {
		return rightSensor.getColor();
	}
	
	public void reset() {
		leftSensor.reset();
		rightSensor.reset();
	}
	
	public void close() {
		leftSensor.close();
		rightSensor.close();
	}
	
}
	
