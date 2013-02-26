package display;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;

import users.Users;

/**
 * This class defines a frame that can be exited, effectively closing the application.
 * @author Carl Patenaude Poulin
 */

public class CustomFrame extends JFrame {
	
	public CustomFrame() {
		super();
		this.setExitable();
	}
	
	private void setExitable() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent event)
			{	
				try {
					Users.getInstance().save();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0); //calling the method is a must
			}
		});
	}
}
