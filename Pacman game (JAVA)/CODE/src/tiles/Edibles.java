package tiles;

/**
 * This class describes a Tile that contains
 * objects edible by Pacman
 * 
 * @author Antoine Tu
 *
 */
public abstract class Edibles extends Tile {
	private int points;
	
	/**
	 * Initializes a new Edible object.
	 * @param xPosition	the position of the Edible on the x-axis.
	 * @param yPosition	the position of the Edible on the y-axis.
	 * @param points	the number of points the Edible is worth.
	 */
	public Edibles(int xPosition, int yPosition, int points) {
		super(xPosition, yPosition);
		this.points = points;
	}
	
	/**
	 * Gets the number of points this object is worth.
	 * @return	the number of points.
	 */
	public int getPoints() {
		return this.points;
	}
	
	
}
