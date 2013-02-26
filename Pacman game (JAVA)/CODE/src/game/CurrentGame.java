package game;

public class CurrentGame {
	private static Game instance;

	public static Game getInstance() {
		return instance;
	}

	public static void setInstance(Game instance) {
		CurrentGame.instance = instance;
	}
}
