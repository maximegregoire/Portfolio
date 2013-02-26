package users;

import java.io.Serializable;

/**
 * This class defines the statistic data for a level, 
 * including the level number, score, lives remaining and whether the level has been won or lost.
 * @author Antoine Tu
 * @author Carl Patenaude Poulin
 *
 */
public class LevelStatistics implements Serializable {
	private final int levelNumber, score, livesRemaining;
	private boolean won;
	
	/**
	 * Instantiates a new level statistic
	 * @param levelNumber the level number
	 * @param score the score for this level
	 * @param livesRemaining the number of lives remaining
	 * @param won true if the level has been won; false otherwise
	 */
	public LevelStatistics(int levelNumber, int score, int livesRemaining, boolean won) {
		this.levelNumber = levelNumber;
		this.score = score;
		this.livesRemaining = livesRemaining;
		this.won = won;
	}

	/**
	 * Gets the score.
	 * @return the score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Gets whether the level has been won.
	 * @return true if the level has been won; false otherwise.
	 */
	public boolean isWon() {
		return won;
	}
	
	/**
	 * Gets whether the level has been or lost
	 * @return true if the number of lives is 0; false otherwise. 
	 */
	public boolean isLost() {
		return livesRemaining == 0;
	}

	/**
	 * Gets the level number.
	 * @return the level number.
	 */
	public int getLevelNumber() {
		return levelNumber;
	}

	/**
	 * Gets the remaining number of lives.
	 * @return the number of lives.
	 */
	public int getLivesRemaining() {
		return livesRemaining;
	}

	/**
	 * Gets the number of deaths.
	 * @return the number of deaths.
	 */
	public int getDeaths() {
		return 3 - livesRemaining;
	}
}
