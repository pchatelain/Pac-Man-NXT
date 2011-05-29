package sensors;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class BlackWhiteSensor {

   private LightSensor sensor; 
   public int blackLightValue;
   public int whiteLightValue;
   
   public BlackWhiteSensor(SensorPort p) {
	   sensor = new LightSensor(p); 
	   sensor.setFloodlight(true);
   }
   
   public int getLightValue() {
	   return sensor.getLightValue();
   }
   
   public int getNormalizedLightValue() {
	   return ((sensor.getLightValue() - blackLightValue) * 100 / (whiteLightValue - blackLightValue));
   }
}