package characters;

import java.awt.Color;

import tiles.Tile;


/**
 * This class describes Blinky, a member of Ghost.
 * @author Carl Patenaude
 * @author Maxime Gregoire
 * @author Matthew Beirouti
 * @author Antoine Tu
 *
 */
public class Blinky extends Ghost {
	
	/**
	 * Initializes a Blinky Ghost.
	 */

	public Blinky(Tile spawn, int dots) {
		super(spawn, Color.RED, dots);
		
		this.STARTING_DIRECTION = Direction.RIGHT;
		this.setDir(this.STARTING_DIRECTION);
		this.setNextDirection(this.STARTING_DIRECTION);
	}
}
