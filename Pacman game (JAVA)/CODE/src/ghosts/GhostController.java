package ghosts;

import game.CurrentLevel;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import tiles.Tile;
import tiles.WallTile;
import characters.Direction;
import characters.Ghost;
import characters.GhostDisplay;
import characters.MovementTimer;
import characters.Pacman;
import display.MazeDisplay;

public abstract class GhostController {
	protected GhostMode mode = GhostMode.SCATTERING;
	protected GhostMode previousMode;
	protected final Pacman p;
	protected final Ghost g;
	protected Point scatterDestination;
	Timer scatterTimer;
	Timer chaseTimer;
	Timer frightenedTimer;
	private final int[] scatterTimerLengths;
	private int scatterPeriod = 0;
	
	private static final Direction[] DIRECTIONS_AS_ARRAY = Direction.values(); //Used to generate random direction when frightened
	
	public GhostController(Ghost ghost, Pacman p, GhostMode initmode, Point scatterDestination, 
			final int[] scatterTimerLengths, int frightenedTimerLength) {
		this.p = p;
		this.g = ghost;
		this.mode = initmode;
		this.scatterDestination = scatterDestination;
		this.scatterDestination.x *= Tile.TILE_SIZE;
		this.scatterDestination.y *= Tile.TILE_SIZE;
		this.scatterTimerLengths = scatterTimerLengths;
		
		//Move is called every iteration of the action timer
		//This is bad practice but since move is deterministic and well-formed for all inputs
		//It does not backfire.
		MovementTimer.getInstance().subscribe(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				move();
			}
		});
		
		scatterTimer = new Timer(scatterTimerLengths[0], new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scatterPeriod++;
				try {
					scatterTimer.setDelay(scatterTimerLengths[scatterPeriod]);
				} catch (ArrayIndexOutOfBoundsException except) {
					scatterTimer.setDelay(0);
				}
				
				scatterTimer.stop();
				chaseTimer.start();
				mode = GhostMode.CHASING;
			}
		});
		
		chaseTimer = new Timer(20000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chaseTimer.stop();
				scatterTimer.start();
				mode = GhostMode.SCATTERING;
			}
		});
		
		frightenedTimer = new Timer(frightenedTimerLength, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mode = previousMode;
				GhostDisplay gd = (GhostDisplay) g.getDisplay();
				gd.setFrightened(false);
				if(mode == GhostMode.CHASING) {
					chaseTimer.start();
				} else {
					scatterTimer.start();
				}
				frightenedTimer.stop();
			}
		});
		
		if(mode == GhostMode.SCATTERING) {
			scatterTimer.start();
		}
	}
	
	public void setFrightened() {
		this.previousMode = this.mode;
		this.mode = GhostMode.FRIGHTENED;
		GhostDisplay gd = (GhostDisplay) this.g.getDisplay();
		gd.setFrightened(true);
		this.frightenedTimer.restart();
		this.scatterTimer.stop();
		this.chaseTimer.stop();
	}

	public GhostMode getMode() {
		return mode;
	}

	public void setMode(GhostMode mode) {
		this.mode = mode;
	}
	
	private final void move() {
		switch (mode) {
		case CAGED:
			cagedMove();
			break;
		case CHASING:
			chasingMove();
			break;
		case FRIGHTENED:
			frightenedMove();
			break;
		case SCATTERING:
			scatteringMove();
			break;
		/*default:
			g.setDir(Direction.STOPPED);*/
		case LEAVINGCAGE:
			MazeDisplay m = CurrentLevel.getInstance().getMazeDisplay();
			moveTo(m.getCageDoor());
			break;
		}
	}
	
	protected final void moveTo(int x, int y) { //In pixels
		MazeDisplay m = CurrentLevel.getInstance().getMazeDisplay();
		
		Tile target = m.getTile((float) x, (float) y);
		
		moveTo(target);
	}
	
	protected final void moveTo(Tile target) {
		
		ArrayList<Direction> Directions = new ArrayList<Direction>(4);
		
		//In order of priority; when two options are equivalent, prefer UP, then LEFT, then DOWN, then RIGHT
		Directions.add(Direction.UP);
		Directions.add(Direction.LEFT);
		Directions.add(Direction.DOWN);
		Directions.add(Direction.RIGHT);
		
		//Remove opposite directions
		switch (g.getDir()) {
		case DOWN:
			Directions.remove(Direction.UP);
			break;
		case LEFT:
			Directions.remove(Direction.RIGHT);
			break;
		case RIGHT:
			Directions.remove(Direction.LEFT);
			break;
		case UP:
			Directions.remove(Direction.DOWN);
			break;
		case STOPPED:
			break;
		}
		
		MazeDisplay m = CurrentLevel.getInstance().getMazeDisplay();
		
		//forward is the next tile
		Tile forward = m.getTileInDirection(this.g.getDisplay().getXPos(), this.g.getDisplay().getYPos(), g.getDir());
		
		//If forward is a wall then going forward is clearly not an option
		if (forward.isWall()) {
			Directions.remove(g.getDir());
			forward = g.getDisplay().getTile();
			//forward = currentMaze.getTileInDirection(this.g.getXPos(), this.g.getYPos(), g.getNextDirection());
		}
		
		ArrayList<Float> tileDistances = new ArrayList<Float>();
		
		//For all potential directions that don't have walls, stick the distance between them and the target tile in an array
		for (int i = 0; i < Directions.size(); i++){
			Tile tile = m.getTileInDirection(forward, Directions.get(i));
			if (!(tile.isWall())) {
				//get the distance between the potential tile we could be moving on to and the target
				tileDistances.add(Tile.distance(m.getTileInDirection(forward, Directions.get(i)), target));
			} else {
				//This maintains the invariant that tileDistances[i] is the distance between the target and the tile in direction Directions[i]
				Directions.remove(i);
				i--;
			}
		}
		
		//Which index has the smallest distance to target?
		
		int minDistanceIndex = 0;

		for (int i = 1; i < Directions.size(); i++) {
			if (tileDistances.get(i) < tileDistances.get(minDistanceIndex)) {
				minDistanceIndex = i;
			}
		}
		
		try {
			Tile tile = m.getTileInDirection(forward, Directions.get(minDistanceIndex));
			g.setNextDirection(Directions.get(minDistanceIndex));
		} catch (IndexOutOfBoundsException e) {
			randomMove();
		}
		
	}
	
	protected void scatteringMove() {
		moveTo(scatterDestination.x, scatterDestination.y);
	}
	
	protected void chasingMove() {
		moveTo((int) p.getDisplay().getXPos(), (int) p.getDisplay().getYPos());
	}

	protected void frightenedMove() {
		if(this.previousMode == GhostMode.CAGED){
			this.setMode(GhostMode.CAGED);
			this.cagedMove();			
		} else {
		randomMove();
		}
	}
	
	//Only change direction if you're hitting a wall; the cage is a line so movement is restricted to left and right
	protected void cagedMove() {
		MazeDisplay m = CurrentLevel.getInstance().getMazeDisplay();
		if(m.getTileInDirection(g.getDisplay().getTile(), Direction.LEFT) instanceof WallTile) {
			g.setNextDirection(Direction.RIGHT);
		} else if (m.getTileInDirection(g.getDisplay().getTile(), Direction.RIGHT) instanceof WallTile) {
			g.setNextDirection(Direction.LEFT);
		}
	}
	
	private static Direction randomDirection() {
		return DIRECTIONS_AS_ARRAY[new Random().nextInt(DIRECTIONS_AS_ARRAY.length)];
	}
	
	//Any direction is good except reverse!
	protected void randomMove() {
		Direction randomDir = randomDirection();
		
		//Check if reverse direction (not allowed)
		Direction forbiddenDir = null;
		
		switch(randomDir) {
		case DOWN:
			forbiddenDir = Direction.UP;
			break;
		case LEFT:
			forbiddenDir = Direction.RIGHT;
			break;
		case RIGHT:
			forbiddenDir = Direction.LEFT;
			break;
		case STOPPED:
			break;
		case UP:
			forbiddenDir = Direction.DOWN;
			break;
		default:
			break;
		}
		
		//If they're equivalent, re-start the function (the inner construct is functionally equivalent to goto)
		if (forbiddenDir != null && forbiddenDir == randomDir) {
			cagedMove();
			return;
		}
		
		g.setNextDirection(randomDir);
	}
}
