package io;
import lejos.nxt.LCD;


public class LineDisplayWriter {
	
	public static int lineLength = 17;
	
	public static String[] cleared = {
		"                 ",
		"                 ",
		"                 ",
		"                 ",
		"                 ",
		"                 ",
		"                 ",
		"                 "};
	public static String[] lines = cleared;
	
	public static void setLine(String string, int line) {
		setLine(string, line, false);
	}
	
	public static void setLine(String string, int line, boolean refresh) {
		if(line >= lines.length)
			line = lines.length-1;
		if(string.length() > lineLength)
			string = string.substring(0, lineLength);
		else if(string.length() < lineLength)
			string = string + repeatString(" ", lineLength - string.length());
		lines[line] = string;
		LCD.drawString(string, 0, line);
		if(refresh)
			LCD.refresh();
	}
	
	private static String repeatString(String string, int times) {
		String repeatedString = "";
		for(int i = 1; i <= times; i++)
			repeatedString = repeatedString + string;
		return repeatedString;
	}
	
	public static void clear() {
		lines = cleared;
		LCD.clear();
	}
	
	public static void refresh() {
		LCD.refresh();
	}
}
