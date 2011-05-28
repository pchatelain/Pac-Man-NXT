package tools;
import io.Instructions;
import lejos.nxt.*;
/**
 * A locomotion module with methods to drive
 * a car with two independent motors. The left motor 
 * should be connected to port C and the right motor
 * to port B.
 *  
 * @author  Ole Caprani
 * @version 17.4.08
 */
public class Car 
{
    // Commands for the motors
    private final static int forward  = 1,
                             backward = 2,
                             stop     = 3;
	                         
    private static MotorPort leftMotor = MotorPort.C;
    private static MotorPort rightMotor= MotorPort.B;
    
    private static int speed;
    
    private Car()
    {	   
    } 
   
    public static void stop() 
    {
	    leftMotor.controlMotor(0,stop);
	    rightMotor.controlMotor(0,stop);
    }
   
    public static void forward(int leftPower, int rightPower)
    {
	    leftMotor.controlMotor(leftPower,forward);
	    rightMotor.controlMotor(rightPower,forward);
    }
   
    public static void backward(int leftPower, int rightPower)
    {
	    leftMotor.controlMotor(leftPower,backward);
	    rightMotor.controlMotor(rightPower,backward);
    }
    
    public static void followInstruction(int inst) {
    	switch (inst) {
    	case Instructions.DOT: break; // TODO
    	case Instructions.LEFT: forward(0, speed); break;
    	case Instructions.RIGHT: forward(speed, 0); break;
    	case Instructions.START: forward(speed, speed); break;
    	case Instructions.STOP: stop(); break;
    	case Instructions.U_TURN: break; // TODO
    	default: if (inst <= 100) speed = inst; forward(speed, speed);
    	}
    }
}