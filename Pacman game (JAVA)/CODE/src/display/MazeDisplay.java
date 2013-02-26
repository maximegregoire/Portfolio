/**
 * This class defines a pacman maze, which is responsible for
 * initializing and displaying the characters and tiles.
 * @author Carl Patenaude Poulin
 * @author Maxime Gregoire
 * @author Matthew Beirouti
 *
 */
	
package display;

import ghosts.BlinkyController;
import ghosts.GhostMode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

import javax.swing.JPanel;

import tiles.EmptyTile;
import tiles.Tile;
import tiles.WallTile;
import characters.Blinky;
import characters.Character;
import characters.Clyde;
import characters.Direction;
import characters.Inky;
import characters.Pacman;
import characters.Pinky;

public class MazeDisplay extends JPanel {

	List<Character> characters;
	Pacman pacman;
	public Clyde clyde;
	public Inky inky;
	public Pinky pinky;
	public Blinky blinky;
	
	Tile[][] tileMap;
	Tile cageDoor, pacmanSpawn, fruitSpawn;
	
	private int numberOfPills;
	private int numberOfTilesX;
	private int numberOfTilesY;
	
	private static final int MS_PER_REFRESH = 20;
	private static final int REFRESH_FREQUENCY = 1000 / MS_PER_REFRESH;
	
	/**
	 * Loads the map of the maze according to the provided map
	 * @param mazeMap an array of integers that represent the map; see levels package for an example.
	 * @param characters the character objects that are to be represented in the maze.
	 */
	
	public MazeDisplay(int[][] mazeMap, List<Character> characters) {
		tileMap = mazeMapToTilePlan(mazeMap);
		numberOfTilesY = tileMap[0].length;
		numberOfTilesX = tileMap.length;
		
		this.characters = characters;
		
    	//Set to no layout manager; this way, components aren't automatically displaced
    	setLayout(null);
    	
    	//Move to level class
    	
    	
    	int[] scatterTimerLengths = {7000, 7000, 5000, 5000}; //Change both to level dependent values
    	int frightenedLength = 10000;
    	
    	Tile blinkyTile = this.getTileInDirection(cageDoor, Direction.UP);
    	Tile pinkyTile = this.getTileInDirection(cageDoor, Direction.DOWN);
    	Tile clydeTile = this.getTileInDirection(pinkyTile, Direction.RIGHT);
    	Tile inkyTile = this.getTileInDirection(pinkyTile, Direction.LEFT);
    	
    	int clydeDots = 60;
    	int inkyDots = 30;
    	
    	pacman = new Pacman(pacmanSpawn);
    	pacman.setSpeed(5);
    	clyde = new Clyde(clydeTile, clydeDots);
    	clyde.setGhostController(new BlinkyController(clyde, pacman, GhostMode.CAGED, new Point(numberOfTilesX, 1), scatterTimerLengths, frightenedLength));
    	clyde.setSpeed(1);
    	inky = new Inky(inkyTile, inkyDots);
    	inky.setGhostController(new BlinkyController(inky, pacman, GhostMode.CAGED, new Point(numberOfTilesX, 1), scatterTimerLengths, frightenedLength));
    	inky.setSpeed(1);
    	pinky = new Pinky(pinkyTile, 0);
    	pinky.setGhostController(new BlinkyController(pinky, pacman, GhostMode.CAGED, new Point(numberOfTilesX, 1), scatterTimerLengths, frightenedLength));
    	pinky.setSpeed(1);
    	blinky = new Blinky(blinkyTile, -1);
    	blinky.setGhostController(new BlinkyController(blinky, pacman, GhostMode.SCATTERING, new Point(numberOfTilesX - 1, 1), scatterTimerLengths, frightenedLength));
    	blinky.setSpeed(1);
    	
    	// Add the characters
        this.add(pacman.getDisplay());
        this.add(clyde.getDisplay());
        this.add(inky.getDisplay());
        this.add(pinky.getDisplay());
        this.add(blinky.getDisplay());
        
        characters.add(pacman);
        characters.add(clyde);
        characters.add(inky);
        characters.add(pinky);
        characters.add(blinky);
        
        this.requestFocusInWindow();
    }
	
