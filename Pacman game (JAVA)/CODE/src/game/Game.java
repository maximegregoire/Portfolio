package game;

import java.util.ArrayList;

import users.CurrentUser;
import users.GameStatistics;
import users.LevelStatistics;
import display.CustomFrame;
import display.WindowManager;

public class Game {
	private ArrayList<Level> levels;
	private CustomFrame frame;
	
	public Game(CustomFrame frame) {
		CurrentGame.setInstance(this);
		this.setFrame(frame);
	}
	
	public void play(Level firstLevel) {
		this.levels = new ArrayList<Level>();
		levels.add(firstLevel);
		firstLevel.play();
	}

	public CustomFrame getFrame() {
		return frame;
	}

	public void setFrame(CustomFrame frame) {
		this.frame = frame;
	}
	
	public GameStatistics exportStatistics() {
		ArrayList<LevelStatistics> levelstats = new ArrayList<LevelStatistics>();
		
		for(Level l : levels) {
			levelstats.add(l.exportStatistics());
		}
		
		return new GameStatistics(levelstats);
	}

	public void loadNextLevel() {
		int lastLevelNumber = levels.get(levels.size() - 1).getLevelNumber();
		
		CurrentUser.getInstance().setLevelUnlocked(lastLevelNumber + 1);
		
		this.endGame(); //Swap this out for level change instead
		}

	void endGame() {
		WindowManager.openStatisticDisplay(true);
	}
	
	
}
