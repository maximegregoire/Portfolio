package characters;

import game.CurrentLevel;
import game.Level;
import ghosts.GhostMode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import levels.AbstractLevel;
import tiles.EmptyTile;
import tiles.Pill;
import tiles.Tile;
import users.CurrentUser;
import users.GuestProfile;
import users.UserProfile;
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
public class Pacman extends Character {
	private final Level level = CurrentLevel.getInstance();
	private final int STARTING_X; //In tiles
	private final int STARTING_Y; //In tiles
	private float currentX; //In tiles
	private float currentY;	//In tiles
	private int numberOfLives = 3;
	private Direction paintDirection = Direction.RIGHT; //Initial paint orientation for Pacman
	private int pillsEaten = 0;
	
	private boolean isFruitActive;
	private long startSpawningTime; //For the fruit
	private long endSpawningTime; //For the fruit
	private final int INITIAL_FRUIT_SPAWN = 70;
	private final int FRUIT_SPAWN_RATE = 100;
	private final int TOTAL_FRUIT_TIME = 10000; // in ticks; amount of milliseconds is this times MovementTimer's delay
	private final int FRUIT_SCORE = 100; //TODO add different values for each lvl
	
	private ArrayList<PillEatenListener> listeners = new ArrayList<PillEatenListener>();

	/**
	 * Initializes Pacman
	 */
	public Pacman(Tile spawn) {
		super();
		
		this.setDisplay(new PacmanDisplay(this));
		
		this.setDir(Direction.RIGHT);
		
		STARTING_X = spawn.getXPosition() / Tile.TILE_SIZE;
		STARTING_Y = spawn.getYPosition() / Tile.TILE_SIZE;
		
		setxPos(STARTING_X * Tile.TILE_SIZE);
		setyPos(STARTING_Y * Tile.TILE_SIZE);
		
        MovementTimer.getInstance().subscribe(this.timerListener);
	}

	ActionListener timerListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			eatEdibles();
			lookForGhosts();
		}
	};
	
	/**
	 * Allows Pacman to eat Edibles
	 */
	public synchronized void eatEdibles(){
		currentX = this.display.getXPos();
		currentY = this.display.getYPos();
		MazeDisplay m = level.getMazeDisplay();
		EmptyTile actualTile = (EmptyTile) m.getTile(currentX, currentY);
		
		if(actualTile.hasPill()){
			
			//Ate a dot! Tell the ghosts so they can leave the cage
			//Put before the increment because one of the ghosts leaves at 0
			
			for (PillEatenListener pel : listeners) {
				pel.atePill();
			}
			
			m.blinky.ateDot();
			m.pinky.ateDot();
			m.clyde.ateDot();
			m.inky.ateDot();
			
			actualTile.setPill(false);
			level.addToScore(Pill.DOT_POINTS);
		}
		
		//Eat the fruit if there is one on the tile
		if(actualTile.hasFruit()){
			actualTile.setHasFruit(false);
			level.addToScore(FRUIT_SCORE);
		}
		
		//Eat power pill if there is one on the tile
		if(actualTile.hasPowerPill()){
			actualTile.setPowerPill(false);
			m.blinky.getGhostController().setFrightened();
			m.inky.getGhostController().setFrightened();
			m.pinky.getGhostController().setFrightened();
			m.clyde.getGhostController().setFrightened();
		}
	}
	
	public void addPillEatenListener( PillEatenListener pel )
	{
		listeners.add(pel);
	}
	
	/**
	 * Allows collisions between Pacman and the ghosts.
	 */
	public synchronized void lookForGhosts() {
		MazeDisplay m = level.getMazeDisplay();
		
		Tile pacmanTile = m.getTile(currentX, currentY);
		
		Ghost[] arr = {m.blinky, m.clyde, m.inky, m.pinky};
		
		for (Ghost g : arr) {
			if (pacmanTile == m.getTile(g.getxPos(), g.getyPos())) {
				if (g.getGhostController().getMode() == GhostMode.FRIGHTENED) {
					g.reinitialize();
					g.setDotCount(g.getMaxDots());
					level.addToScore(200);
				} else {
					killPacman();
				}
			}
		}
	}
	
	/**
	 * Allows the fruits to spawn at certain time in the game.
	 */
	/*
	public void spawnFruits(){
		
		MazeDisplay m = level.getMazeDisplay();
		EmptyTile fruitTile = m.getFruitSpawn();
		
		if (this.getPillsEaten() % FRUIT_SPAWN_RATE == INITIAL_FRUIT_SPAWN && !(this.isFruitActive())) {
			this.setFruitActive(true);
			fruitTile.setHasFruit(true);
			fruitTile.repaint();
			setStartSpawningTime(System.currentTimeMillis());
		}
		
		if (this.isFruitActive() && (System.currentTimeMillis() - this.getStartSpawningTime()) >= TOTAL_FRUIT_TIME) {
			fruitTile.setHasFruit(false);
			fruitTile.repaint();
			this.setFruitActive(false);
		}
		
	}*/

    /**
     * Return Pacman and the ghosts to their starting position
     */
    public void returnEverythingToStartPosition(){
    	MazeDisplay m = level.getMazeDisplay();
    	
    	m.blinky.reinitialize();
    	m.clyde.reinitialize();
    	m.inky.reinitialize();
    	m.pinky.reinitialize();
    	
    	display.move(STARTING_X*Tile.TILE_SIZE,STARTING_Y*Tile.TILE_SIZE);
    	setNextDirection(Direction.STOPPED);
    	setDir(Direction.STOPPED);
    	
    }
    
    /**
     * Kills Pacman, subtracts one life and return Pacman and the ghosts
     * to their starting positions.
     */
    public void killPacman(){
    	substractOneLife();
		returnEverythingToStartPosition();
    }

	/**
	 * Get Pacman's number of lives
	 */
    
    public int getNumberOfLives() {
		return numberOfLives;
	}

    /**
     * Subtract one life from Pacman's number of lives
     */
	public void substractOneLife() {
		this.numberOfLives -= 1;
		if (this.numberOfLives <= 0) {
			MazeDisplay m = level.getMazeDisplay();
			m.setVisible(false);
			if(CurrentUser.getInstance() instanceof GuestProfile) {
				WindowManager.openMainMenu();
			} else {
				//GameStatistic gameStatistic = new GameStatistic(currentMaze.getGameLevel(), score, 0, false);
				WindowManager.openStatisticDisplay(true);
			}
		}
	}

	/**
	 * Getter for the start spawning time of the fruit.
	 * @return the start spawning time of the fruit
	 */
	public long getStartSpawningTime() {
		return startSpawningTime;
	}

	/**
	 * Setter for the start spawning time of the fruit.
	 * @param the start spawning time of the fruit
	 */
	public void setStartSpawningTime(long startSpawningTime) {
		this.startSpawningTime = startSpawningTime;
	}

	/**
	 * Getter for the end spawning time of the fruit.
	 * @return the end spawning time of the fruit
	 */
	public long getEndSpawningTime() {
		return endSpawningTime;
	}

	/**
	 * Setter for the end spawning time of the fruit.
	 * @param the end spawning time of the fruit
	 */
	public void setEndSpawningTime(long endSpawningTime) {
		this.endSpawningTime = endSpawningTime;
	}

	public boolean isFruitActive() {
		return isFruitActive;
	}

	public void setFruitActive(boolean isFruitActive) {
		this.isFruitActive = isFruitActive;
	}
}