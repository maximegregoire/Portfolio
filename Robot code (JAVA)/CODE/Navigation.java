/**
 * Class concerning everything related to the movement of the robot.
 * @author Maxime Grégoire
 * @author Andrew Walker
 * @author Dan Crisan
 */

import lejos.nxt.*;
import lejos.util.Delay;

public class Navigation {
	private Odometer odo;
	private TwoWheeledRobot bot;
	private NXTRegulatedMotor leftWheel;
	private NXTRegulatedMotor rightWheel;
	
	private static double LEFT_RADIUS;	
	private static double RIGHT_RADIUS;
	private static final int FORWARD_SPEED = 100;
	private static final int ROTATION_SPEED = 80;
	private static double WIDTH;
	private static double[] position = new double[3];	//will hold x, y and theta values from the odometer
	private double xDest = 0.0;
	private double yDest = 0.0;
	private UltrasonicSensor forwardUSSensor = new UltrasonicSensor(SensorPort.S3);
	private UltrasonicSensor rightUSSensor;
	private UltrasonicSensor leftUSSensor;
	private ArmMovement arm;
	private boolean done = false;
	private boolean isStillAVirgin = true;
	private double xStart = 0;
	private double yStart = 0;
	private double xPD = 0;
	private double yPD = 0;
	private double startHeading;
	public static double xCoordCorner = 0.0;
	public static double yCoordCorner = 0.0;
	public static double cornerHeading = 0.0;
	public static double xCoordObstacle = 0.0;
	public static double yCoordObstacle = 0.0;
	public static double obstacleHeading = 0.0;
	public int startingCorner = 0;
	private static final int USDISTANCE = 25;
	private static final double BACK_UP_DISTANCE = 30.0;
	private static final double PUSH_DISTANCE = 15.0;
	private static final double ARM_LENGTH = 22;
	final double TILE = 30.48;
	
	//Constructor
	public Navigation(Odometer odo, NXTRegulatedMotor leftWheel, NXTRegulatedMotor rightWheel) {
		this.odo = odo;
		this.bot = odo.getTwoWheeledRobot();
		LEFT_RADIUS = bot.getLeftRadius();
		RIGHT_RADIUS = bot.getRightRadius();
		WIDTH = bot.getWidth();
		this.leftWheel = leftWheel;
		this.rightWheel = rightWheel;
	}
	
	/**
	 * Sets the side sensors of the robot.
	 * @param leftUSSensor
	 * @param rightUSSensor
	 */
	public void setNavSideSensors(UltrasonicSensor leftUSSensor, UltrasonicSensor rightUSSensor)
	{
		this.leftUSSensor = leftUSSensor;
		this.rightUSSensor = rightUSSensor;
	}
	
	/**
	 * Sets the arm of the robot.
	 * @param arm
	 */
	public void setArm(ArmMovement arm)
	{
		this.arm = arm;
	}
	
	/**
	 * Sets the starting corner and heading of the robot, using the given X and Y coordinates.
	 * @param xStart
	 * @param yStart
	 */
	public void setStart(double xStart, double yStart){
		this.xStart = xStart;
		this.yStart = yStart;
		if(xStart==0.0)	{
			if(yStart==0.0)	{
				this.startHeading = 0.0;
				this.startingCorner = 1;
			} else if (yStart==10.0)	{
				this.startHeading = 90.0;
				this.startingCorner = 2;
			}
		} else if (xStart==10.0){
			if(yStart==0.0)	{
				this.startHeading = 270.0;
				this.startingCorner = 4;
			} else if (yStart==10.0)	{
				this.startHeading = 180.0;
				this.startingCorner = 3;
			}
		}
		this.xStart = xStart*TILE;
		this.yStart = yStart*TILE;
	}
	
	/**
	 * Getter for the X starting coordinate.
	 * @return the X start coordinate
	 */
	public double getXStart(){
		return this.xStart;
	}
	
	/**
	 * Getter for the Y starting coordinate.
	 * @return the Y start coordinate
	 */
	public double getYStart(){
		return this.yStart;
	}
	
