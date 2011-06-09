package io;


public class StaticInstructor implements Instructor {
	
	private Instruction[][] sequences = {{
			Instruction.RIGHT, Instruction.RIGHT,
			Instruction.FORWARD, Instruction.LEFT, Instruction.FORWARD, Instruction.RIGHT,
			Instruction.RIGHT, Instruction.FORWARD, Instruction.FORWARD, Instruction.FORWARD, Instruction.FORWARD, 
			Instruction.RIGHT},{
			Instruction.RIGHT, Instruction.FORWARD, Instruction.FORWARD,
			Instruction.RIGHT, Instruction.RIGHT, Instruction.FORWARD, Instruction.FORWARD, Instruction.FORWARD,
			Instruction.LEFT, Instruction.FORWARD, Instruction.FORWARD, Instruction.LEFT, 
			Instruction.FORWARD, Instruction.FORWARD, Instruction.FORWARD, Instruction.FORWARD, Instruction.LEFT,
			Instruction.FORWARD, Instruction.FORWARD, Instruction.FORWARD, Instruction.FORWARD, Instruction.FORWARD,
			Instruction.FORWARD, Instruction.LEFT, Instruction.FORWARD, Instruction.FORWARD, Instruction.LEFT,
			Instruction.FORWARD, Instruction.FORWARD, Instruction.LEFT, Instruction.FORWARD,Instruction.RIGHT,
			Instruction.FORWARD, Instruction.LEFT, Instruction.FORWARD, Instruction.RIGHT,
			Instruction.RIGHT, Instruction.FORWARD, Instruction.FORWARD, Instruction.FORWARD, Instruction.FORWARD, 
			Instruction.RIGHT}};
	
	private int sequence;
	private int sequencePosition = 0;
	
	public StaticInstructor(int sequence) {
		this.sequence = sequence;
	}
	
	public Instruction next() {
    	if(sequencePosition == sequences[sequence].length)
    		sequencePosition = 0;
    	return sequences[sequence][sequencePosition++];
	}
	
	public Instruction peek() {
    	if(sequencePosition == sequences[sequence].length)
    		return sequences[sequence][0];
    	return sequences[sequence][sequencePosition];
	}

}
