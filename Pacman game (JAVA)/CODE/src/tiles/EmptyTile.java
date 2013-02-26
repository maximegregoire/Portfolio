package tiles;

/**
 * This class describes a Tile object that is navigable. It might or might not have an item on it (pill, power pill, fruit).
 * 
 * <p> objects of this class can be added as PillEatenListeners to Pacman, and once enough pills have been eaten a fruit will spawn.
 * 
 * @author Antoine Tu
 * @author Carl Patenaude Poulin
 * @author Maxime Grégoire
 */
import game.CurrentLevel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import characters.PillEatenListener;

public class EmptyTile extends Tile implements PillEatenListener, ActionListener {

	private static final int FRUIT_SPAWN_RATE = 100;
	private static final int INITIAL_FRUIT_SPAWN = 70;
	private boolean hasPill;
	private boolean hasFruit = false;
	private boolean hasPowerPill = false;
	private Timer fruitTimer = new Timer(10000, this);
	
	/**
	 * Initializes an EmptyTile with the given coordinates.
	 * @param xPosition	the position of the EmptyTile on the x-axis.
	 * @param yPosition	the position of the EmptyTile on the y-axis.
	 */
	public EmptyTile(int xPosition, int yPosition, boolean hasPill) {
		super(xPosition, yPosition);
		this.setWall(false);
		if(hasPill){
			this.setPill(true);

		}

	}

	/**
	 * Draws the EmptyTile object.
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.GRAY);
		g.fillRect(Math.round(getXPosition()), Math.round(getYPosition()), getWidth(), getHeight());
		g.setColor(Color.GRAY);
		g.drawRect(Math.round(getXPosition()), Math.round(getYPosition()), getWidth(), getHeight());
		if(this.hasFruit()){
			int dimension = 20; //TODO fruit dimension
			int tileSize = Tile.TILE_SIZE;
			g.setColor(Color.RED);
			g.fillOval(Math.round(getXPosition()+((tileSize-dimension)/2)), Math.round(getYPosition()+((tileSize-dimension)/2)), dimension, dimension);
			g.setColor(Color.BLUE);
			g.drawOval(Math.round(getXPosition()+((tileSize-dimension)/2)), Math.round(getYPosition()+((tileSize-dimension)/2)), dimension, dimension);
		} else if(this.hasPill()){
			int dimension = Pill.DIMENSION;
			int tileSize = Tile.TILE_SIZE;
			g.setColor(Color.WHITE);
			g.fillOval(Math.round(getXPosition()+((tileSize-dimension)/2)), Math.round(getYPosition()+((tileSize-dimension)/2)), dimension, dimension);
			g.setColor(Color.BLACK);
			g.drawOval(Math.round(getXPosition()+((tileSize-dimension)/2)), Math.round(getYPosition()+((tileSize-dimension)/2)), dimension, dimension);
		} else if(this.hasPowerPill()){
			int dimension = (int)(Tile.TILE_SIZE*0.8);
			int tileSize = Tile.TILE_SIZE;
			g.setColor(Color.WHITE);
			g.fillOval(Math.round(getXPosition()+((tileSize-dimension)/2)), Math.round(getYPosition()+((tileSize-dimension)/2)), dimension, dimension);
			g.setColor(Color.BLACK);
			g.drawOval(Math.round(getXPosition()+((tileSize-dimension)/2)), Math.round(getYPosition()+((tileSize-dimension)/2)), dimension, dimension);
		}	
	}


	@Override
	public void atePill() {
		if (CurrentLevel.getInstance().getPillsEaten() % FRUIT_SPAWN_RATE == INITIAL_FRUIT_SPAWN) {
			this.setHasFruit(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		fruitTimer.stop();
		this.setHasFruit(false);
	}
	
	public boolean hasPill() {
		return hasPill;
	}
	
	public void setPill(boolean hasPill) {
		this.hasPill = hasPill;
	}
	
	public boolean hasFruit() {
		return hasFruit;
	}

	public void setHasFruit(boolean hasFruit) {
		this.hasFruit = hasFruit;
		if(hasFruit) {
			fruitTimer.restart();
		} else {
			fruitTimer.stop();
		}
	}
	
	public boolean hasPowerPill() {
		return hasPowerPill;
	}

	public void setPowerPill(boolean hasPowerPill) {
		this.hasPowerPill = hasPowerPill;
	}
}
