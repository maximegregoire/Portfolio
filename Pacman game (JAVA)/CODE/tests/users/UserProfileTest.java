package users;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserProfileTest {
	HashFunction testHash = new SimpleHash();
	UserProfile normalUser;
	UserProfile longUser;
	static LevelStatistics wonLevel = new LevelStatistics(1, 9000, 2, true);
	static LevelStatistics lostLevel = new LevelStatistics(1, 9000, 0, false);
	static LevelStatistics wonLevel2 = new LevelStatistics(2, 9000, 3, true);
	static LevelStatistics lostLevel2 = new LevelStatistics(2, 9000, 0, false);
	static List<LevelStatistics> levels = new ArrayList<LevelStatistics>();
	static GameStatistics gameStatistics;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		levels.add(wonLevel);
		levels.add(lostLevel);
		levels.add(wonLevel2);
		levels.add(lostLevel2);
		gameStatistics = new GameStatistics(levels);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		gameStatistics = null;
	}

	@Before
	public void setUp() throws Exception {
		normalUser = new UserProfile("testuser", "testpassword", testHash);
		longUser = new UserProfile("arbitrarilylongusername", "arbitrarilylongpassword", testHash);
		longUser.setEmail("example@example.com");
		longUser.setFirstName("john");
		longUser.setLastName("doe");
	}

	@After
	public void tearDown() throws Exception {
		normalUser = null;
		longUser = null;
	}

	/**
	 * Test if a right password is recognized as correct.
	 */
	@Test
	public void testIsRightPassword() {
		assertTrue(normalUser.isRightPassword("testpassword"));
	}
	
	/**
	 * Test if a wrong password is recognized as incorrect.
	 */
	@Test
	public void testIsWrongPassword() {
		assertFalse(normalUser.isRightPassword("fakepassword"));
	}
	
	/**
	 * Test changing the password
	 */
	@Test
	public void testChangePassword() {
		try {
			normalUser.changePassword("testpassword", "newPassword");
		} catch (WrongPasswordException e) {
			fail("A correct password has been incorrectly evaluated as incorrect");
		}
	}
	
	/**
	 * Test changing the password by providing the wrong password
	 */
	@Test
	public void testWrongChangePassword() {
		try {
			normalUser.changePassword("fakepassword", "newPassword");
		} catch (WrongPasswordException e) {
			return;
		}
	}
	
	/**
	 * Test if a level is properly reported as locked or unlocked
	 */
	@Test
	public void testIsLevelUnlocked() {
		assertTrue(normalUser.isLevelUnlocked(0));
		assertFalse(normalUser.isLevelUnlocked(1));
	}
	
	/**
	 * Test if adding a game to the user's profile effectively adds something.
	 * Later on, we'll test if the added game is indeed the right one.
	 */
	@Test
	public void testAddGame() {
		normalUser.addGame(gameStatistics);
		assertFalse(normalUser.getGames() == null);
	}
	/**
	 * Test if we get the right statistics for the user.
	 */
	@Test
	public void testGetGames() {
		// null
		assertTrue(longUser.getGames() == null);
		GameStatistics gameStatistics2 = new GameStatistics(levels);
		ArrayList<GameStatistics> listOfGames = new ArrayList<GameStatistics>();
		listOfGames.add(gameStatistics);
		listOfGames.add(gameStatistics2);
		normalUser.setGames(listOfGames);
		assertTrue(normalUser.getGames().equals(listOfGames));
	}
	
	/**
	 * Tests if unlocking a level effectively unlocks it
	 */
	@Test
	public void testSetLevelUnlocked() {
		normalUser.setLevelUnlocked(1);
		assertTrue(normalUser.isLevelUnlocked(1));
	}
	
	/**
	 * Tests if getting the list of unlocked levels returns the right array.
	 */
	@Test
	public void testGetLevelsUnlocked() {
		boolean[] unlockedState = {true, false, false, false};
		assertTrue(normalUser.getLevelsUnlocked().equals(unlockedState));
	}
	
	/**
	 * Tests if setting the list of unlocked levels works correctly.
	 */
	@Test
	public void testSetLevelsUnlocked() {
		boolean[] unlockedState = {true, true, false, false, false};
		normalUser.setLevelsUnlocked(unlockedState);
		assertTrue(normalUser.getLevelsUnlocked().equals(unlockedState));
	}
	
	/**
	 * Tests if getting the username of the player returns the right value
	 */
	@Test
	public void testGetUsername() {
		assertTrue(normalUser.getUsername().equals("testuser"));
	}
	
	/**
	 * Tests if getting the email of the player returns the right value
	 */
	@Test
	public void testGetEmail() {
		String email = "example@example.com";
		assertTrue(longUser.getEmail().equals(email));
	}
	
	/**
	 * Tests if setting the email of the player returns the right value
	 */
	@Test
	public void testSetEmail() {
		String email = "example@example.com";
		normalUser.setEmail(email);
		assertTrue(normalUser.getEmail().equals(email));
	}
	
	/**
	 * Tests if getting the first and last names of the player return the right value
	 */
	@Test
	public void testGetNames() {
		assertTrue(longUser.getFirstName().equals("john"));
		assertTrue(longUser.getLastName().equals("doe"));
	}
	
	/**
	 * Tests if setting the first and last names of the player return the right value
	 */
	@Test
	public void testSetNames() {
		normalUser.setFirstName("john");
		normalUser.setLastName("doe");
		assertTrue(normalUser.getFirstName().equals("john"));
		assertTrue(normalUser.getLastName().equals("doe"));
	}
	
	/**
	 * Tests if getting the username returns the right value
	 */
	@Test
	public void testToString() {
		assertTrue(normalUser.toString().equals("testuser"));
	}
}
