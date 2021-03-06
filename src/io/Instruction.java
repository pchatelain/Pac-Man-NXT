package io;

public enum Instruction {
	START, STOP, RIGHT, LEFT, FORWARD, DOT, U_TURN, UNKNOWN, NONE, EOT;
	
	public static String stringOf(Instruction inst) {
		switch (inst) {
		case START: return "start";
		case STOP: return "stop";
		case RIGHT: return "right";
		case LEFT: return "left";
		case FORWARD: return "forward";
		case DOT: return "dot";
		case U_TURN: return "u_turn";
		case UNKNOWN: return "unknown";
		case NONE: return "none";
		case EOT: return "eot";
		default: return "";
		}
	}
}