	private Tile[][] mazeMapToTilePlan(int[][] mazeMap) {
		
        final int TILE_WITH_PILL = 0;
        final int WALL_TILE = 1;
        final int EMPTY_TILE = 2;
        final int FRUIT_TILE = 3;
        final int CAGE_DOOR = 4;
        final int PACMAN_SPAWN = 5;
        final int POWERPILL = 6;
        //Numbers of edibles
        numberOfPills = 0;
        
        Tile[][] res = new Tile[mazeMap[0].length][];
        
        for (int x = 0; x < mazeMap[0].length; x++) { //Iterate first horizontally,
        	
        	res[x] = new Tile[mazeMap.length];
        	
        	for (int y = 0; y < mazeMap.length; y++){ //Then iterate vertically
        		
        		int xcoord = x*(Tile.TILE_SIZE);
        		int ycoord = y*(Tile.TILE_SIZE);
        		
        		//tileMap is the transposition of the mazeMap matrix,
        		//with integers translated to their corresponding tiles.
        		//Side-effects include the definition of several important tiles
        		//Such as the cage door and the spawning points of Pacman and the fruits
        		
        		Tile currentTile;
        		
        		switch (mazeMap[y][x]){ 
        		case TILE_WITH_PILL:  
        			currentTile = new EmptyTile(xcoord, ycoord, true); 
        			numberOfPills++; 
        			break;
        		case WALL_TILE: 
        			currentTile = new WallTile(xcoord, ycoord); 
        			break;
        		case EMPTY_TILE: 
        			currentTile = new EmptyTile(xcoord, ycoord, false); 
        			break;
        		case FRUIT_TILE: 
        			fruitSpawn = new EmptyTile(xcoord, ycoord, false);
        			currentTile = fruitSpawn;
        			break;
        		case CAGE_DOOR:
        			cageDoor = new EmptyTile(xcoord, ycoord, false);
        			currentTile = cageDoor;
        			break;
        		case PACMAN_SPAWN:
        			pacmanSpawn = new EmptyTile(xcoord, ycoord, false);
        			currentTile = pacmanSpawn;
        			break;
        		case POWERPILL:
        			EmptyTile tmp = new EmptyTile(xcoord, ycoord, false);
        			tmp.setPowerPill(true);
        			currentTile = tmp;
        			break;
        		default: throw new RuntimeException("Tile type unimplemented in maze constructor: " + mazeMap[y][x]);
        		}
        		
        		res[x][y] = currentTile;
        		this.add(currentTile);
        	}
        }
        return res;
	}

	public Dimension getPreferredSize() {
		return new Dimension(800, 800);
	}

	/**
	 * Draws the maze and objects within. Called automatically when repaint() is called.
	 */
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		setBackground(Color.BLACK);
		
		//this.paintChildren(g); //doesn't work

		for(Tile[] tileArray : tileMap) {
			for(Tile tile : tileArray) {
				tile.paint(g);
			}
		}
		
