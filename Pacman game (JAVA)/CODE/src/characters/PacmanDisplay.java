package characters;

import game.CurrentLevel;
import ghosts.GhostMode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import tiles.Pill;
import tiles.Tile;
import users.CurrentUser;
import users.GuestProfile;
import display.MazeDisplay;
import display.WindowManager;

/**
 * This class describes a Pacman character, which is
 * a player-controlled Character object in the Pacman
 * game.
 * 
 * @author Antoine Tu
 * @author Carl Patenaude
 * @author Maxime Gregoire
 * @author Matthew Beirouti
 *
 */
public class PacmanDisplay extends CharacterDisplay {
	private Tile clydeTile, pinkyTile, inkyTile, blinkyTile;
	private int score = 0;
	private float currentX;
	private float currentY;
	private int numberOfLives = 3;
	private Direction paintDirection = Direction.RIGHT;
	/**
	 * Initializes Pacman
	 */
	public PacmanDisplay(Character characterModel) {
		super(characterModel);
	}
			
	/**
	 * Draws Pacman
	 */
    public void paintComponent(Graphics g){
    	super.paintComponent(g);

        g.setColor(Color.YELLOW);
        g.fillOval(Math.round(getXPos()), Math.round(getYPos()), getWidth(), getHeight());
        g.setColor(Color.RED);
        g.drawOval(Math.round(getXPos()), Math.round(getYPos()), getWidth(), getHeight());
        
        if (this.getCharacterModel().getDir() == Direction.STOPPED){
        	paintDirection = this.getCharacterModel().getPastDirection();
        } else {
        	paintDirection = this.getCharacterModel().getDir();
        }
        
        switch (paintDirection){
        case UP:
        	g.setColor(Color.BLACK);
        	g.fillOval(Math.round(getXPos())+(int)(0.2*Tile.TILE_SIZE),Math.round(getYPos())+(int)(0.2*Tile.TILE_SIZE),(int)(Tile.TILE_SIZE*0.2), (int)(Tile.TILE_SIZE*0.2));
        	g.setColor(Color.WHITE);
        	g.drawOval(Math.round(getXPos())+(int)(0.2*Tile.TILE_SIZE),Math.round(getYPos())+(int)(0.2*Tile.TILE_SIZE),(int)(Tile.TILE_SIZE*0.2), (int)(Tile.TILE_SIZE*0.2));
        	break;
        case DOWN:
        	g.setColor(Color.BLACK);
        	g.fillOval(Math.round(getXPos())+(int)(0.6*Tile.TILE_SIZE),Math.round(getYPos())+(int)(0.6*Tile.TILE_SIZE),(int)(Tile.TILE_SIZE*0.2), (int)(Tile.TILE_SIZE*0.2));
        	g.setColor(Color.WHITE);
        	g.drawOval(Math.round(getXPos())+(int)(0.6*Tile.TILE_SIZE),Math.round(getYPos())+(int)(0.6*Tile.TILE_SIZE),(int)(Tile.TILE_SIZE*0.2), (int)(Tile.TILE_SIZE*0.2));
        	break;
        case LEFT:
        	g.setColor(Color.BLACK);
        	g.fillOval(Math.round(getXPos())+(int)(0.2*Tile.TILE_SIZE),Math.round(getYPos())+(int)(0.2*Tile.TILE_SIZE),(int)(Tile.TILE_SIZE*0.2), (int)(Tile.TILE_SIZE*0.2));
        	g.setColor(Color.WHITE);
        	g.drawOval(Math.round(getXPos())+(int)(0.2*Tile.TILE_SIZE),Math.round(getYPos())+(int)(0.2*Tile.TILE_SIZE),(int)(Tile.TILE_SIZE*0.2), (int)(Tile.TILE_SIZE*0.2));
        	break;
        case RIGHT:
        	g.setColor(Color.BLACK);
        	g.fillOval(Math.round(getXPos())+(int)(0.6*Tile.TILE_SIZE),Math.round(getYPos())+(int)(0.2*Tile.TILE_SIZE),(int)(Tile.TILE_SIZE*0.2), (int)(Tile.TILE_SIZE*0.2));
        	g.setColor(Color.WHITE);
        	g.drawOval(Math.round(getXPos())+(int)(0.6*Tile.TILE_SIZE),Math.round(getYPos())+(int)(0.2*Tile.TILE_SIZE),(int)(Tile.TILE_SIZE*0.2), (int)(Tile.TILE_SIZE*0.2));
        	break;
		default:
			break;
        }
    }
}