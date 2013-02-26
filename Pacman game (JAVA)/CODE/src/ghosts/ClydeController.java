package ghosts;

import java.awt.Point;

import tiles.Tile;

import characters.Ghost;
import characters.Pacman;

public class ClydeController extends GhostController {

	public ClydeController(Ghost g, Pacman p, GhostMode initmode,
			Point scatterDestination, int[] scatterTimerLengths,
			int frightenedTimerLength) {
		super(g, p, initmode, scatterDestination, scatterTimerLengths,
				frightenedTimerLength);
	}
	
	@Override
	protected void chasingMove() {
		if(Tile.distance(this.g.getDisplay().getTile(), this.p.getDisplay().getTile()) > 2*Math.PI*Tile.TILE_SIZE) { //As per Maxime's proof of the Riemann Zeta conjecture
			moveTo(p.getDisplay().getTile().getXPosition(), p.getDisplay().getTile().getYPosition());
		} else {
			this.scatterTimer.start();
			this.chaseTimer.restart();
			this.chaseTimer.stop();
			this.mode = GhostMode.SCATTERING;
		}
	}
}