		for(Character character : characters) {
			character.getDisplay().paint(g);
		}
	}
	
	/**
	 * Converts a pixel coordinate to tile coordinates.
	 * @param a the pixel coordinate.
	 * @return an integer representing the tile coordinate.
	 */
	private int pixelCoordToTileCoord(float a) {
		return (Math.round(a)) / Tile.TILE_SIZE;
	}
	
	/**
	 * Gets the tile at the given coordinates.
	 * @param x the x-coordinate of the tile.
	 * @param y the y-coordinate of the time.
	 * @return a Tile object.
	 */
	public Tile getTile(float x, float y) {
		return getTile(pixelCoordToTileCoord(x), pixelCoordToTileCoord(y));
	}
	
	/**
	 * Gets the tile at the given coordinates.
	 * @param x the x-coordinate of the tile.
	 * @param y the y-coordinate of the time.
	 * @return a Tile object.
	 */
	public Tile getTile(int x, int y) {
		return tileMap[mod(x, numberOfTilesX)][mod(y, numberOfTilesY)];
	}
	
	/**
	 * Gets the tile from the given coordinates and in the direction specified.
	 * @param x the x-coordinate of the tile.
	 * @param y the y-coordinate of the time.
	 * @param d the direction to look for the tile.
	 * @return a Tile object.
	 */
	public Tile getTileInDirection(int x, int y, Direction d) {
		switch(d) {
		case UP: y -= 1; break;
		case DOWN: y += 1; break;
		case LEFT: x -= 1; break;
		case RIGHT: x += 1; break;
		case STOPPED: return getTile(x, y);
		}
		
		return getTile(x, y);
	}
	
	/**
	 * Gets the tile from the given coordinates and in the direction specified.
	 * @param x the x-coordinate of the tile.
	 * @param y the y-coordinate of the time.
	 * @param d the direction to look for the tile.
	 * @return a Tile object.
	 */
	public Tile getTileInDirection(float x, float y, Direction d) {
		return getTileInDirection(pixelCoordToTileCoord(x), pixelCoordToTileCoord(y), d);
	}
	
	/**
	 * Gets the tile from the given Tile and in the direction specified.
	 * @param t the tile to look from.
	 * @param d the direction to look for the tile.
	 * @return a Tile object.
	 */
	public Tile getTileInDirection(Tile t, Direction d) {
		return getTileInDirection((float) t.getXPosition(), (float) t.getYPosition(), d);
	}
	
	/**
	 * Gets the number of tiles in the x axis of the map.
	 * @return an integer representing the number of tiles.
	 */
	public int getNumberOfTilesX() {
		return numberOfTilesX;
	}

	/**
	 * Sets the number of tiles in the x axis of the map.
	 * @param numberOfTilesX the number of tiles to set.
	 */
	public void setNumberOfTilesX(int numberOfTilesX) {
		this.numberOfTilesX = numberOfTilesX;
	}

	/**
	 * Gets the number of tiles in the y axis of the map.
	 * @return an integer representing the number of tiles.
	 */
	public int getNumberOfTilesY() {
		return numberOfTilesY;
	}

	/**
	 * Sets the number of tiles in the y axis of the map.
	 * @param numberOfTilesY the number of tiles to set.
	 */
	public void setNumberOfTilesY(int numberOfTilesY) {
		this.numberOfTilesY = numberOfTilesY;
	}

	/**
	 * Gets the number of pills.
	 * @return the number of pills.
	 */
	public int getNumberOfPills() {
		return numberOfPills;
	}

	/**
	 * Sets the number of pills.
	 * @param numberOfPills the number of pills to set.
	 */
	public void setNumberOfPills(int numberOfPills) {
		this.numberOfPills = numberOfPills;
	}
	
	/**
	 * Helper method that allows to recalculate the coordinates of the character
	 * to wrap around the map. This effectively creates the tunnel warping effect in the game.
	 * Since Java doesn't wrap around negative numbers, we have to implement our own version of mod.
	 * @param x the current number
	 * @param n the number by which we want to modulo
	 * @return the calculated modulo
	 */
	
	public int mod(int x, int n){
		int res = x%n;
		if(res<0) res+=n;
		return res;
	}

	public static int getRefreshFrequency() {
		return REFRESH_FREQUENCY;
	}
	
	/**
	 * Gets the ghost cage door.
	 * @return a Tile object representing the cage door.
	 */
	
	public Tile getCageDoor() {
		return cageDoor;
	}

	/**
	 * Gets the tile on which fruits spawn
	 * @return a Tile object
	 */
	
	public EmptyTile getFruitSpawn() {
		return (EmptyTile) fruitSpawn;
	}

	public Pacman getPacman() {
		return this.pacman;
	}
	
	@Override 
	 public boolean isFocusable() { return true; }
}