/**
 * Class concerning everything related to the arm and the claws.
 * @author Maxime Grégoire
 * @author Andrew Walker
 * @author Dan Crisan
 */

import lejos.nxt.*;
import lejos.nxt.remote.RemoteMotor;
import lejos.util.Delay;

public class ArmMovement {

	private RemoteMotor leftArm;
	private RemoteMotor rightArm;
	private RemoteMotor claw;
	private NXTRegulatedMotor puller;
	private Navigation nav;

	//Constructor
	public ArmMovement(RemoteMotor leftArm, RemoteMotor rightArm, RemoteMotor claw, NXTRegulatedMotor puller, Navigation nav)
	{
		this.leftArm = leftArm;
		this.rightArm = rightArm;
		this.claw = claw;
		this.puller = puller;
		this.nav = nav;
	}
	
	/**
	 * Back the robot by 20 centimeters, pull the robot's arm down, travel forward 40 centimeters to make
	 * sure the beacon is in its claws, shuts the claws and get the arm and the beacon up.
	 */
	public void grab()
	{
		claw.setSpeed(50);
		claw.stop();
		setPullerSpeed(200);
		setArmSpeed(50);
		nav.travelTo(-20);
		armDown(105,0);
		nav.travelTo(40);
		closeClaw();
		Delay.msDelay(450);
		armUp(110,1);
		nav.travelTo(-20);
	}
	
	/**
	 * Setter for the two arm motors' speed.
	 * @param speed
	 */
	public void setArmSpeed(int speed)
	{
		leftArm.setSpeed(speed);
		rightArm.setSpeed(speed);
	}
	
	/**
	 * Setter for the puller motor's speed.
	 * @param speed
	 */
	public void setPullerSpeed(int speed)
	{
		puller.setSpeed(speed);
	}
	
	/**
	 * Setter for the claw's speed.
	 * @param speed
	 */
	public void setClawSpeed(int speed)
	{
		claw.setSpeed(speed);
	}
	
	/**
	 * Makes the claw close (grab).
	 */
	public void closeClaw()
	{
		setClawSpeed(900);
		claw.backward();
	}
	
	/**
	 * Makes the claw open (release).
	 */
	public void openClaw()
	{
		claw.flt();
		setClawSpeed(50);
		claw.forward();
		Delay.msDelay(2000);
		claw.stop();
		setClawSpeed(50);
	}
	
	/**
	 * Makes the arm go up by a certain angle. This method takes into account if the arm holds a weight (1) or not (0).
	 * @param angle
	 * @param weight
	 */
	public void armUp(int angle, int weight)
	{
		setArmSpeed(50+450*weight);
		setPullerSpeed(200+700*weight);
		leftArm.rotate(-angle,true);
		rightArm.rotate(-angle,true);
		puller.rotate((int)(-angle*10),false);
		setArmSpeed(50);
		setPullerSpeed(200);
	}
	
	/**
	 * Makes the arm go down by a certain angle. This method takes into account if the arm holds a weight (1) or not (0).
	 * @param angle
	 * @param weight
	 */
	public void armDown(int angle, int weight)
	{
		setArmSpeed(50-25*weight);
		leftArm.rotate(angle,true);
		rightArm.rotate(angle,true);
		puller.rotate((int)(angle*10),false);
		setArmSpeed(50);
	}
}