	/**
	 * Sets the player destination.
	 * @param xFlag
	 * @param yFlag
	 */
	public void setPlayerDestination(double xFlag, double yFlag){
		this.xPD = xFlag;
		this.yPD = yFlag;
	}
	
	/**
	 * Getter for the X coordinate of the destination.
	 * @return the X coordinate of the destination
	 */
	public double getXPD(){
		return this.xPD;
	}
	
	/**
	 * Getter for the Y coordinate of the destination.
	 * @return the Y coordinate of the destination
	 */
	public double getYPD(){
		return this.yPD;
	}
	
	/**
	 * Getter for the starting corner.
	 * @return the starting corner
	 */
	public int getCorner(){
		return this.startingCorner;
	}
	
	/**
	 * Setter for the standard destination.
	 * @param xDest
	 * @param yDest
	 */
	public void setDestination(double xDest, double yDest)
	{
		this.xDest = xDest;
		this.yDest = yDest;
	}
	
	/**
	 * Getter for the standard X coordinate of destination.
	 * @return X coordinate of destination
	 */
	public double getXDest(){
		return this.xDest;
	}
	
	/**
	 * Getter for the standard Y coordinate of destination.
	 * @return Y coordinate of destination
	 */
	public double getYDest(){
		return this.yDest;
	}
	
	/**
	 * Travel to the standard destination (used by the main method)
	 */
	public void travelToDestination()
	{
		int Offset = 25;
		  if(xStart == 0.0){
			  if(yStart == 0.0){
				  travelTo(xDest*TILE-Offset,yDest*TILE-Offset);
			  }else travelTo(xDest*TILE-Offset,yDest*TILE+Offset);
		  }else{
			  if(yStart == 0.0){
				  travelTo(xDest*TILE+Offset,yDest*TILE-Offset);
			  }else travelTo(xDest*TILE+Offset,yDest*TILE+Offset);
		  }
	}

	/**
	 * Travels to given X and Y coordinate while avoiding obstacles and remembering corner location.
	 * @param x
	 * @param y
	 */
	public void travelTo(double x, double y) {
		double[] myPos = new double[3];
		boolean doNothing = false;

		final double xD = x;
		final double yD = y;
		//turn to the correct direction
		double angle = findAngle(x,y);
		turnTo(angle);
		Delay.msDelay(100);
		
		while(true){
			
			//Check if there is an obstacle in front
			if (getFilteredData(forwardUSSensor)<USDISTANCE){
				odo.getPosition(myPos);
				
				if(isStillAVirgin){
				xCoordObstacle=myPos[0];
				yCoordObstacle=myPos[1];
				obstacleHeading=myPos[2];
				isStillAVirgin = false;
				}
				//Remember corner
				if(getFilteredData(rightUSSensor)>getFilteredData(leftUSSensor)){
					if(getFilteredData(leftUSSensor)<USDISTANCE)
					{
						xCoordCorner=myPos[0];
						yCoordCorner=myPos[1];
						cornerHeading=myPos[2];
					}
					//Avoid obstacle
					turnTo(90);
				}else{
					if(getFilteredData(rightUSSensor)<USDISTANCE)
					{
						xCoordCorner=myPos[0];
						yCoordCorner=myPos[1];
						cornerHeading=myPos[2];
					}
					turnTo(-90);
				}
				
				Delay.msDelay(100);
				
				//If there is another obstacle while trying to avoid.
				travelTo(TILE*1.5, true);
				while(true){
					if(getFilteredData(forwardUSSensor)<15){
						bot.stop();
						break;
					}
					
					if(leftWheel.getRotationSpeed()==0) break;
				}
				
				//turns to the right angle and try to get to the coordinates again (recursion)
				done = false;
				travelTo(x,y);	
				doNothing = true;
				break;
				}
			
			
			
			if(!doNothing){
			//calculate deltax, deltay and distance
			//reading from odometer is in millimeters, but position stores it in centimeters,
			//so no factor of 10 needs to be applied to correct it

			(new Thread() {
				public void run()
				{
					leftWheel.setSpeed(FORWARD_SPEED);
					rightWheel.setSpeed(FORWARD_SPEED);
					
					leftWheel.rotate(convertDistance(LEFT_RADIUS,findDistance(xD,yD)),true);
					rightWheel.rotate(convertDistance(RIGHT_RADIUS,findDistance(xD,yD)),false);
					done=true;
				}
			}).start();
			doNothing = true;
			
			}

			if(done)
			{
				done=false;
				break;
			}
			
		}
	}
	
