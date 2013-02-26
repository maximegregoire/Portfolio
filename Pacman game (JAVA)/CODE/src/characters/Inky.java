package characters;

import java.awt.Color;

import tiles.Tile;


/**
 * This class describes Inky, a member of Ghost.
 * @author Carl Patenaude
 * @author Maxime Gregoire
 * @author Matthew Beirouti
 * @author Antoine Tu
 *
 */
public class Inky extends Ghost {

	/**
	 * Initializes a Inky Ghost.
	 */
	public Inky(Tile spawn, int dots) {
		super(spawn, Color.CYAN, dots);
		
		this.STARTING_DIRECTION = Direction.LEFT;
		this.setDir(this.STARTING_DIRECTION);
		this.setNextDirection(this.STARTING_DIRECTION);
	}
}
