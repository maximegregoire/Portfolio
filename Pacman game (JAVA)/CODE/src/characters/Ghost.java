package characters;

import ghosts.GhostController;
import ghosts.GhostMode;

import java.awt.Color;
import java.awt.Graphics;

import tiles.Tile;


/**
 * This class describes Ghost characters as well
 * as methods pertaining to their actions in the
 * Pacman game
 * 
 * @author Antoine Tu
 * @author Carl Patenaude
 * @author Maxime Gregoire
 * @author Matthew Beirouti
 *
 */
public abstract class Ghost extends Character {

	Color color;
	GhostController ghostController;
	final int STARTING_X;
	final int STARTING_Y;
	Direction STARTING_DIRECTION;
	final int maxDots;
	int dotCount = 0;
	GhostMode initialMode;

   /**
    * Initializes a ghost on a spawn Tile. The ghost gets out of the cage
    * when Pacman ate a certain number of pills: maxDot.
    * @param spawn : the ghost spawning tile
    * @param maxDot : the number of dots pacman needs to eat before the ghost
    * gets out of the cage
    */
	
	public Ghost(Tile spawn, Color color, int maxDot) {
		this.setDisplay(new GhostDisplay(this, color));
		
		STARTING_X = spawn.getXPosition() / Tile.TILE_SIZE;
		STARTING_Y = spawn.getYPosition() / Tile.TILE_SIZE;
		this.display.setXPos(STARTING_X * Tile.TILE_SIZE);
		this.display.setYPos(STARTING_Y * Tile.TILE_SIZE);

		this.maxDots = maxDot;
	}

	/**
	 * Checks if Pacman  ate the needed amount of dots for the ghost to leave the cage. 
	 * If so, the ghost's mode is changed, in order for it to leave the cage.
	 */

	public void ateDot() {
		if (dotCount == maxDots) {
			this.ghostController.setMode(GhostMode.LEAVINGCAGE);
		}
		dotCount++;
	}
	
	/**
	 * Getter for the GhostController of the ghost.
	 * @return the ghost's GhostController
	 */
	public GhostController getGhostController() {
		return ghostController;
	}

	/**
	 * Setter for the GhostController of the ghost
	 * @param gc
	 */
	public void setGhostController(GhostController gc) {
		this.ghostController = gc;
		this.setMode(gc.getMode());
	}   
	
	/**
	 * Setter for the ghost's mode.
	 * @param mode
	 */
	
	public void setMode(GhostMode mode) {
		this.initialMode = mode;
		this.ghostController.setMode(mode);
		this.dotCount = 0;
		this.setDir(this.STARTING_DIRECTION);
		this.setNextDirection(this.STARTING_DIRECTION);
	}
	
	/**
	 * Reinitializes the ghost at its initial position.
	 */

	public void reinitialize() {
		this.setMode(initialMode);
		this.getDisplay().move(STARTING_X * Tile.TILE_SIZE, STARTING_Y * Tile.TILE_SIZE);
		((GhostDisplay) this.getDisplay()).setFrightened(false);
	}

	/**
	 * Getter for the actual number of pills eaten by Pacman.
	 * @return the number of pills eaten
	 */
	
	public int getDotCount() {
		return dotCount;
	}

	/**
	 * Setter for the actual number of pills eaten by Pacman.
	 * @param dotCount
	 */
	
	public void setDotCount(int dotCount) {
		this.dotCount = dotCount;
	}

	/**
	 * Getter for the number of dots needed for a certain ghost to 
	 * come out of the cage.
	 * @return the ghost's number of dots needed to leave cage
	 */
	
	public int getMaxDots() {
		return maxDots;
	}
}
