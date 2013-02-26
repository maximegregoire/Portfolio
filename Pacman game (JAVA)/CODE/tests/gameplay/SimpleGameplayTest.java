package gameplay;

import static org.junit.Assert.*;

import game.Game;
import game.Level;

import java.awt.Frame;

import org.junit.Test;

import tiles.EmptyTile;
import tiles.Tile;

import levels.*;
import characters.*;
import display.CustomFrame;
import display.WindowManager;

public class SimpleGameplayTest {
	
	@Test
	public void testSmoothMovement() {
		CustomFrame frame = new CustomFrame();
		frame.setSize(850, 750);
		frame.setVisible(false);
		Game game = new Game(frame);
		Level firstLevel = new Level(new Level1(), frame);
		game.play(firstLevel);
		System.out.println("Testing smooth movement method...");
		Pacman pacman = firstLevel.getMazeDisplay().getPacman();
		pacman.setxPos(3);
		pacman.setyPos(3);
		pacman.doSmoothMovement();
		assertTrue(pacman.getxPos() > 3);
		assertTrue(pacman.getyPos() == 3);
	}
	
	@Test
	public void testGhostEatingPacman() {
		CustomFrame frame = new CustomFrame();
		frame.setSize(850, 750);
		frame.setVisible(false);
		Game game = new Game(frame);
		Level firstLevel = new Level(new Level1(), frame);
		game.play(firstLevel);
		System.out.println("Testing ghost eating pacman...");
		Pacman pacman = firstLevel.getMazeDisplay().getPacman();
		Clyde clyde = firstLevel.getMazeDisplay().clyde;
		int pacmanInitialLives = pacman.getNumberOfLives();
		clyde.setxPos(pacman.getxPos()-29);
		clyde.setyPos(pacman.getyPos());
		pacman.setSpeed(3);
		pacman.setNextDirection(Direction.LEFT);
		boolean pacmanDied = false;
		
		pacman.doSmoothMovement();
		
		//The i<30000000 is to give pacman some time to go through the whole line.
		//The NPE is due to the fact that it eventualy runs into a wall
		for (int i = 0; i<300000000; i++){
			if(pacman.getNumberOfLives() < pacmanInitialLives) {
				pacmanDied = true;
				pacman.setDir(Direction.STOPPED);
			}
			
		}
		assertTrue(pacmanDied == true);
	}
	
	@Test
	public void testPacmanEatingPills() {
		CustomFrame frame = new CustomFrame();
		frame.setSize(850, 750);
		frame.setVisible(false);
		Game game = new Game(frame);
		Level firstLevel = new Level(new Level1(), frame);
		game.play(firstLevel);
		System.out.println("Testing ghost eating pacman eating pills");
		Pacman pacman = firstLevel.getMazeDisplay().getPacman();
		pacman.setDir(Direction.RIGHT);
		boolean pacmanAte = false;
		pacman.doSmoothMovement();
		// the i<10000 is to give pacman some time to move
		for (int i = 0; i<10000; i++){
			if(firstLevel.getPillsEaten()>0) pacmanAte = true;
		}
		assertTrue(pacmanAte = true);
	}
	

}
