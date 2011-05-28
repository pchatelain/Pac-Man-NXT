package sensors;

public enum Color {
	BLACK(0), GREY(50), WHITE(100);
	
	public int value;
	
	private Color(int value) {
		this.value = value;
	}
}
