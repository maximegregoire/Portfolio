/**
 * Main class of team 8's Armageddon Robot.
 * 
 * The code was compiled with LeJos version 9.1
 * 
 * @author Maxime Grégoire
 * @author Andrew Walker
 * @author Dan Crisan
 */



import java.io.IOException;
import lejos.nxt.*;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteNXT;
import lejos.util.Delay;

public class Main {
	
	private static boolean attackerStart=false;

	public static void main(String[] args)
	{	
		//Initiate local motors and sensors
		NXTRegulatedMotor leftWheel = new NXTRegulatedMotor(MotorPort.A);
		NXTRegulatedMotor rightWheel = new NXTRegulatedMotor(MotorPort.B);
		NXTRegulatedMotor puller = new NXTRegulatedMotor(MotorPort.C);
		
		LightSensor searchLightSensor = new LightSensor(SensorPort.S2);
		LightSensor localizationLightSensor = new LightSensor(SensorPort.S1);
		UltrasonicSensor forwardUSSensor = new UltrasonicSensor(SensorPort.S3);
		
		//Initiate the robot's objects
		final TwoWheeledRobot robot = new TwoWheeledRobot(leftWheel, rightWheel);
		final Odometer odometer = new Odometer(robot, true, leftWheel, rightWheel);
		final Navigation nav = odometer.getNavigation();
		final LCDInfo lcd = new LCDInfo(odometer);
		lcd.start();
		
		//Get Bluetooth signal
		BT.getDestination(nav);

		lcd.start();
		
		//Create RemoteNXT motors and sensors and instantiate the robot's arm
		try
		{	
			LCD.drawString("Connecting...", 0, 0);
			NXTCommConnector connector = Bluetooth.getConnector();
			RemoteNXT nxt = new RemoteNXT("SEagle", connector);
			LCD.clear();
			RemoteMotor leftArm = nxt.A;
			RemoteMotor rightArm = nxt.B;
			RemoteMotor claw = nxt.C;
			final UltrasonicSensor rightUSSensor = new UltrasonicSensor(nxt.S1);
			final UltrasonicSensor leftUSSensor = new UltrasonicSensor(nxt.S4);
			ArmMovement arm = new ArmMovement(leftArm, rightArm, claw, puller, nav);
			nav.setArm(arm);
			nav.setNavSideSensors(leftUSSensor, rightUSSensor);
			
			if(BT.isAttacker)
			{
				//ATTACKER PART
				
				
				//Start the timer
				long startTime, endTime;
				startTime = System.currentTimeMillis();
				
				//Override method to bypass the 5 minutes delay, as required in the specifications
				new Thread(){
					public void run()
					{
						while (Button.waitForAnyPress() != Button.ID_ENTER){
							
						}
						attackerStart=true;
						Sound.twoBeeps();
					}
				}.start();
				
				//Wait period of 5 minutes
				while(!attackerStart)
				{
					endTime=System.currentTimeMillis();
					if(endTime-startTime>300000)
					{
						Sound.beep();
						break;
					}
				}
				
				
				//Do ultrasonic localization
				USLocalizer usl = new USLocalizer(odometer,forwardUSSensor);
				usl.doLocalization();

				
				//Do light localization
				LightLocalizer lsl = new LightLocalizer(odometer,localizationLightSensor);
				lsl.doLocalization();
				
				//Search for light source
				LightSearch search = new LightSearch(odometer,searchLightSensor, forwardUSSensor, leftWheel, rightWheel, arm);
				search.getCloser_AttackMode();
				
				//Finish up
				nav.attackerFinish(nav.getXDest()*nav.TILE,nav.getYDest()*nav.TILE);
				
			} else {
				//DEFENDER PART
				
				//Do ultrasonic localization
				USLocalizer usl = new USLocalizer(odometer,forwardUSSensor);
				usl.doLocalization();
				
				//Do light localization
				LightLocalizer lsl = new LightLocalizer(odometer,localizationLightSensor);
				lsl.doLocalization();
				
				//Travel to beacon coordinates
				nav.travelToDestination();
				
				//Search for light source
				LightSearch search = new LightSearch(odometer,searchLightSensor, forwardUSSensor, leftWheel, rightWheel, arm);
				search.getToSource();
				
				//Finish up
				nav.defenderFinish();
			}
		} catch (IOException e)	{
			LCD.clear();
			LCD.drawString("Conn failed", 0, 0);
		}
	}
}
