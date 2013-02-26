package game;

import java.util.ArrayList;

import levels.AbstractLevel;
import users.LevelStatistics;
import characters.Character;
import characters.Ghost;
import characters.MovementTimer;
import characters.Pacman;
import characters.PillEatenListener;
import display.CustomFrame;
import display.MazeDisplay;

public class Level {
	int score = 0;
	int remainingLives = 3;
	AbstractLevel levelModel;
	MazeDisplay mazeDisplay;
	CustomFrame frame;
	
	private int pillsEaten = 0;
	private int totalPills;
	private boolean won = false;
	
	private static final int MS_PER_REFRESH = 20;
	
	public Level(AbstractLevel level, CustomFrame frame) {
		CurrentLevel.setInstance(this);
		//We re-initialize the timer because it might still
		//have listeners left from a past level
		new MovementTimer(MS_PER_REFRESH);
		
		this.levelModel = level;
		this.mazeDisplay = new MazeDisplay(level.getMazeMap(), new ArrayList<Character>());
		
		this.totalPills = mazeDisplay.getNumberOfPills();
		
		this.mazeDisplay.getPacman().addPillEatenListener(new PillEatenListener() {
			@Override
			public void atePill() {
				pillsEaten++;
				if (pillsEaten == totalPills) {
					endLevel(true);
				}
			}
		});
		
		this.mazeDisplay.clyde.setSpeed(level.getGhostSpeed()*5f);
		this.mazeDisplay.inky.setSpeed(level.getGhostSpeed()*5f);
		this.mazeDisplay.pinky.setSpeed(level.getGhostSpeed()*5f);
		this.mazeDisplay.blinky.setSpeed(level.getGhostSpeed()*5f);
		this.mazeDisplay.getPacman().setSpeed(level.getPacmanSpeed()*5f);
		System.out.println(level.getPacmanSpeed()*5f);
		
		this.mazeDisplay.getPacman().addPillEatenListener(this.mazeDisplay.getFruitSpawn());
		this.frame = frame;
	}
	
	public MazeDisplay getMazeDisplay() {
		return this.mazeDisplay;
	}
	
	public LevelStatistics getStatistics(boolean won) {
		return new LevelStatistics(levelModel.getLevelNumber(), score, remainingLives, won);
	}
	
	public void play() {
		this.frame.dispose();
		this.frame = new CustomFrame();
		
		//Add maze to frame
		this.frame.add(this.getMazeDisplay());
		frame.setSize(this.getMazeDisplay().getPreferredSize());
		
		//Give control to the player
		this.mazeDisplay.addKeyListener(new PlayerControl(this.mazeDisplay.getPacman()));
		
		//frame.repaint();
		
		this.frame.setVisible(true);
		MovementTimer.getInstance().start();
	}
	
	public LevelStatistics exportStatistics() {
		return new LevelStatistics(this.getLevelNumber(), this.score, this.remainingLives, this.won);
	}
	
	public void addToScore(int score) {
		this.score += score;
		System.out.println("" + this.score);
	}

	public int getPillsEaten() {
		return pillsEaten;
	}

	public void setPillsEaten(int pillsEaten) {
		this.pillsEaten = pillsEaten;
	}
	
	public void endLevel(boolean won) {
		this.frame.dispose();
		if (won) {
			CurrentGame.getInstance().loadNextLevel();
		} else {
			CurrentGame.getInstance().endGame();
		}
		
	}
	
	public int getLevelNumber() {
		return this.levelModel.getLevelNumber();
	}
}