	/**
	 * Does the finishing move of the defender, which consists of going to either a corner, a wall or the middle of the 
	 * field, drop the beacon and go back to the starting corner.
	 */
	public void defenderFinish()
	{
		//if a corner is found..
		if(xCoordCorner!=0.0 || yCoordCorner!=0.0)
		{
				travelTo(xCoordCorner,yCoordCorner);
				turnTo(findAngle(cornerHeading));
			
			leftWheel.setSpeed(FORWARD_SPEED);
			rightWheel.setSpeed(FORWARD_SPEED);
			
			leftWheel.rotate(convertDistance(LEFT_RADIUS,-BACK_UP_DISTANCE),true);
			rightWheel.rotate(convertDistance(RIGHT_RADIUS,-BACK_UP_DISTANCE),false);
			
			arm.armDown(95,1);
			
			arm.openClaw();
			
			arm.armUp(95,0);
			
			leftWheel.setSpeed(FORWARD_SPEED);
			rightWheel.setSpeed(FORWARD_SPEED);
			
			leftWheel.rotate(convertDistance(LEFT_RADIUS,PUSH_DISTANCE),true);
			rightWheel.rotate(convertDistance(RIGHT_RADIUS,PUSH_DISTANCE),false);
			
			leftWheel.setSpeed(FORWARD_SPEED);
			rightWheel.setSpeed(FORWARD_SPEED);
			
			leftWheel.rotate(convertDistance(LEFT_RADIUS,-PUSH_DISTANCE),true);
			rightWheel.rotate(convertDistance(RIGHT_RADIUS,-PUSH_DISTANCE),false);
			
			travelTo(xStart,yStart);
			
			turnTo(findAngle(startHeading+45));
			
			leftWheel.setSpeed(FORWARD_SPEED);
			rightWheel.setSpeed(FORWARD_SPEED);
			
			leftWheel.rotate(convertDistance(LEFT_RADIUS,-10*PUSH_DISTANCE),true);
			rightWheel.rotate(convertDistance(RIGHT_RADIUS,-10*PUSH_DISTANCE),false);
	}else{
			if(xCoordObstacle!=0.0 || yCoordObstacle!=0.0) {
			//if no corner is found but an obstacle was found
			travelTo(xCoordObstacle,yCoordObstacle);
			turnTo(findAngle(cornerHeading));
			leftWheel.setSpeed(FORWARD_SPEED);
			rightWheel.setSpeed(FORWARD_SPEED);
			
			leftWheel.rotate(convertDistance(LEFT_RADIUS,-BACK_UP_DISTANCE),true);
			rightWheel.rotate(convertDistance(RIGHT_RADIUS,-BACK_UP_DISTANCE),false);
			
			arm.armDown(105,1);
			
			arm.openClaw();
			
			arm.armUp(105,0);
			
			leftWheel.setSpeed(FORWARD_SPEED);
			rightWheel.setSpeed(FORWARD_SPEED);
			
			leftWheel.rotate(convertDistance(LEFT_RADIUS,PUSH_DISTANCE),true);
			rightWheel.rotate(convertDistance(RIGHT_RADIUS,PUSH_DISTANCE),false);
			
			leftWheel.setSpeed(FORWARD_SPEED);
			rightWheel.setSpeed(FORWARD_SPEED);
			
			leftWheel.rotate(convertDistance(LEFT_RADIUS,-PUSH_DISTANCE),true);
			rightWheel.rotate(convertDistance(RIGHT_RADIUS,-PUSH_DISTANCE),false);
			
			travelTo(xStart,yStart);
			
			turnTo(findAngle(startHeading+45));
			
			leftWheel.setSpeed(FORWARD_SPEED);
			rightWheel.setSpeed(FORWARD_SPEED);
			
			leftWheel.rotate(convertDistance(LEFT_RADIUS,-10*PUSH_DISTANCE),true);
			rightWheel.rotate(convertDistance(RIGHT_RADIUS,-10*PUSH_DISTANCE),false);
		} else {
			//if no corner or obstacle is found, go to the middle of the field and drop it.
			travelTo(5*TILE,5*TILE);
			
			arm.armDown(95, 1);
			arm.openClaw();
			arm.armUp(95,0);
			
			leftWheel.setSpeed(FORWARD_SPEED);
			rightWheel.setSpeed(FORWARD_SPEED);
			
			leftWheel.rotate(convertDistance(LEFT_RADIUS,-PUSH_DISTANCE),true);
			rightWheel.rotate(convertDistance(RIGHT_RADIUS,-PUSH_DISTANCE),false);
			
			travelTo(xStart,yStart);
			turnTo(findAngle(startHeading+45));
			
			leftWheel.setSpeed(FORWARD_SPEED);
			rightWheel.setSpeed(FORWARD_SPEED);
			
			leftWheel.rotate(convertDistance(LEFT_RADIUS,-10*PUSH_DISTANCE),true);
			rightWheel.rotate(convertDistance(RIGHT_RADIUS,-10*PUSH_DISTANCE),false);
		}
			}
	}
	
