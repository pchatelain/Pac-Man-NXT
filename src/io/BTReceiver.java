package io;

import java.io.IOException;
import java.io.InputStream;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class BTReceiver implements Instructor {

	public static final int U_TURN = 255;
	public static final int RIGHT = 254;
	public static final int LEFT = 253;
	public static final int DOT = 252;
	public static final int START = 251;
	public static final int STOP = 250;
	
	private InputStream in;
	
	public boolean waitForConnection(int timeout) {
		NXTConnection connection = Bluetooth.waitForConnection(timeout, 0);
		if(connection == null)
			return false;
		in = connection.openInputStream();
		return true;
	}
	
	private Instruction nextInstruction = Instruction.NONE;
	
	public Instruction next() {
		if(nextInstruction == Instruction.NONE)
			peek();
		return nextInstruction;
	}
	
	public Instruction peek() {
		try {
			if (in.available() == 0)
				return Instruction.NONE;
			switch (in.read()) {
			case -1: return nextInstruction = Instruction.EOT;
			case U_TURN: return nextInstruction = Instruction.U_TURN;
			case RIGHT: return nextInstruction = Instruction.RIGHT;
			case LEFT: return nextInstruction = Instruction.LEFT;
			case DOT: return nextInstruction = Instruction.DOT;
			case START: return nextInstruction = Instruction.START;
			case STOP: return nextInstruction = Instruction.STOP;
			default: return nextInstruction = Instruction.UNKNOWN;
			}
		} catch (IOException e) {
			System.exit(1);
		}
		return Instruction.NONE;
	}
	
	public void close() throws IOException {
		in.close();
	}
	
}
