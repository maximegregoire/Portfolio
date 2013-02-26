package tiles;


/**
 * This class describes an Pill edible object.
 * 
 * @author Antoine Tu
 *
 */
public class Pill extends Edibles {
	public final static int DIMENSION = 10;
	public final static int DOT_POINTS = 10;
	
	/**
	 * Initializes a Pill object with the given coordinates.
	 * @param xPosition	the position of the Pill on the x-axis.
	 * @param yPosition	the position of the Pill on the y-axis.
	 */
	
	public Pill(int xPosition, int yPosition) {
		super(xPosition, yPosition, DOT_POINTS);
	}
	
   }
