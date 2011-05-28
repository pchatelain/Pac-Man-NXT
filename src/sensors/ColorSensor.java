package sensors;

import lejos.nxt.Button;
import lejos.nxt.ColorLightSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class ColorSensor {
	private ColorLightSensor ls; 
	   private int blackLightValue;
	   private int greyLightValue;
	   private int whiteLightValue;
	   private int blackGreyThreshold;
	   private int whiteGreyThreshold;

	   protected void setBlackLightValue(int blackLightValue) {
		   this.blackLightValue = blackLightValue;
	   }

	   protected void setWhiteLightValue(int whiteLightValue) {
		   this.whiteLightValue = whiteLightValue;
	   }
	   
	   public ColorSensor(SensorPort p)
	   {
		   ls = new ColorLightSensor(p, ColorLightSensor.TYPE_REFLECTION); 
		   // Use the light sensor as a reflection sensor
		   ls.setFloodlight(true);
	   }

	   private int read(String color){
		   
		   int lightValue=0;
		   
		   while (Button.ENTER.isPressed());
		   
		   LCD.clear();
		   LCD.drawString("Press ENTER", 0, 0);
		   LCD.drawString("to calibrate", 0, 1);
		   LCD.drawString(color, 0, 2);
		   while( !Button.ENTER.isPressed() ){
		      lightValue = ls.readValue();
		      LCD.drawInt(lightValue, 4, 10, 2);
		      LCD.refresh();
		   }
		   return lightValue;
	   }
	   
	   public void calibrate()
	   {
		   blackLightValue = read("black");
		   greyLightValue = read("grey");
		   whiteLightValue = read("white");
		   // The threshold is calculated as the median between 
		   // the two readings over the two types of surfaces
		   if ((blackLightValue > greyLightValue) || (greyLightValue > whiteLightValue)) {
			   System.err.println("Color information incoherent. Quitting.");
			   System.exit(1);
		   }
		   blackGreyThreshold = (blackLightValue+greyLightValue)/2;
		   whiteGreyThreshold = (whiteLightValue+greyLightValue)/2;
	   }
	   
	   public void computeThresholds()
	   {
		   if (blackLightValue >= whiteLightValue) {
			   System.err.println("Color information incoherent. Quitting.");
			   System.exit(1);
		   }
		   greyLightValue = (blackLightValue + whiteLightValue) / 2;
		   blackGreyThreshold = (blackLightValue+greyLightValue)/2;
		   whiteGreyThreshold = (whiteLightValue+greyLightValue)/2;
	   }
	   
	   public boolean black() {
	           return (ls.readValue() < blackGreyThreshold);
	   }
	   
	   public int getBlackLightValue() {
		   return blackLightValue;
	   } 
	   
	   public boolean grey() {
		   int value = ls.readValue();
	       return (value > blackGreyThreshold) && (value < whiteGreyThreshold);
	   }

	   public int getGreyLightValue() {
		   return greyLightValue;
	   } 
	   
	   public boolean white() {
		   return (ls.readValue()> whiteGreyThreshold);
	   }
	   
	   public int getWhiteLightValue() {
		   return whiteLightValue;
	   }
	   
	   public int getBlackGreyThreshold() {
		   return blackGreyThreshold;
	   }
	   
	   public int getWhiteGreyThreshold() {
		   return whiteGreyThreshold;
	   }
	   
	   public Color getColor() {
		   int value = ls.readValue();
		   if (value < blackGreyThreshold)
			   return Color.BLACK;
		   if (value > whiteGreyThreshold)
			   return Color.WHITE;
		   return Color.GREY;
	   }
	   
	   public int light() {
	 	   return ls.readValue();
	   }
	   
	   public int getLightValue() {
		   return ls.readValue();
	   }
}
