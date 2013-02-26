package tiles;

/**
 * This class describes a Tile object that does not
 * contain anything and is navigable by Pacman and
 * the ghosts
 * 
 * @author Antoine Tu
 */
import java.awt.Color;
import java.awt.Graphics;

public class Pipe extends Tile {

	/**
	 * Initializes an EmptyTile with the given coordinates.
	 * @param xPosition	the position of the EmptyTile on the x-axis.
	 * @param yPosition	the position of the EmptyTile on the y-axis.
	 */
	public Pipe(int xPosition, int yPosition) {
		super(xPosition, yPosition);
		this.setWall(true);		
	}
	
	/**
	 * Draws the EmptyTile object.
	 */
	public void paintComponent(Graphics g){
    	super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(Math.round(getXPosition()), Math.round(getYPosition()), getWidth(), getHeight());
        g.setColor(Color.GREEN);
        g.drawRect(Math.round(getXPosition()), Math.round(getYPosition()), getWidth(), getHeight());
    }
	
   }
