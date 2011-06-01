package io;

import java.io.IOException;
import java.io.InputStream;

import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class BTReceiver {

	public static final int U_TURN = 255;
	public static final int RIGHT = 254;
	public static final int LEFT = 253;
	public static final int DOT = 252;
	public static final int START = 251;
	public static final int STOP = 250;
	
	private InputStream in;
	
	public BTReceiver() {
		LCD.drawString("Waiting for connection", 1, 1);
		NXTConnection connection = Bluetooth.waitForConnection();
		in = connection.openInputStream();
	}
	
	public Instruction nextInstruction() throws IOException {
		switch (in.read()) {
		case U_TURN: return Instruction.U_TURN;
		case RIGHT: return Instruction.RIGHT;
		case LEFT: return Instruction.LEFT;
		case DOT: return Instruction.DOT;
		case START: return Instruction.START;
		case STOP: return Instruction.STOP;
		default: return Instruction.UNKNOWN;
		}
	}
	
	public void close() throws IOException {
		in.close();
	}
	
}
