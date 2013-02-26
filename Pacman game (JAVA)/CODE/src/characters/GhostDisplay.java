package characters;

import ghosts.GhostMode;

import java.awt.Color;
import java.awt.Graphics;

import tiles.Tile;

public class GhostDisplay extends CharacterDisplay {
	
	private Color color;
	private boolean frightened;

	public GhostDisplay(Character characterModel, Color color) {
		super(characterModel);
		this.color = color;
	}

	   public void paintComponent(Graphics g) {
			if(this.frightened) {
				g.setColor(Color.BLUE);
			} else {
				g.setColor(this.color);
			}

			g.fillRect(Math.round(getXPos()), Math.round(getYPos()), getWidth(),
					getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(Math.round(getXPos()), Math.round(getYPos()), getWidth(),
					getHeight());
		}

	public boolean isFrightened() {
		return frightened;
	}

	public void setFrightened(boolean frightened) {
		this.frightened = frightened;
	}
}
