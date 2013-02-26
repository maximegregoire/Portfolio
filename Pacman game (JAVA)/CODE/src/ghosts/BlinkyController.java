package ghosts;

import java.awt.Point;

import characters.Ghost;
import characters.Pacman;

public class BlinkyController extends GhostController {

	public BlinkyController(Ghost g, Pacman p, GhostMode mode,	Point scatterDestination, int[] scatterTimerLengths, int scatterPeriod) {
		super(g, p, mode, scatterDestination, scatterTimerLengths, scatterPeriod);
	}
}
