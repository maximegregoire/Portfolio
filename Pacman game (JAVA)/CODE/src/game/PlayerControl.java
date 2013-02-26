package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import characters.Direction;
import characters.MovementTimer;
import characters.Pacman;
import display.WindowManager;

public class PlayerControl extends KeyAdapter {
	private boolean paused = false;
	private Pacman character;
	
	public PlayerControl(Pacman p) {
		this.character = p;
	}
	
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP: this.character.setNextDirection(Direction.UP);
		break;
		case KeyEvent.VK_DOWN: this.character.setNextDirection(Direction.DOWN);
		break;
		case KeyEvent.VK_LEFT: this.character.setNextDirection(Direction.LEFT);
		break;
		case KeyEvent.VK_RIGHT: this.character.setNextDirection(Direction.RIGHT);;
		break;
		case KeyEvent.VK_P: if (paused) {
			MovementTimer.getInstance().start();
		} else {
			MovementTimer.getInstance().stop();
		}
		paused = !paused;
		break;
		case KeyEvent.VK_ESCAPE: if(paused) {
			CurrentLevel.getInstance().endLevel(false);
		}
		break;
		default: return;
		}
	}
}
