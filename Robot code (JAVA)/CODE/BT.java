/**
 * Class to get the data required for the accomplishment of the task. The signal is obtained through a bluetooth signal.
 * Works in parallel with the bluetooth server and client JAR provided.
 * @author Maxime Grégoire
 * @author Andrew Walker
 * @author Dan Crisan
 */

import bluetooth.BluetoothConnection;
import bluetooth.PlayerRole;
import bluetooth.StartCorner;
import bluetooth.Transmission;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class BT {
	
	//Initialisation of the flag coordinates (absolute) and the tile's dimensions (in centimeters)
	static int destX = 0;
	static int destY = 0;
	static int flagX = 0;
	static int flagY = 0;
	public static boolean isAttacker;
	
	/**
	 * Sets up the bluetooth connection and transmission. Gets the data and upload it into the robot's navigation.
	 * @param nav
	 */
	public static void getDestination(Navigation nav) {
		//Sets up bluetooth connection and transmission
		BluetoothConnection conn = new BluetoothConnection();
		Transmission t = conn.getTransmission();
		
		if (t == null) {
			Sound.beep();
			Delay.msDelay(1000);

			LCD.drawString("Failed to read transmission", 0, 5);
		} else {
			
			//Robot will start from this corner:
			StartCorner corner = t.startingCorner;

			//The role of the player is: 
			PlayerRole role = t.role; 
			
			//Defender will go here to get the flag:
			flagX = t.fx;	//flag pos x
			flagY = t.fy;	//flag pos y

			//Attacker will drop the flag off here
			destX = t.dx;	//destination pos x
			destY = t.dy;	//destination pos y

			//Print out the transmission information to the LCD
			conn.printTransmission();

		//Sets destination coordinates to the robot's navigation, depending on the role
		if(role.equals(PlayerRole.ATTACKER)){
			nav.setDestination(destX, destY);
			nav.setPlayerDestination(flagX,flagY);
			isAttacker = true;
		}
		else if(role.equals(PlayerRole.DEFENDER)){
			nav.setDestination(flagX, flagY);
			isAttacker = false;
		}
		
			//Sets initial point coordinates to the robot's navigation
			if(corner.equals(StartCorner.BOTTOM_LEFT)){
				nav.setStart(0.0, 0.0);
			} else if(corner.equals(StartCorner.BOTTOM_RIGHT)){
				nav.setStart(10.0, 0.0);
			} else if(corner.equals(StartCorner.TOP_LEFT)){
				nav.setStart(0.0, 10.0);
			} else if(corner.equals(StartCorner.TOP_RIGHT)){
				nav.setStart(10.0, 10.0);
			}	
			
			conn.printTransmission();
		}	
	}
}
