/**
 * Class concerning the robot's physical components.
 * @author Maxime Grégoire
 * @author Andrew Walker
 * @author Dan Crisan
 */


import lejos.nxt.NXTRegulatedMotor;

public class TwoWheeledRobot {
	public static final double DEFAULT_LEFT_RADIUS = 2.80;
	public static final double DEFAULT_RIGHT_RADIUS = 2.80;
	public static final double DEFAULT_WIDTH = 12.80;
	
	private NXTRegulatedMotor leftWheel, rightWheel;
	private double leftRadius, rightRadius, width;
	private double forwardSpeed, rotationSpeed;
	
	//Constructor
	public TwoWheeledRobot(NXTRegulatedMotor leftWheel,
						   NXTRegulatedMotor rightWheel) {
		this.leftWheel = leftWheel;
		this.rightWheel = rightWheel;
		this.leftRadius = DEFAULT_LEFT_RADIUS;
		this.rightRadius = DEFAULT_RIGHT_RADIUS;
		this.width = DEFAULT_WIDTH;
	}
	
	/**
	 * Getter for the left radius of the wheel.
	 * @return the left radius of the wheel
	 */
	public double getLeftRadius()
	{
		return DEFAULT_LEFT_RADIUS;
	}
	
	/**
	 * Getter for the right radius of the wheel.
	 * @return the right radius of the wheel
	 */
	public double getRightRadius()
	{
		return DEFAULT_RIGHT_RADIUS;
	}
	
	/**
	 * Getter for the width between the wheels of the robot.
	 * @return the width between the wheels of the robot
	 */
	public double getWidth()
	{
		return DEFAULT_WIDTH;
	}
	
	/**
	 * Getter of displacement of the robot from the wheel.
	 * @return the displacement of the robot from the wheel
	 */
	public double getDisplacement() {
		return (leftWheel.getTachoCount() * leftRadius +
				rightWheel.getTachoCount() * rightRadius) *
				Math.PI / 360.0;
	}
	
	/**
	 * Getter of heading of the robot from the wheel.
	 * @return the heading of the robot from the wheel
	 */
	public double getHeading() {
		return (leftWheel.getTachoCount() * leftRadius -
				rightWheel.getTachoCount() * rightRadius) / width;
	}
	
	/**
	 * Getter of displacement and the heading of the robot from the wheel, as an array.
	 * @return displacement and the heading of the robot from the wheel, as an array
	 */
	public void getDisplacementAndHeading(double [] data) {
		int leftTacho, rightTacho;
		leftTacho = leftWheel.getTachoCount();
		rightTacho = rightWheel.getTachoCount();
		
		data[0] = (leftTacho * leftRadius + rightTacho * rightRadius) *	Math.PI / 360.0;
		data[1] = (leftTacho * leftRadius - rightTacho * rightRadius) / width;
	}
	
	/**
	 * Setter for the forward speed of the robot.
	 * @param speed
	 */
	public void setForwardSpeed(double speed) {
		forwardSpeed = speed;
		rotationSpeed = 0;
		setSpeeds(forwardSpeed, rotationSpeed);
	}
	
	/**
	 * Setter for the rotation speed of the robot.
	 * @param speed
	 */
	public void setRotationSpeed(double speed) {
		forwardSpeed = 0;
		rotationSpeed = speed;
		setSpeeds(forwardSpeed, rotationSpeed);
	}
	
	/**
	 * Setter for the forward and rotation speed of the robot.
	 * @param forwardSpeed
	 * @param rotationalSpeed
	 */
	public void setSpeeds(double forwardSpeed, double rotationalSpeed) {
		double leftSpeed, rightSpeed;

		this.forwardSpeed = forwardSpeed;
		this.rotationSpeed = rotationalSpeed;

		leftSpeed = (forwardSpeed + rotationalSpeed * width * Math.PI / 360.0) *
				180.0 / (leftRadius * Math.PI);
		rightSpeed = (forwardSpeed - rotationalSpeed * width * Math.PI / 360.0) *
				180.0 / (rightRadius * Math.PI);

		// set motor directions
		if (leftSpeed > 0.0)
			leftWheel.forward();
		else {
			leftWheel.backward();
			leftSpeed = -leftSpeed;
		}
		
		if (rightSpeed > 0.0)
			rightWheel.forward();
		else {
			rightWheel.backward();
			rightSpeed = -rightSpeed;
		}
		
		// set motor speeds
		if (leftSpeed > 900.0)
			leftWheel.setSpeed(900);
		else
			leftWheel.setSpeed((int)leftSpeed);
		
		if (rightSpeed > 900.0)
			rightWheel.setSpeed(900);
		else
			rightWheel.setSpeed((int)rightSpeed);
	}
	
	/**
	 * Stops the movement of the wheels of the robot.
	 */
	public void stop()
	{
		leftWheel.stop(true);
		rightWheel.stop(false);
	}
}
