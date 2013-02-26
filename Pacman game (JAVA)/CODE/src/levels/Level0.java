package levels;

/**
 * Debugging level
 * @author Carl Patenaude Poulin
 */

public class Level0 extends AbstractLevel {
	
	public Level0() {
		pacmanNormSpeed = 0.8f;
		pacmanNormDotSpeed = 0.71f;
		pacmanFrightSpeed = 0.9f;
		pacmanFrightDotSpeed = 0.79f;
		ghostSpeed = 0.75f;
		ghostFrightenedSpeed = 0.5f;
		ghostTunnelSpeed = 0.4f;
		int[][] maze = {
					{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, 
					{1,1,1,1,1,1,2,2,2,2,2,2,2,1,1,1,1,1,1},  
					{1,1,1,1,1,1,2,1,1,4,1,1,2,1,1,1,1,1,1},
					{1,1,1,1,1,1,2,1,2,2,2,1,2,1,1,1,1,1,1},
					{1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,1,1},
					{1,1,1,1,1,1,2,2,2,3,2,2,2,1,1,1,1,1,1},  
					{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},  
					{1,6,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,6,1},  
					{1,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,1},  
					{1,1,1,1,0,0,0,0,0,5,0,0,0,0,0,1,1,1,1},
				};
		mazeMap = maze;
	}
	
	
}
