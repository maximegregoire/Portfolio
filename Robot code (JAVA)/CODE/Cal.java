import lejos.nxt.*;
import lejos.util.*;
import lejos.nxt.LightSensor;

public class Cal {
	
	private static final long CORRECTION_PERIOD = 50;
	
	public static void main(String[] args)	{
		
		int buttonChoice;
		LightSensor ls = new LightSensor(SensorPort.S2);
		ls.setFloodlight(false);
		buttonChoice = Button.waitForAnyPress();
		if(buttonChoice==Button.ID_LEFT)	{
			LCD.drawString("High",0,1);
			Button.waitForAnyPress();
			ls.calibrateHigh();
			LCD.drawString("Low.",0,1);
			Button.waitForAnyPress();
			ls.calibrateLow();
		}
		
		double prevV = 0.0;
		double thisV = 0.0;

		long correctionStart, correctionEnd;
		
		while(true)	{
			correctionStart = System.currentTimeMillis();
			
			thisV = ls.readValue();
			
			prevV = thisV;
			
			buttonChoice = Button.readButtons();
			LCD.clear();
			LCD.drawString("ls: " + ls.readValue(),0,5);
			
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {
					Thread.sleep(CORRECTION_PERIOD
							- (correctionEnd - correctionStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometry correction will be
					// interrupted by another thread
				}
			}
			
			if(buttonChoice == Button.ID_ESCAPE)	{
				break;
			}
		}
	}
}