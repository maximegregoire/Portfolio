package characters;

import javax.swing.JComponent;

import tiles.Tile;
import display.MazeDisplay;

/**
 * This class describes a Character object, which
 * represents movable objects in the Pacman game.
 * <p> TODO: explain doSmoothMovement
 * </p>
 * @author Carl Patenaude
 * @author Maxime Gregoire
 * @author Matthew Beirouti
 * @author Antoine Tu
 *
 */
public abstract class CharacterDisplay extends JComponent {

	private Character characterModel;
	
	/**
	 * Initializes a Character.
	 */
	public CharacterDisplay(Character characterModel) {
		this.setSize(Tile.TILE_SIZE - 1, Tile.TILE_SIZE - 1);
		this.characterModel = characterModel;
	}

	public void move(int x, int y) {
		move((float) x, (float) y);
	}

	/**
	 * Change the Character's position to an absolute coordinate.
	 * @param x the new x-coordinate Character.
	 * @param y the new y-coordinate of the Character.
	 */
	public void move(float x, float y) {

		repaintArea();
		
		setXPos(x);
		setYPos(y);

		repaintArea();
	}
	
	/**
	 * Redraw the maze at the current position.
	 */
	public void repaintArea() {
		this.getParent().repaint((int) getXPos(), (int) getYPos(),
				getWidth() + 2, getHeight() + 2);
	}

	/**
	 * Gets the position of the Character on the x-axis.
	 * @return the position of the Character on the x-axis.
	 */
	public float getXPos() {
		return this.characterModel.getxPos();
	}

	/**
	 * Sets the position of the Character on the x-axis.
	 * @param xPos the x-coordinate of the Character.
	 */
	public void setXPos(float xPos) {
		this.characterModel.setxPos(xPos);
	}

	/**
	 * Gets the position of the Character on the y-axis.
	 * @return the position of the Character on the y-axis.
	 */
	public float getYPos() {
		return this.characterModel.getyPos();
	}

	/**
	 * Sets the position of the Character on the y-axis.
	 * @param yPos the y-coordinate of the Character.
	 */
	public void setYPos(float yPos) {
		this.characterModel.setyPos(yPos);
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
	 * Implementation of the modulo function used for the tunnels, for floats
	 */
	public float mod(float x, int n){
		return (float)mod((int)x,n);
	}
	
	/**
	 * Gets the character's tile
	 * @return the character's tile
	 */
	public Tile getTile() {
		MazeDisplay m = (MazeDisplay) this.getParent();
		return m.getTile(this.getXPos(), this.getYPos());
	}

	/**
	 * Gets the character's model
	 * @return the character's model
	 */
	public Character getCharacterModel() {
		return characterModel;
	}

	/**
	 * Sets the character's model
	 * @param characterModel
	 */
	public void setCharacterModel(Character characterModel) {
		this.characterModel = characterModel;
	}
}
