package characters;

import java.awt.Color;

import tiles.Tile;


/**
 * This class describes Clyde, a member of Ghost.
 * @author Carl Patenaude
 * @author Maxime Gregoire
 * @author Matthew Beirouti
 * @author Antoine Tu
 *
 */
public class Clyde extends Ghost {

	/**
	 * Initializes a Clyde ghost
	 * @param spawn
	 * @param dots
	 */
	public Clyde(Tile spawn, int dots) {
		super(spawn, Color.ORANGE, dots);

		this.STARTING_DIRECTION = Direction.RIGHT;
		this.setDir(this.STARTING_DIRECTION);
		this.setNextDirection(this.STARTING_DIRECTION);
	}
}
