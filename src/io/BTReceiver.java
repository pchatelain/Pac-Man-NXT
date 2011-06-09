package io;

import java.io.IOException;
import java.io.InputStream;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class BTReceiver implements Instructor {
	
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
			case 255: return nextInstruction = Instruction.U_TURN;
			case 254: return nextInstruction = Instruction.RIGHT;
			case 253: return nextInstruction = Instruction.LEFT;
			case 252: return nextInstruction = Instruction.DOT;
			case 251: return nextInstruction = Instruction.START;
			case 250: return nextInstruction = Instruction.STOP;
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
