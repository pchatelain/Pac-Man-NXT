package sensors;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class BlackWhiteGreySensor {

   private LightSensor ls; 
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

   public BlackWhiteGreySensor(SensorPort p)
   {
	   ls = new LightSensor(p); 
	   // Use the light sensor as a reflection sensor
	   ls.setFloodlight(true);
   }

   protected int read(String color){
	   
	   int lightValue=0;
	   
	   while (Button.ENTER.isPressed());
	   
	   LCD.clear();
	   LCD.drawString("Press ENTER", 0, 0);
	   LCD.drawString("to calibrate", 0, 1);
	   LCD.drawString(color, 0, 2);
	   while( !Button.ENTER.isPressed() ){
	      lightValue = ls.getLightValue();
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
           return (ls.getLightValue() < blackGreyThreshold);
   }
   
   public int getBlackLightValue() {
	   return blackLightValue;
   } 
   
   public boolean grey() {
	   int value = ls.getLightValue();
       return (value > blackGreyThreshold) && (value < whiteGreyThreshold);
   }

   public int getGreyLightValue() {
	   return greyLightValue;
   } 
   
   public boolean white() {
	   return (ls.getLightValue()> whiteGreyThreshold);
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
	   int value = ls.getLightValue();
	   if (value < blackGreyThreshold)
		   return Color.BLACK;
	   if (value > whiteGreyThreshold)
		   return Color.WHITE;
	   return Color.GREY;
   }
   
   public int light() {
 	   return ls.getLightValue();
   }
   
   public int getLightValue() {
	   return ls.getLightValue();
   }
   
   public int getNormalizedLightValue() {
	   return ((ls.getLightValue() - blackLightValue) * 100 / (whiteLightValue - blackLightValue));
   }
}