/**
 * Class to localize the position and the exact heading of the robot by reading the intersection of
 * the grid lines.
 * @author Maxime Grégoire
 * @author Andrew Walker
 * @author Dan Crisan
 */

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class LightSearch {
	private Odometer odo;
	private TwoWheeledRobot robot;
	private LightSensor ls;
	private UltrasonicSensor us;
	private Navigation nav;

	private NXTRegulatedMotor leftWheel;
	private NXTRegulatedMotor rightWheel;
	private ArmMovement arm;

	public static double FORWARD_SPEED = 100;
	public static double ROTATION_SPEED = 5;
	private final double TILE = 30.48;
	public boolean closeEnough = false;
	public boolean getCloser = false;
	private final int DISTANCE_FROM_BLOCK = 30;

	private double highReading;

	// Constructor
	public LightSearch(Odometer odo, LightSensor ls, UltrasonicSensor us,

	NXTRegulatedMotor leftWheel, NXTRegulatedMotor rightWheel, ArmMovement arm) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.ls = ls;
		this.us = us;
		this.nav = odo.getNavigation();
		this.leftWheel = leftWheel;
		this.rightWheel = rightWheel;
		this.arm = arm;
	}

	// find the source orientation, and go 40 cm in that direction,
	// then check again for the orientation, until it is close enough

	/**
	 * Find the beacon by getting closer to it, 40 centimeters at a time and
	 * then grabs it.
	 */
	public void getToSource() {
		ls.setFloodlight(false);
		while (!closeEnough) {
			getCloser(40);
		}

		Sound.beep();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Travels 3 positions on the field (the starting corner, the middle of the field and the opposite corner) 
	 * and look for the brightest orientation, then makes the robot go to it and search the beacon.
	 * @param xD
	 * @param yD
	 */
	public void getCloser_AttackMode()
		  {
			  //determine starting corner
			  int corner = nav.getCorner();
			  double brightValue = 0;
			  int brightestPlace = 0;
			  final int startingCorner = 0;
			  final int middle = 1;
			  final int oppositeCorner = 2;
			  //Travel to the starting corner and get the brightest orientation
			  nav.travelTo(getFixedX(corner), getFixedY(corner));
			  getBright();
			  brightValue = highReading;
			  
			  //Travel to the middle of the field and get the brightest orientation
			  nav.travelTo(TILE*5,TILE*5);
			  getBright();
			  if(highReading>brightValue){
				  brightestPlace = 1;
			  }else{
				  highReading = brightValue;
			  }
			  
			  //Travel to the opposite corner and get the brightest orientation
			  nav.travelTo(getFixedX(corner+2),getFixedY(corner+2));
			  if(highReading>brightValue){
				  brightestPlace = 2;
			  }else{
				  highReading = brightValue;
			  }
			  
			  //Checks which place has the brightest orientation, go to it and look for the beacon.
			  switch(brightestPlace){
			  case startingCorner: nav.travelTo(getFixedX(corner), getFixedY(corner));
			  		  getToSource(); break;
			  case middle: nav.travelTo(TILE*5,TILE*5);
			  		  getToSource(); break;
			  case oppositeCorner: getToSource(); break;
			  }
		  }

	/**
	 * Gets the robot closer to the beacon by a certain distance.
	 * @param distance
	 */
	public void getCloser(int distance) {

		// Get the brightest orientation and turn in that direction
		double orientation = getBright();
		robot.setRotationSpeed(ROTATION_SPEED);
		nav.turnTo(nav.findAngle(orientation));
		robot.setForwardSpeed(FORWARD_SPEED);
		nav.travelTo(distance, true);
		boolean isVirgin = true;
		
		//Checks if the US sensor detects something in front of the object.
		while (true) {
			if (isVirgin) {
				Delay.msDelay(1000);
				isVirgin = false;
			}
			if (leftWheel.getRotationSpeed() == 0 &&

			rightWheel.getRotationSpeed() == 0) {
				Sound.twoBeeps();
				break;
			}
			if (us.getDistance() < distance) {
				closeEnough = true;
				robot.stop();
				nav.travelTo(-15);
				arm.grab();
				break;
			}
		}
		//If the beacon is close enough, grab the beacon.
		if (us.getDistance() < distance && !closeEnough) {
			closeEnough = true;
			robot.stop();
			nav.travelTo(-15);
			arm.grab();
		}
	}

	/**
	 * Turns the robot 360 degrees and returns the brightest orientation.
	 * @return the brightest orientation
	 */
	public double getBright() {
		int filterFirstValues = 0;
		double[] initPos = new double[3];
		double[] pos = new double[3];
		odo.getPosition(pos);
		odo.getPosition(initPos);
		double brightestValue = ls.readValue();
		double brightestAngle = pos[2];
		double actualValue;
		robot.setRotationSpeed(30);
		while (true) {
			odo.getPosition(pos);
			if (Math.abs(pos[2] - initPos[2]) < 1 && filterFirstValues > 1000) {
				Sound.buzz();
				break;
			} else {
				filterFirstValues++;
				// if(filterFirstValues%5 == 2){
				actualValue = ls.readValue();
				if (actualValue > brightestValue) {
					brightestValue = actualValue;
					brightestAngle = pos[2];
				}
				// }
			}

		}
		robot.stop();

		return brightestAngle;
	}


	/**
	 * Returns the X coordinates of the actual corner, plus a 1-tile offset, towards the center of the field.
	 * @param actualCorner
	 * @return The X coordinates of the actual corner, plus a 1-tile offset, towards the center of the field.
	 */
	public double getFixedX(int actualCorner) {
		double[] XArray = { 9 * TILE, TILE, TILE, 9 * TILE };
		return XArray[(actualCorner) % 4];
	}

	/**
	 * Returns the Y coordinates of the actual corner, plus a 1-tile offset, towards the center of the field.
	 * @param actualCorner
	 * @return The Y coordinates of the actual corner, plus a 1-tile offset, towards the center of the field.
	 */
	public double getFixedY(int actualCorner) {
		double[] YArray = { 0 + TILE, 0 + TILE, 9 * TILE, 9 * TILE };
		return YArray[(actualCorner) % 4];
	}
}
