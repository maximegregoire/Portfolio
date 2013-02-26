package characters;

import java.awt.Color;

import tiles.Tile;


/**
 * This class describes Pinky, a member of Ghost.
 * @author Carl Patenaude
 * @author Maxime Gregoire
 * @author Matthew Beirouti
 * @author Antoine Tu
 *
 */
public class Pinky extends Ghost {
	
	/**
	 * Initializes a Pinky Ghost.
	 */
	public Pinky(Tile spawn, int dots) {
		super(spawn, Color.PINK, dots);
		
		this.STARTING_DIRECTION = Direction.LEFT;
		this.setDir(this.STARTING_DIRECTION);
		this.setNextDirection(this.STARTING_DIRECTION);
	}
}
