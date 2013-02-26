package game;

public class CurrentLevel {
	private static Level instance;

	public static Level getInstance() {
		return instance;
	}

	public static void setInstance(Level instance) {
		CurrentLevel.instance = instance;
	}
}
