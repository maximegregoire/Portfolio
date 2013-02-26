package characters;

import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * This class takes care of the movement timer for all the characters.
 * @author Maxime Grégoire
 * @author Carl Patenaude Poulin
 * @author Antoine Tu
 */
public class MovementTimer {
	private static MovementTimer instance;
	private Timer timer;
	
	/**
	 * Initializes the movement timer with its delay.
	 * @param delay
	 */
	public MovementTimer(int delay) {
		this.timer = new Timer(delay, null);
		instance = this;
	}
	
	/**
	 * get the instance of the MovementTimer
	 * @return instance of MovementTimer
	 */
	public static MovementTimer getInstance() {
		return instance;
	}
	
	/**
	 * Subscribes a certain action listener to the timer.
	 * @param a, the action listener
	 */
	public void subscribe(ActionListener a) {
		this.timer.addActionListener(a);
	}
	
	/**
	 * Starts the movement timer.
	 */
	public void start() {
		timer.start();
	}
	
	/**
	 * Stops the movement timer.
	 */
	public void stop() {
		timer.stop();
	}
	
	/**
	 * Getter for the delay of the movement timer.
	 * @return the delay of the movement timer.
	 */
	public int getDelay() {
		return timer.getDelay();
	}
}
