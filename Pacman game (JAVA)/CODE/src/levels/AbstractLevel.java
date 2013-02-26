package levels;
/**
 * This class defines the generic of a level in Pacman.
 * A level determines the speed of Pacman and the ghosts.
 * As well, the map is different depending on the level.
 * Levels can be in an unlocked or locked state.
 * @author Antoine Tu
 *
 */
public abstract class AbstractLevel {
	private static AbstractLevel instance;
	
	protected int levelNumber;
	protected float pacmanNormSpeed;
	protected float pacmanNormDotSpeed;
	protected float pacmanFrightSpeed;
	protected float pacmanFrightDotSpeed;
	protected float ghostSpeed;
	protected float ghostFrightenedSpeed;
	protected float ghostTunnelSpeed;
	protected int[][] mazeMap;
	protected int[][] scatterTimerLengths;
	
	public static AbstractLevel getInstance() {
		return instance;
	}

	/**
	 * Gets the speed of Pacman for the current level.
	 * @return the speed of Pacman (in percent).
	 */
	public float getPacmanSpeed() {
		return pacmanNormSpeed;
	}

	/**
	 * Gets the speed of Pacman after he eats a dot.
	 * @return the speed of Pacman (in percent).
	 */
	public float getPacmanNormDotSpeed() {
		return pacmanNormDotSpeed;
	}

	/**
	 * Gets the speed of Pacman in fright mode.
	 * @return the speed of Pacman (in percent).
	 */
	public float getPacmanFrightSpeed() {
		return pacmanFrightSpeed;
	}

	/**
	 * Gets the speed of Pacman in fright mode after eating a dot.
	 * @return the speed of Pacman (in percent).
	 */
	public float getPacmanFrightDotSpeed() {
		return pacmanFrightDotSpeed;
	}


	/**
	 * Gets the speed of Pacman for the current level.
	 * @return the speed of the ghosts (in percent).
	 */
	public float getGhostSpeed() {
		return ghostSpeed;
	}
	
	/**
	 * Gets the speed of the ghosts when they are frightened in the current level.
	 * @return the speed of the ghosts (in percent).
	 */
	public float getGhostFrightenedSpeed() {
		return ghostFrightenedSpeed;
	}

	/**
	 * Gets the speed of the ghosts when they enter a tunnel.
	 * @return the speed of the ghosts (in percent).
	 */
	public float getGhostTunnelSpeed() {
		return ghostTunnelSpeed;
	}

	/**
	 * Gets the maze map for the current level.
	 * @return a 2D array representing the maze.
	 */
	public int[][] getMazeMap() {
		return mazeMap;
	}

	/**
	 * Gets the name of the current level.
	 * @return a string representing the level name.
	 */
	public String getLevelName() {
		return "Level " + levelNumber;
	}
	
	/**
	 * Gets the number of the current level.
	 * @return the level number.
	 */
	public int getLevelNumber() {
		return levelNumber;

	}

	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}
	
}