	/**
	 * Does the finishing move of the attacker, which consists of going to the given X and Y coordinates,
	 * dropping the beacon and going back to the starting corner.
	 * @param x
	 * @param y
	 */
	public void attackerFinish(double x, double y)
	{
		travelTo(x,y);
		
		leftWheel.setSpeed(FORWARD_SPEED);
		rightWheel.setSpeed(FORWARD_SPEED);
		
		leftWheel.rotate(convertDistance(LEFT_RADIUS,-ARM_LENGTH),true);
		rightWheel.rotate(convertDistance(RIGHT_RADIUS,-ARM_LENGTH),false);
		
		arm.armDown(105, 1);
		arm.openClaw();
		arm.armUp(105,0);
		
		leftWheel.setSpeed(FORWARD_SPEED);
		rightWheel.setSpeed(FORWARD_SPEED);
		
		leftWheel.rotate(convertDistance(LEFT_RADIUS,-PUSH_DISTANCE),true);
		rightWheel.rotate(convertDistance(RIGHT_RADIUS,-PUSH_DISTANCE),false);
		
		travelTo(xStart,yStart);
		turnTo(findAngle(startHeading+45));
		
		leftWheel.setSpeed(FORWARD_SPEED);
		rightWheel.setSpeed(FORWARD_SPEED);
		
		leftWheel.rotate(convertDistance(LEFT_RADIUS,-PUSH_DISTANCE),true);
		rightWheel.rotate(convertDistance(RIGHT_RADIUS,-PUSH_DISTANCE),false);
	}
	
	/**
	 * Travel to a certain distance in a straight line and wait for it to finish before doing anything else.
	 * @param distance
	 */
	public void travelTo(double distance) {
		travelTo(distance,false);
	}

	/**
	 * Travel to a certain distance in a straight line and either wait for it to finish or keep on doing other
	 * instructions while the robot is moving.
	 * @param distance
	 * @param waitForFinish
	 */
	public void travelTo(double distance, boolean waitForFinish) {
		leftWheel.setSpeed(FORWARD_SPEED);
		rightWheel.setSpeed(FORWARD_SPEED);

		leftWheel.rotate(convertDistance(LEFT_RADIUS,distance),true);
		rightWheel.rotate(convertDistance(RIGHT_RADIUS,distance),waitForFinish);
	}
	
	/**
	 * Turn to a certain angle and wait for it to finish before doing anything else.
	 * @param angle
	 */
	public void turnTo(double angle) {
		turnTo(angle,false);
	}
	
	/**
	 * Turn to a certain angle and either wait for it to finish or keep on doing other
	 * instructions while the robot is turning.
	 * @param angle
	 * @param goBack
	 */
	public void turnTo(double angle, boolean waitForFinish) {
		leftWheel.setSpeed(ROTATION_SPEED);
		rightWheel.setSpeed(ROTATION_SPEED);
		
		leftWheel.rotate(convertAngle(LEFT_RADIUS,WIDTH,angle),true);
		rightWheel.rotate(-convertAngle(RIGHT_RADIUS,WIDTH,angle),waitForFinish);
	}
	
