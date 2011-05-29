package behaviors;

import io.LineDisplayWriter;
import lejos.robotics.subsumption.Behavior;
import sensors.DoubleSensor;
import tools.Car;
import tools.Configurable;
import tools.Configurator;

public class DriveForward2Sensors implements Behavior, Configurable {
	private boolean suppressed;
	private DoubleSensor sensor;
	
	public DriveForward2Sensors(DoubleSensor sensor) {
		this.sensor = sensor;
		String[] settings = {"prop", "integr", "deriv", "base", "motor"};
    	Configurator conf = new Configurator(this, settings);
    	conf.listen();
	}

	public boolean takeControl() {
		return true;
	}

	public void suppress() {
		suppressed = true;
	}
	
	private double Kp = 0.6d;
	private double Ki = 0.7d;
	private double Kd = 0.5d;
	private int Tp = 90;
	private boolean motor = false;
	public void action() {
		suppressed = false;
		
		int error = 0;
		int integral = 0;
		int derivative = 0;
		int lastError = 0;
		int leftPower = 0;
		int rightPower = 0;
		while (!suppressed) {
			int left = sensor.getLeftNormalized();
			int right = sensor.getRightNormalized();
			error = right-left;
			integral += error;
			if(integral > 100)
				integral = 100;
			if(integral < 100)
				integral = -100;
			
			derivative = error-lastError;
			int correction = (int) Math.abs(Kp*left + Ki*integral + Kd*derivative);
			if(left > right) {
				leftPower = Tp;
				rightPower = Tp - correction;
			} else {
				leftPower = Tp - correction;
				rightPower = Tp;
			}
			lastError = error;
			LineDisplayWriter.setLine("Error: "+right+" "+left, 4);
			LineDisplayWriter.setLine("Power: "+rightPower+" "+leftPower, 5);
			if(motor)
				Car.forward(leftPower, rightPower);
			else
				Car.stop();
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				Car.stop();
				System.exit(1);
			}
		}
	}
	
  	public void increaseSetting(int setting) {
  		switch (setting) {
  		case 0:
  			Kp += 0.1;
  			break;
  		case 1:
  			Ki += 0.1;
  			break;
  		case 2:
  			Kd += 0.1;
  			break;
  		case 3:
  			Tp += 2;
  			break;
  		case 4:
  			motor = !motor;
  			break;
  		}
  	}
	public void decreaseSetting(int setting) {
		switch (setting) {
  		case 0:
  			Kp -= 0.1;
  			break;
  		case 1:
  			Ki -= 0.1;
  			break;
  		case 2:
  			Kd -= 0.1;
  			break;
  		case 3:
  			Tp -= 2;
  			break;
  		case 4:
  			motor = !motor;
  			break;
  		}
	}
	public String getSettingValue(int setting) {
		switch (setting) {
  		case 0:
  			return Double.toString(Kp);
  		case 1:
  			return Double.toString(Ki);
  		case 2:
  			return Double.toString(Kd);
  		case 3:
  			return Double.toString(Tp);
  		case 4:
  			if(motor)
  				return "On";
  			return "Off";
  		default: 
  			return "";
  		}
	}
}
