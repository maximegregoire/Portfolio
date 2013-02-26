package display;

import game.Level;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import levels.AbstractLevel;
import levels.Level1;
import levels.Level3;
import levels.Level2;
import net.miginfocom.swing.MigLayout;
import users.CurrentUser;
import users.GuestProfile;
import users.UserProfile;
import users.UsernameNotFoundException;
import users.Users;

/**
 * This class defines the main menu of the Pacman game.
 * @author Antoine Tu
 *
 */

public class MainMenu extends JPanel {
	UserProfile user;
	AbstractLevel[] levels = {new Level1(), new Level2(), new Level3()};
	public MainMenu() {
		setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		
		Panel titlePanel = new Panel();
		titlePanel.setName("titlePanel");
		titlePanel.setLayout(new MigLayout("", "[415px][grow][415px]", "[122px][250px][grow]"));
		
		JLabel lblPacman = new JLabel("PACMAN");
		titlePanel.add(lblPacman, "cell 1 0,alignx left,aligny top");
		lblPacman.setVerticalAlignment(SwingConstants.TOP);
		lblPacman.setHorizontalAlignment(SwingConstants.CENTER);
		lblPacman.setForeground(Color.YELLOW);
		lblPacman.setFont(new Font("Dialog", Font.BOLD, 96));
		add(titlePanel);
		
		final Panel messagePanel = new Panel();
		titlePanel.add(messagePanel, "cell 1 1,alignx center,aligny top");
		messagePanel.setName("messagePanel");
		JLabel userWelcomeLabel = new JLabel();
		// Get the current user
		user = CurrentUser.getInstance();
		userWelcomeLabel.setForeground(Color.BLUE);
		userWelcomeLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
		userWelcomeLabel.setText("Welcome " + user.getUsername());
		messagePanel.add(userWelcomeLabel);
		
		Panel mainMenu = new Panel();
		titlePanel.add(mainMenu, "cell 1 2,alignx center,aligny bottom");
		mainMenu.setName("mainMenu");
		mainMenu.setLayout(new GridLayout(0, 1, 0, 0));
		
		Label playGameLabel = new Label("Play Game");
		playGameLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (user instanceof GuestProfile) {
					WindowManager.startGame(new Level1());
				} else {
					WindowManager.showSelectLevelMenu();
				}
			}
		});
		playGameLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		playGameLabel.setName("playGameLabel");
		playGameLabel.setAlignment(Label.CENTER);
		mainMenu.add(playGameLabel);
		
		// Adds additional menu items if the user is logged in.
		if (!(user instanceof GuestProfile)) {
			Label profileLabel = new Label("Profile");
			profileLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						WindowManager.openProfileDisplay();
					} catch (IOException e1) {
					} catch (UsernameNotFoundException e1) {
					}
				}
			});
			profileLabel.setAlignment(Label.CENTER);
			profileLabel.setName("profileLabel");
			profileLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
			mainMenu.add(profileLabel);
			
			Label statisticsLabel = new Label("Statistics");
			statisticsLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					WindowManager.openStatisticDisplay(false);
				}
			});
			statisticsLabel.setAlignment(Label.CENTER);
			statisticsLabel.setName("statisticsLabel");
			statisticsLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
			mainMenu.add(statisticsLabel);
			
			Label logoutLabel = new Label("Logout");
			logoutLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					CurrentUser.setInstance(new GuestProfile());
					WindowManager.openLoginWindow();
				}
			});
			logoutLabel.setAlignment(Label.CENTER);
			logoutLabel.setName("logoutLabel");
			logoutLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
			mainMenu.add(logoutLabel);
			
		} else {
			Label loginLabel = new Label("Login");
			loginLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					WindowManager.openLoginWindow();
				}
			});
			loginLabel.setAlignment(Label.CENTER);
			loginLabel.setName("loginLabel");
			loginLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
			mainMenu.add(loginLabel);
		}
		
		Label exitLabel = new Label("Exit");
		exitLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Users.getInstance().save();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		exitLabel.setName("exitLabel");
		exitLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		exitLabel.setAlignment(Label.CENTER);
		mainMenu.add(exitLabel);
	}
	
}