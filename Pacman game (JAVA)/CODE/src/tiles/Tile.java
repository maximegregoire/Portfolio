package tiles;

import javax.swing.JComponent;

/**
 * This class describes a Tile object, which is
 * a basic building block for the maze of the
 * Pacman game.
 * 
 * @author Antoine Tu
 * @author Carl Patenaude
 * @author Maxime Gregoire
 * @author Matthew Beirouti
 *
 */
public abstract class Tile extends JComponent {
	
	public static final int TILE_SIZE = 30;

	private int xPosition;
	private int yPosition;
	private boolean isWall;
	public boolean isLit;
	
	
	/**
	 * Initializes a Tile with the given coordinates.
	 * 
	 * @param xPosition	the position of the Tile in the x-axis.
	 * @param yPosition	the position of the Tile in the y-axis.
	 */
	public Tile(int xPosition, int yPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.setSize(TILE_SIZE - 1, TILE_SIZE - 1);
	}
	
	/**
	 * Gets whether or not the Tile is a wall.
	 * 
	 * @return	true if the Tile is a wall; false otherwise.
	 */
	public boolean isWall() {
		return isWall;
	}

	
	
	/**
	 * Sets a Tile's isWall property.
	 * @param isWall true if the Tile is a wall; false otherwise.
	 */
	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}

	/**
	 * Sets the position of the Tile on the x-axis.
	 * @param value	the new position.
	 */
	public void setXPosition(int value) {
		this.xPosition = value;
	}

	/**
	 * Gets the position of the Tile on the x-axis.
	 * @return the position of the Tile on the x-axis.
	 */
	public int getXPosition() {
		return this.xPosition;
	}

	/**
	 * Sets the position of the Tile on the y-axis.
	 * @param value	the new position.
	 */
	public void setYPosition(int value) {
		this.yPosition = value;
	}

	/**
	 * Gets the position of the Tile on the y-axis.
	 * @return the position of the Tile on the y-axis.
	 */
	public int getYPosition() {
		return this.yPosition;
	}

	public static float distance(Tile startTile, Tile endTile){
		float horizontalDistance = ((startTile.getXPosition()*Tile.TILE_SIZE)+(Tile.TILE_SIZE/2))-((endTile.getXPosition()*Tile.TILE_SIZE)+(Tile.TILE_SIZE/2));
		float verticalDistance = ((startTile.getYPosition()*Tile.TILE_SIZE)+(Tile.TILE_SIZE/2))-((endTile.getYPosition()*Tile.TILE_SIZE)+(Tile.TILE_SIZE/2));
		float totalDistance = (float) Math.sqrt(Math.pow(horizontalDistance, 2) + Math.pow(verticalDistance, 2));
		
		
		return (float) totalDistance;
		
	}
	
}
