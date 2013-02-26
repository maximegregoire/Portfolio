package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class defines game statistics, which is a collection of LevelStatistics,
 * as well as methods for getting statistical data.
 * @author Carl Patenaude Poulin
 * @author Antoine Tu
 *
 */
public class GameStatistics implements Serializable {
	private final List<LevelStatistics> levels;
	
	public GameStatistics(List<LevelStatistics> levels) {
		this.levels = levels;
	}
	
	/**
	 * Gets whether the levels have all been won
	 * @return true if all of the games have been won, false otherwise.
	 */
	public boolean isWon() {
		for(LevelStatistics l : levels) {
			if(!l.isWon()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the total score for all the levels.
	 * @return the total score as a sum of all the scores.
	 */
	public int getScore() {
		int res = 0;
		for(LevelStatistics l : levels) {
			res += l.getScore();
		}
		return res;
	}
	
	/**
	 * Adds a level to the list.
	 * @param level the level to add.
	 */
	public void addLevel(LevelStatistics level) {
		levels.add(level);
	}
	
	/**
	 * Gets the List of levels.
	 * @return a List containing all the levels.
	 */
	public List<LevelStatistics> getLevels() {
		return levels;
	}
	
	/**
	 * Gets the total number of deaths for all the levels.
	 * @return the total number of deaths.
	 */
	public int getDeaths() {
		int res = 0;
		for (LevelStatistics lvl : levels) {
			res += lvl.getDeaths();
		}
		return res;
	}
}
