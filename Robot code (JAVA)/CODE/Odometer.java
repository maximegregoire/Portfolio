/**
 * Class concerning the odometry of our robot, given during laboratory 3 by the professors and
 * the teaching assistants.
 */


import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Timer;
import lejos.util.TimerListener;

public class Odometer implements TimerListener {
	public static final int DEFAULT_PERIOD = 25;
	private TwoWheeledRobot robot;
	private Timer odometerTimer;
	private Navigation nav;
	private Object lock;
	private double x, y, theta;
	private double [] oldDH, dDH;
	
	//Constructor
	public Odometer(TwoWheeledRobot robot, boolean start, NXTRegulatedMotor leftWheel, NXTRegulatedMotor rightWheel) {
		// initialise variables
		this.robot = robot;
		this.nav = new Navigation(this, leftWheel, rightWheel);
		odometerTimer = new Timer(DEFAULT_PERIOD, this);
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		oldDH = new double [2];
		dDH = new double [2];
		lock = new Object();
		
		// start the odometer immediately, if necessary
		if (start)
			odometerTimer.start();
	}
	
	public void timedOut() {
		robot.getDisplacementAndHeading(dDH);
		dDH[0] -= oldDH[0];
		dDH[1] -= oldDH[1];
		
		// update the position in a critical region
		synchronized (lock) {
			theta += dDH[1];
			theta = fixDegAngle(theta);
			
			x += dDH[0] * Math.sin(Math.toRadians(theta));
			y += dDH[0] * Math.cos(Math.toRadians(theta));
		}
		
		oldDH[0] += dDH[0];
		oldDH[1] += dDH[1];
	}
	
	/**
	 * Getter for the actual position. 
	 * @param pos
	 */
	public void getPosition(double [] pos) {
		synchronized (lock) {
			pos[0] = x;
			pos[1] = y;
			pos[2] = theta;
		}
	}
	
	/**
	 * Getter for the robot object.
	 * @return the robot object
	 */
	public TwoWheeledRobot getTwoWheeledRobot() {
		return robot;
	}
	
	/**
	 * Getter for the robot's navigation.
	 * @return the robot's navigation
	 */
	public Navigation getNavigation() {
		return this.nav;
	}
	
	/**
	 * Setter for the position of the odometer, according to a boolean array.
	 * @param pos
	 * @param update
	 */
	public void setPosition(double [] pos, boolean [] update) {
		synchronized (lock) {
			if (update[0]) x = pos[0];
			if (update[1]) y = pos[1];
			if (update[2]) theta = pos[2];
		}
	}
	
	/**
	 * Returns an angle between 0 and 360 degrees if the angle is above or below.
	 * @param angle
	 * @return an angle between 0 and 360 degrees
	 */
	public static double fixDegAngle(double angle) {		
		if (angle < 0.0)
			angle = 360.0 + (angle % 360.0);
		
		return angle % 360.0;
	}
	
	/**
	 * Returns the minimum angle from an angle to a desired angle.
	 * @param a
	 * @param b
	 * @return the minimum angle between a and b
	 */
	public static double minimumAngleFromTo(double a, double b) {
		double d = fixDegAngle(b - a);
		
		if (d < 180.0)
			return d;
		else
			return d - 360.0;
	}
}
