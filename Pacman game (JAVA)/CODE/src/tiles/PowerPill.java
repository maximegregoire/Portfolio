package tiles;

/**
 * This class describes a PowerPill edible object.
 * 
 * @author Antoine Tu
 *
 */
public class PowerPill extends Edibles {
	private static final int ENERGIZER_POINTS = 50;
	
	/**
	 * Initializes a PowerPill object with the given coordinates.
	 * @param xPosition	the position of the PowerPill on the x-axis.
	 * @param yPosition	the position of the PowerPill on the y-axis.
	 */
	public PowerPill(int xPosition, int yPosition) {
		super(xPosition, yPosition, ENERGIZER_POINTS);
	}
   
   }
