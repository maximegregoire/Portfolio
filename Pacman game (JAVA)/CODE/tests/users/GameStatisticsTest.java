package users;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * This class defines unit tests for the GameStatistics class
 * @author Antoine Tu
 *
 */
public class GameStatisticsTest {
	static LevelStatistics wonLevel = new LevelStatistics(1, 9000, 2, true);
	static LevelStatistics lostLevel = new LevelStatistics(1, 100, 0, false);
	static LevelStatistics wonLevel2 = new LevelStatistics(2, 9000, 3, true);
	static LevelStatistics lostLevel2 = new LevelStatistics(2, 500, 0, false);
	static List<LevelStatistics> levels = new ArrayList<LevelStatistics>();
	
	static GameStatistics gameStatistics;
	static GameStatistics gameStatisticsLoss;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		levels.add(wonLevel);
		levels.add(wonLevel2);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	

	@Before
	public void setUp() throws Exception {
		gameStatistics = new GameStatistics(levels);
		levels.add(lostLevel);
		gameStatisticsLoss = new GameStatistics(levels);
	}

	@After
	public void tearDown() throws Exception {
		gameStatistics = null;
		gameStatisticsLoss = null;
	}

	/**
	 * Tests whether won games are correctly evaluated as won, and that a lost game is
	 * correctly evaluated as lost.
	 */
	@Test
	public final void testIsWon() {
		assertTrue(gameStatistics.isWon());
		assertFalse(gameStatisticsLoss.isWon());
	}
	
	/**
	 * Tests whether the total score is correctly calculated.
	 */
	@Test
	public final void testGetScore() {
		assertTrue(gameStatistics.getScore() == 18000);
		assertTrue(gameStatisticsLoss.getScore() == 9100);
	}
	
	/**
	 * Tests whether adding a level to the GameStatistics effectively adds it to the collection
	 */
	@Test
	public final void testAddLevel() {
		levels.add(lostLevel2);
		gameStatisticsLoss.addLevel(lostLevel2);
		assertTrue(gameStatisticsLoss.getLevels().equals(levels));
	}
	
	/**
	 * Tests whether the total number of deaths is correctly calculated.
	 */
	@Test
	public final void testGetDeaths() {
		assertTrue(gameStatistics.getDeaths() == 1);
		assertTrue(gameStatisticsLoss.getDeaths() == 4);
	}
}
