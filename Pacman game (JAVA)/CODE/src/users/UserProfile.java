package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class defines a user profile and contains the username, password, as well as levels unlocked and statistics
 * @author Carl Patenaude Poulin
 *
 */

public class UserProfile implements Serializable {
	private ArrayList<GameStatistics> games;
	private boolean[] levelsUnlocked; //0 is debug level
	private int passwordHash, timesDied;
	private final String username;
	private String firstName, lastName, email;
	private HashFunction hashFunction;
	
	/**
	 * Instantiates a new UserProfile
	 * @param username the username
	 * @param password the password
	 * @param hashFunction a hash function to hash the password
	 */
	public UserProfile(String username, String password, HashFunction hashFunction) {
		this.username = username;
		this.passwordHash = hashFunction.hash(password);
		this.hashFunction = hashFunction;
		levelsUnlocked = new boolean[]{true, false, false, false};
		this.games = new ArrayList<GameStatistics>();
	}
	
	/**
	 * Instantiates an empty UserProfile, effectively creating a guest user
	 */
	public UserProfile() { 
		this.username = "Guest";
	}
	
	/**
	 * Returns whether the password provided is right
	 * @param password the input password
	 * @return true if the password is correct; false otherwise
	 */
	public boolean isRightPassword(String password) {
		return hashFunction.hash(password) == this.passwordHash;
	}
	
	/**
	 * Changes the password of the user.
	 * @param oldPassword the old password.
	 * @param newPassword the new password.
	 * @throws WrongPasswordException
	 */
	public void changePassword(String oldPassword, String newPassword) throws WrongPasswordException {
		if (!isRightPassword(oldPassword)) {
			throw new WrongPasswordException();
		}
		this.passwordHash = hashFunction.hash(newPassword);
	}
	
	/**
	 * Gets whether the specified level is unlocked.
	 * @param levelNumber the level to check.
	 * @return true if the level is unlocked, false otherwise.
	 */
	public boolean isLevelUnlocked(int levelNumber) {
		return levelsUnlocked[levelNumber];
	}
	
	/**
	 * Gets the game statistics of the user.
	 * @return an ArrayList of game statistics
	 */
	public ArrayList<GameStatistics> getGames() {
		return games;
	}
	
	/**
	 * Sets the game statistics of the user
	 * @param games the game statistics
	 */
	public void setGames(ArrayList<GameStatistics> games) {
		this.games = games;
	}
	
	/**
	 * Adds a statistic to the list of player statistics
	 * @param game the statistic data to add
	 */
	public void addGame(GameStatistics game) {
		games.add(game);
	}
	
	/**
	 * Unlocks the specified level.
	 * @param levelNumber the level to unlock.
	 */
	public void setLevelUnlocked(int levelNumber) {
		if (levelNumber < levelsUnlocked.length) {
			this.levelsUnlocked[levelNumber] = true;
		}
	}
	
	/**
	 * Gets the levels unlocked
	 * @return an array representing the unlocked (true) state of the levels.
	 */
	public boolean[] getLevelsUnlocked() {
		return levelsUnlocked;
	}
	
	/**
	 * Sets the levels unlocked
	 * @param levelsUnlocked a boolean array representing whether the levels are unlocked (true) or locked (false)
	 */
	public void setLevelsUnlocked(boolean[] levelsUnlocked) {
		this.levelsUnlocked = levelsUnlocked;
	}
	
	/**
	 * Gets the username of the player
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the email of the player
	 * @return the player email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the player
	 * @param email the email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the first name of the player
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the player
	 * @param firstName the first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name of the player.
	 * @return the last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the player
	 * @param lastName the last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Gets the username of the player
	 * @return the username
	 */
	public String toString() {
		return this.username;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof UserProfile && this.hashCode() == o.hashCode());
	}
	
	@Override
	public int hashCode() {
		return this == null ? 0 : username.hashCode() * 31 + passwordHash;
	}
	
	/**
	 * Gets the number of deaths of this player
	 * @return the number of deaths
	 */
	public int getDeaths() {
		int res = 0;
		for (GameStatistics g : games) {
			for (LevelStatistics l : g.getLevels()) {
				res += l.getDeaths();
			}
		}
		return res;
	}
	
	/**
	 * Gets the number of wins of this player
	 * @return
	 */
	public int getWins() {
		int res = 0;
		for (GameStatistics g : games) {
			if (g.isWon()) {
				res++;
			}
		}
		return res;
	}
	
	/**
	 * Gets the maximum score for the given level
	 * @param levelNumber the level
	 * @return the max score
	 */
	public int getMaxScoreByLevel(int levelNumber) {
		int max = 0;
		
		for (LevelStatistics level : this.getLevelsByNumber(levelNumber)) {
			max = Math.max(max, level.getScore());
		}
		
		return max;
	}
	
	/**
	 * Gets the number of deaths for the specified level
	 * @param levelNumber the level
	 * @return the number of deaths
	 */
	public int getDeathsByLevel(int levelNumber) {
		int deaths = 0;
		
		for (LevelStatistics level : this.getLevelsByNumber(levelNumber)) {
			deaths += level.getDeaths();
			}
		
		return deaths;
	}
	
	/**
	 * Gets the number of wins for the specified level
	 * @param levelNumber the level
	 * @return the number of wins
	 */
	public int getWinsByLevel(int levelNumber) {
		int wins = 0;
		
		for (LevelStatistics level : this.getLevelsByNumber(levelNumber)) {
			if (level.isWon()) {
				wins++;
			}
		}
		
		return wins;
	}
	
	/**
	 * Gets the number of losses for the specified level
	 * @param levelNumber the level
	 * @return the number of losses
	 */
	public int getLossesByLevel(int levelNumber) {
		int losses = 0;
		for (LevelStatistics level : this.getLevelsByNumber(levelNumber)) {
			if (level.isLost()) {
				losses++;
			}
		}
		return losses;
	}
	/**
	 * Gets the list of LevelStatistics for the specified level
	 * @param levelNumber the level
	 * @return a List of LevelStatistics
	 */
	private List<LevelStatistics> getLevelsByNumber (int levelNumber) {
		ArrayList<LevelStatistics> levels = new ArrayList<LevelStatistics>();
		
		for(GameStatistics game : games) {
			for (LevelStatistics level : game.getLevels()) {
				if (level.getLevelNumber() == levelNumber) {
					levels.add(level);
				}
			}
		}
		
		return levels;
	}
	
}
