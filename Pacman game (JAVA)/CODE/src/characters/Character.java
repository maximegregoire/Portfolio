package characters;

import game.CurrentLevel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tiles.Tile;
import display.MazeDisplay;

/**
 * This class describes a Character object, which
 * represents movable objects in the Pacman game.
 * @author Carl Patenaude
 * @author Maxime Gregoire
 * @author Matthew Beirouti
 * @author Antoine Tu
 *
 */
public abstract class Character {
	
	CharacterDisplay display;

	private float speed;
	
	private float xPos, yPos;
	
	private float xTarget, yTarget;

	protected Direction pastDirection = Direction.LEFT; //Terrible problems abound if this isn't initialized
	protected Direction direction = Direction.STOPPED;
	protected Direction nextDirection = Direction.STOPPED;
	float OFFSET = Tile.TILE_SIZE / 2.0f;

	/**
	 * Initializes a Character.
	 */
	public Character() {
		MovementTimer.getInstance().subscribe(this.timerListener);
	}

	ActionListener timerListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (direction == Direction.STOPPED) {
				direction = nextDirection;
			}
			doSmoothMovement();
		}
	};

	/**
	 * This method checks the current position against the next
	 * predicted position along the direction the
	 * Character is heading and adjusts the position
	 * depending on whether the Character is heading
	 * into a wall or any other tile.
	 */
	public synchronized void doSmoothMovement() {
		
		if (this.direction == Direction.STOPPED) {
			return;
		}
		
		float currentX = this.display.getXPos();
		float currentY = this.display.getYPos();
		
		float[] displacement = getDisplacement();

		xTarget = currentX + displacement[0];
		yTarget = currentY + displacement[1];
		
		//No more work to do... Unless we're on a new tile
		if (isOnNewTile(currentX, currentY, displacement[0], displacement[1])) {
			
			//If we can turn, we do
			if (!isNextTileAWall(currentX, currentY,this.nextDirection)) {
				correctTargetForTile();
			} else if (isNextTileAWall(currentX, currentY, this.direction)) {
				//If we can't continue forward, we stop
				correctTargetForWall();
			}
		}
		
		display.move(xTarget, yTarget);
	}

	/**
	 * Corrects the target position of the Character
	 * when the next tile is a Wall.
	 */
	private void correctTargetForWall() {
		pastDirection = this.direction;
		switch (this.direction) {
		case UP:
			yTarget = getDistToNextTile(yTarget);
			break;
		case DOWN:
			yTarget = getDistToNextTile(yTarget);
			break;
		case LEFT:
			xTarget = getDistToNextTile(xTarget);
			break;
		case RIGHT:
			xTarget = getDistToNextTile(xTarget);
			break;
		case STOPPED:
			throw new RuntimeException("Crossed into a tile while character was stopped");
		}

		this.direction = Direction.STOPPED;
		this.nextDirection = Direction.STOPPED;
	}

	/**
	 * Adjusts the target coordinates to take into account the distance
	 * to the next tile depending on the direction of the Character.
	 */
	private void correctTargetForTile() {
		float remainder = 0, closestTile = 0;
		MazeDisplay m = CurrentLevel.getInstance().getMazeDisplay();
		
		
		switch (this.direction) {
		case UP:
			yTarget = mod(yTarget,m.getNumberOfTilesY()*Tile.TILE_SIZE);
			closestTile = getDistToNextTile(yTarget);
			remainder = closestTile - yTarget;
			yTarget = closestTile;
			break;
		case DOWN:
			yTarget = mod(yTarget,m.getNumberOfTilesY()*Tile.TILE_SIZE);
			closestTile = getDistToNextTile(yTarget);
			remainder = yTarget - closestTile;
			yTarget = closestTile;
			break;
		case LEFT:
			xTarget = mod(xTarget,(m.getNumberOfTilesX()*Tile.TILE_SIZE));
			closestTile = getDistToNextTile(xTarget);
			remainder = closestTile - xTarget;
			xTarget = closestTile;
			break;
		case RIGHT:
			xTarget = mod(xTarget,m.getNumberOfTilesX()*Tile.TILE_SIZE);
			closestTile = getDistToNextTile(xTarget);
			remainder = xTarget - closestTile;
			xTarget = closestTile;
			break;
		default:
			break;
		}
		
		switch (this.nextDirection) {
		case UP: 
			yTarget -= remainder; 
			break;
		case DOWN: 
			yTarget += remainder; 
			break;
		case LEFT:
			xTarget -= remainder; 
			break;
		case RIGHT: 
			xTarget += remainder; 
			break;
		}
		
		this.direction = this.nextDirection;
	}

	/**
	 * Verifies if the Character is going to be on a new tile after moving.
	 * @param currentX the current position of the Character on the x-axis.
	 * @param currentY the current position of the Character on the y-axis.
	 * @param dx the displacement on the x-axis.
	 * @param dy the displacement on the y-axis.
	 * @return true if the Character lands on a tile; false otherwise.
	 */

	private boolean isOnNewTile(float currentX, float currentY, float dx,
			float dy) {
		
		final float TOLERANCE = 0.1f;
		switch (this.direction) {
		case UP:
			return currentY % Tile.TILE_SIZE <= -dy + TOLERANCE;
		case DOWN:
			return yTarget % Tile.TILE_SIZE <= dy + TOLERANCE;
		case LEFT:
			return currentX % Tile.TILE_SIZE <= -dx + TOLERANCE;
		case RIGHT:
			return xTarget % Tile.TILE_SIZE <= dx + TOLERANCE;
		default:
			return false;
		}
	}
	
	/**
	 * Gets the Character's displacement (delta).
	 * @return the displacement in the x and y coordinates.
	 */
	private float[] getDisplacement() {
		float dx = 0, dy = 0;
		switch (this.direction) {
		case UP:
			dy = -MazeDisplay.getRefreshFrequency() * this.getSpeed() / 100;
			break;
		case DOWN:
			dy = MazeDisplay.getRefreshFrequency() * this.getSpeed() / 100;
			break;
		case LEFT:
			dx = -MazeDisplay.getRefreshFrequency() * this.getSpeed() / 100;
			break;
		case RIGHT:
			dx = MazeDisplay.getRefreshFrequency() * this.getSpeed() / 100;
			break;
		case STOPPED:
			break;
		}
		float[] displacement = {dx, dy};
		return displacement;
	}

	/**
	 * Checks if the next Tile is a WallTile.
	 * @param x the x-coordinate of the next Tile.
	 * @param y the y-coordinate of the next Tile.
	 * @param d the direction in which we are searching.
	 * @return true if the next Tile is a WallTile.
	 */
	public boolean isNextTileAWall(float x, float y, Direction d) {
		MazeDisplay m = CurrentLevel.getInstance().getMazeDisplay();
		boolean b = m.getTileInDirection(x + OFFSET, y + OFFSET, d).isWall();
		return b;
	}

	/**
	 * Gets the distance to the next tile. TODO: Not sure of this part
	 * @param coord the current coordinate of the Character.
	 * @return the distance to the next tile.
	 */
	public int getDistToNextTile(float coord) {
		return (Math.round(coord + Tile.TILE_SIZE / 2) / Tile.TILE_SIZE)
				* Tile.TILE_SIZE;
	}
	
	/**
	 * Gets the direction to which the Character is currently facing.
	 * @return the direction to which the Character is facing.
	 */
	public Direction getDir() {
		return direction;
	}

	/**
	 * Sets the direction to which the Character faces.
	 * @param dir the direction to which the Character faces.
	 */
	public void setDir(Direction dir) {
		this.direction = dir;
	}

	/**
	 * Gets the current speed of the Character.
	 * @return the Character's speed.
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed of the Character.
	 * @param speed the Character speed.
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	/**
	 * Get the next direction of the character
	 */
	public Direction getNextDirection() {
		return nextDirection;
	}
	/**
	 * Set the next direction of the character
	 * @param nextDirection
	 */
	public void setNextDirection(Direction nextDirection) {
		this.nextDirection = nextDirection;
	}

	/**
	 * Modulo function used for the tunnels
	 */
	public int mod(int x, int n){
		int res = x%n;
		if(res<0) res+=n;
		return res;
	}
	
	/**
	 * Getter for the character's display
	 * @return the display of the character
	 */
	public CharacterDisplay getDisplay() {
		return display;
	}

	/**
	 * Setter for the character's display
	 * @param display of a character
	 */
	public void setDisplay(CharacterDisplay display) {
		this.display = display;
	}

	/**
	 * Implementation of the modulo function used for the tunnels, for floats
	 */
	public float mod(float x, int n){
		return (float)mod((int)x,n);
	}
	
	/**
	 * Gets the position of the Character on the x-axis.
	 * @return the position of the Character on the x-axis.
	 */
	public float getxPos() {
		return xPos;
	}

	/**
	 * Sets the position of the Character on the x-axis.
	 * @param xPos the x-coordinate of the Character.
	 */
	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	/**
	 * Gets the position of the Character on the y-axis.
	 * @return the position of the Character on the y-axis.
	 */
	public float getyPos() {
		return yPos;
	}

	/**
	 * Sets the position of the Character on the y-axis.
	 * @param yPos the y-coordinate of the Character.
	 */
	public void setyPos(float yPos) {
		this.yPos = yPos;
	}

	/**
	 * Getter for the past direction of the character
	 * @return the character's past direction
	 */
	public Direction getPastDirection() {
		return pastDirection;
	}

}