	/**
	 * Returns the distance from the robot to certain X and Y coordinates.
	 * @param x
	 * @param y
	 * @return the distance from the robot to certain X and Y coordinates
	 */
	public double findDistance(double x, double y)
	{
		odo.getPosition(position);	//get current position values
		double deltaX = (x-position[0]);
		double deltaY = (y-position[1]);
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	}
	
	
	/**
	 * Returns angle based on desired position as well as current position.
	 * @param x
	 * @param y
	 * @return the angle based on desired position as well as current position
	 */
	public double findAngle(double x, double y){
		odo.getPosition(position);	//get current position values
		double deltaX = (x-position[0]);
		double deltaY = (y-position[1]);
			
		double angle = 0.0;	//to store value of angle that the robot must be facing to travel straight to position x,y
		if(y-position[1]==0)	{	//when delta y = 0, an error occurs in calculating the atan
			angle = deltaX / Math.abs(deltaX) * 90.0; //delta x / |delta x| gives 1 when delta x is positive and -1 when delta x is negative 
		}	else	if(x-position[0]==0 && y-position[1]<0)	{ //will return 0 regardless of delta y, only needs correcting when delta y is negative
			angle = 180.0;
		}	else	{
			angle = 180.0 / Math.PI * Math.atan(deltaX/deltaY); //arctangent of (delta x/delta y)
		}
			
		//We imagine clockwise rotation to be positive, and anti-clockwise to be negative
		//When delta x is positive and delta y is negative, we have a larger positive rotation
		//but the arctan indicates a negative rotation, so wee add 180 to angle to correct it
		if(x-position[0]>0 && y-position[1]<0)	{
			angle = angle + 180;
		}
		//if delta x and delta y are both negative, delta x/ delta y will be positive and a positive angle
		//will be given by the atan. 180 is subtracted to fix this
		if(x-position[0]<0 && y-position[1]<0)	{
			angle = angle - 180;
		}
		//the other combinations of positive and negative delta x and delta y do not need corrections
		return findAngle(angle);	//now that we have an angle, we can send it to findAngle(double theta) to calculate the rest
	}
	
	/**
	 * Returns the shortest angle based on current angle and desired angle.
	 * @param theta
	 * @return the shortest angle based on current angle and desired angle
	 */
	public double findAngle(double theta){
		//Find the proper direction
		odo.getPosition(position);	//get current position values

		double angle;	//create angle to store value of angle through which robot must turn
		
		angle = theta - position[2];			//taking into account current angle in total turning angle
		
		//Following while loops make sure angle is minimal (i.e. making sure robot does
		//not turn -270 degrees when it can turn +90 degrees or +225 degrees when it can turn -135 degrees, for example)
		while (angle>180)	{
			angle = angle - 360;
		}
		while (angle<-180)	{
			angle = angle + 360;
		}
		
		return angle;
	}
	
	/**
	 * Using a 60 centimeters filter (maximum), returns the distance read by the front ultrasonic sensor.
	 * @param sensor
	 * @return the distance read by the front ultra sonic sensor
	 */
	public int getFilteredData(UltrasonicSensor sensor) {
		int distance;
		
		// do a ping
		sensor.ping();
		
		// wait for the ping to complete
		try { Thread.sleep(50); } catch (InterruptedException e) {}
		
		// there will be a delay here
		distance = sensor.getDistance();
		if (distance > 60)
			distance = 60;
				
		return distance;
	}
	
	/**
	 * Returns the number of road revolution needed to go a certain distance, given a certain radius.
	 * @param radius
	 * @param distance
	 * @return the number of road revolution needed to go a certain distance
	 */
	private static int convertDistance(double radius, double distance) {
		//taken from the SquareDriver in Lab2
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	/**
	 * Converts the angle in degrees from radians.
	 * @param radius
	 * @param width
	 * @param angle
	 * @return the angle in degrees
	 */
	private static int convertAngle(double radius, double width, double angle) {
		// also taken from the SquareDriver in Lab2
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}
