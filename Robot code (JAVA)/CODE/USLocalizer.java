/**
 * Class concerning the ultrasonic sensor localization
 * @author Maxime Grégoire
 * @author Andrew Walker
 * @author Dan Crisan
 */

import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	public static double ROTATION_SPEED = 30;
	
	private Odometer odo;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private Navigation nav;
	
	//Constructor
	public USLocalizer(Odometer odo, UltrasonicSensor us) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.us = us;
		this.nav = odo.getNavigation();
		us.off();
	}
	
	/**
	 * Find the right heading and set the odometer accordingly.
	 */
	public void doLocalization() {
		double [] pos = new double [3];
		double angleA, angleB;
		
		//Rotate until it sees the field.
		robot.setRotationSpeed(50);
		while(true){
			if(getFilteredData()==60){
				break;
			}
		}
		
		//Rotate, see a wall and latch the angle.
		while(true){
			if(getFilteredData()<30){
				Sound.beep();
				odo.getPosition(pos);
				angleA = pos[2];
				break;
			}
		}
		// switch direction and wait until it sees no wall
		robot.setRotationSpeed(-50);
		while(true){
			if(getFilteredData()==60){
				break;
			}
		}
		try { Thread.sleep(1500); } catch (InterruptedException e) {}

		// Keep rotating until the robot sees a wall, then latch the angle
		while(true){
			if(getFilteredData()<30){
				Sound.beep();
				robot.stop();
				odo.getPosition(pos);
				angleB = pos[2];
				break;
				}
		}
		
		// Compute the actual angle
		double deltaAngle;
		if(angleA < angleB){
			deltaAngle = 45-((angleA+angleB-360)/2);
		} else {
			deltaAngle = 45-((angleA+angleB)/2);
		}
			
		// Update the odometer with the real coordinates.
		odo.getPosition(pos); 
		odo.setPosition(new double [] {0.0, 0.0, pos[2]+deltaAngle}, new boolean [] {false, false, true});
		nav.turnTo(nav.findAngle(40));
		
	}
	
	/*
	 * Get the distance scanned from the ultrasonic sensor. If the distance exceeds 60,
	 * it returns 60
	 */
	/**
	 * Using a 60 centimeters filter (maximum), returns the distance read by the front ultrasonic sensor.
	 * @return the distance read by the front ultrasonic sensor
	 */
	public int getFilteredData() {
		int distance;
		
		// do a ping
		us.ping();
		
		// wait for the ping to complete
		try { Thread.sleep(50); } catch (InterruptedException e) {}
		
		// there will be a delay here
		distance = us.getDistance();
		if (distance > 60)
			distance = 60;
				
		return distance;
	}

}
