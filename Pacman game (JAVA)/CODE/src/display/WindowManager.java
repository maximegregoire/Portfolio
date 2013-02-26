package display;

import game.CurrentGame;
import game.Game;
import game.Level;

import java.io.IOException;

import javax.swing.SwingUtilities;

import levels.AbstractLevel;
import storage.SimpleStorage;
import users.CurrentUser;
import users.SimpleHash;
import users.UsernameNotFoundException;
import users.Users;
/**
 * This class handles the visibility of the
 * different graphical user interfaces of the Pacman game.
 * 
 * @author Antoine Tu
 *
 */
public class WindowManager {
    private static MainMenu mainMenu;
    private static LoginWindow loginWindow;
    private static CustomFrame frame;
    private static ProfileDisplay profileDisplay;
    private static StatisticDisplay statisticDisplay;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	frame = new CustomFrame();
                try {
					new Users(new SimpleStorage("res/users"), new SimpleHash());
				} catch (IOException e) {
					e.printStackTrace();
				}
                openLoginWindow();
            }
        });
    }

    /**
     * Shows the login window.
     */
    public static void openLoginWindow() {
    	frame.dispose();
    	frame = new CustomFrame();
        loginWindow = new LoginWindow();
    }
    
    /**
     * Gets rid of the login window and show the main menu.
     */
    public static void openMainMenu() {
    	try {
    		loginWindow.dispose();
    	} catch (NullPointerException e) {
    	}
    	frame.dispose();
		frame = new CustomFrame();
		mainMenu = new MainMenu();
        frame.add(mainMenu);
        frame.setSize(850, 750);
        frame.setVisible(true);
	}

    /**
     * Gets rid of the main menu and show the pacman game.
     */
	public static void startGame(AbstractLevel levelModel) {
		Game game = new Game(frame);
		Level firstLevel = new Level(levelModel, frame);
		game.play(firstLevel);
	}

	/**
	 * Shows the user profile.
	 * @throws IOException
	 * @throws UsernameNotFoundException
	 */
	public static void openProfileDisplay() throws IOException, UsernameNotFoundException {
		frame.dispose();
		frame = new CustomFrame();
		frame.add(new ProfileDisplay(CurrentUser.getInstance()));
		frame.setSize(500,400);
		frame.setVisible(true);
	}

	/**
	 * Shows the game selection menu.
	 */
	public static void showSelectLevelMenu() {
		frame.dispose();
		frame = new CustomFrame();
		SelectLevelMenu selectLevelMenu = new SelectLevelMenu();
		frame.add(selectLevelMenu);
		frame.setSize(500,300);
		frame.setVisible(true);
	}

	/**
	 * Shows the game statistics.
	 */
	public static void openStatisticDisplay(boolean singleGame) {
		frame.dispose();
		frame = new CustomFrame();
		if (singleGame) {
			statisticDisplay = new StatisticDisplay(CurrentGame.getInstance().exportStatistics());
		} else {
			statisticDisplay = new StatisticDisplay();
		}
		frame.add(statisticDisplay);
		frame.setSize(625, 400);
		frame.setVisible(true);
	}
}