package tools;

import io.LineDisplayWriter;
import sensors.BlackWhiteGreySensor;
import lejos.nxt.*;
/**
 * A simple line follower for the LEGO 9797 car with
 * a light sensor. Before the car is started on a line
 * a BlackWhiteSensor is calibrated to adapt to different
 * light conditions and colors.
 * 
 * The light sensor should be connected to port 3. The
 * left motor should be connected to port C and the right 
 * motor to port B.
 * 
 * @author  Ole Caprani
 * @version 23.08.07
 */
public class LineFollowerPID implements Configurable
{
	private static double Kp 	= 6.0d;
	private static double Ki 	= 0.3d;
	private static double Kd 	= 11.0d;
	private static double Tp 	= 100.0d;
	
  public static void main (String[] aArg)
  throws Exception
  {
     BlackWhiteGreySensor sensor = new BlackWhiteGreySensor(SensorPort.S3);
     String[] settings = new String[4];
     settings[0] = "prop.";
     settings[1] = "integr.";
     settings[2] = "deriv.";
     settings[3] = "base";
	 Configurator conf = new Configurator(new LineFollowerPID(), settings);
     sensor.calibrate();
	 conf.listen();
     LCD.clear();
     LCD.drawString("Light: ", 0, 2);
     
     // Initialize control variables
     int offset 		= sensor.getBlackGreyThreshold();
     
     double integral 	= 0.0d;
     double lastError 	= 0.0d;
     double derivative 	= 0.0d;
     double error;
     double lightValue;
     double turn;
     int powerLeft, powerRight;
	 
     while (! Button.ESCAPE.isPressed()) {
	     
	     // Sample the world
	     lightValue = (double)sensor.light();
	     
	     // Calculate PID
	     error = lightValue - offset;
	     integral = integral + error;
	     if (integral > 100) integral = 100;
	     else if (integral < -100) integral = -100;
	     derivative = error - lastError;
	     
	     // Calculate power
	     turn = Kp*error + Ki*integral + Kd*derivative;
	     if (turn > 0) {
	    	 powerLeft = (int)(Tp);
	    	 powerRight = (int)(Tp - turn);
	     } else {
	    	 powerLeft = (int)(Tp + turn);
	    	 powerRight = (int)(Tp);
	     }
	     LineDisplayWriter.addLine("Deriv.: " + derivative, 1);
	     LineDisplayWriter.addLine("  Err.: " + error, 2);
	     LineDisplayWriter.addLine("  Turn: " + turn, 3);
	     LineDisplayWriter.addLine(" Power: " + powerLeft + "/" + powerRight, 4);
	     LineDisplayWriter.refresh();
	     
	     Car.forward(powerLeft, powerRight);
	     
	     // Update last error
	     lastError = error;
	     
	     Thread.sleep(5);
     }
     Car.stop();
     conf.unlisten();
     LCD.clear();
     LCD.drawString("Program stopped", 0, 0);
     LCD.refresh();
   }
  
  	public void increaseSetting(int setting) {
  		switch (setting) {
  		case 0 :
  			Kp += 0.1;
  			break;
  		case 1 :
  			Ki += 0.1;
  			break;
  		case 2 :
  			Kd += 0.1;
  			break;
  		case 3 :
  			Tp += 2;
  			break;
  		}
  	}
	public void decreaseSetting(int setting) {
		switch (setting) {
  		case 0 :
  			Kp -= 0.1;
  			break;
  		case 1 :
  			Ki -= 0.1;
  			break;
  		case 2 :
  			Kd -= 0.1;
  			break;
  		case 3 :
  			Tp -= 2;
  			break;
  		}
	}
	public String getSettingValue(int setting) {
		switch (setting) {
  		case 0 :
  			return Double.toString(Kp);
  		case 1 :
  			return Double.toString(Ki);
  		case 2 :
  			return Double.toString(Kd);
  		case 3 :
  			return Double.toString(Tp);
  		default: 
  			return "";
  		}
	}
}