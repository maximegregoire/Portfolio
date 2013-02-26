package users;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * This class tests the different methods of the LevelStatistics class
 * @author Natsumi
 *
 */
public class LevelStatisticsTest {
	LevelStatistics wonLevel = new LevelStatistics(1, 9000, 2, true);
	LevelStatistics lostLevel = new LevelStatistics(1, 100, 0, false);
	LevelStatistics wonLevel2 = new LevelStatistics(2, 9000, 3, true);
	LevelStatistics lostLevel2 = new LevelStatistics(2, 500, 0, false);
	

	/**
	 * Tests whether the correct score is returned.
	 */
	@Test
	public final void testGetScore() {
		assertTrue(wonLevel.getScore() == 9000);
	}
	
	/**
	 * Tests whether a won game is correctly seen as won and
	 * a lost level is correctly seen as lost.
	 */
	@Test
	public final void testIsWon() {
		assertTrue(wonLevel.isWon());
		assertFalse(lostLevel.isWon());
	}

	/**
	 * Tests whether a won game is correctly seen as won and
	 * a lost game is correctly seen as lost.
	 */
	@Test
	public final void testIsNotLost() {
		assertFalse(wonLevel.isLost());
		assertTrue(lostLevel.isLost());
	}

	
	/**
	 * Tests whether the correct game number is returned.
	 */
	@Test
	public final void testGetLevelNumber() {
		assertTrue(wonLevel.getLevelNumber() == 1);
		assertTrue(lostLevel2.getLevelNumber() == 2);
	}
	
	/**
	 * Tests whether the correct number of remaining lives is returned.
	 */
	@Test
	public final void testGetLivesRemaining() {
		assertTrue(wonLevel.getLivesRemaining() == 2);
		assertTrue(lostLevel.getLivesRemaining() == 0);
	}
	
	/**
	 * Test whether the correct number of deaths is returned.
	 */
	@Test
	public final void testGetDeaths() {
		assertTrue(wonLevel.getDeaths() == 1);
		assertTrue(wonLevel2.getDeaths() == 0);
		assertTrue(lostLevel.getDeaths() == 3);
	}
}
