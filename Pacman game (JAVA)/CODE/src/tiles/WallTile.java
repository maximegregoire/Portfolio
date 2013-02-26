package tiles;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This class describes a WallTile, which is a
 * Tile object that cannot be traversed by any
 * Character and that does not contain any Edibles.
 * 
 * @author Antoine Tu
 *
 */
public class WallTile extends Tile {
	
	/**
	 * Initializes a WallTile object at the given coordinates.
	 * @param x the position of the WallTile on the x-axis.
	 * @param y the position of the WallTile on the y-axis.
	 */
	public WallTile(int x, int y) {
		super(x, y);
		this.setWall(true);
	}
	
	/**
	 * Draws the WallTile object.
	 */
	public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	
        g.setColor(Color.BLACK);
        g.fillRect(Math.round(getXPosition()), Math.round(getYPosition()), getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(Math.round(getXPosition()), Math.round(getYPosition()), getWidth(), getHeight());
        
    }
   }
