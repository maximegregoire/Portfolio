package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import levels.Level1;
import net.miginfocom.swing.MigLayout;
import users.CurrentUser;
import users.GameStatistics;
import users.LevelStatistics;
import users.UserProfile;

public class StatisticDisplay extends JPanel {
	
	public StatisticDisplay() {
		addHeader();
		addTotalStatistics();
	}
	
	public StatisticDisplay(GameStatistics currentGameStatistics) {
		addHeader();
		try {
			addTotalStatistics();
		} catch (NullPointerException e) {
			//Means current user is guest, so we don't have to take care of running statistics
		}
		
		showCurrentGameStatistics(currentGameStatistics);
	}
	
	private void addHeader() {
		setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		setLayout(new MigLayout("", "[][][][][]", "[][][][][][][]"));
		
		JButton backButton = new JButton("Main");
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				WindowManager.openMainMenu();
			}
		});
		add(backButton, "cell 0 0,alignx left,aligny top");
		
		JLabel titleLabel = new JLabel("Statistics");
		titleLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		titleLabel.setForeground(Color.WHITE);
		add(titleLabel, "cell 1 0 3 1,alignx center");
		
		JLabel levelTitle = new JLabel("Level");
		levelTitle.setFont(new Font("Dialog", Font.PLAIN, 21));
		levelTitle.setForeground(Color.WHITE);
		add(levelTitle, "cell 0 1,alignx center");
		
		JLabel maxScoreTitle = new JLabel(" | Max Score | ");
		maxScoreTitle.setFont(new Font("Dialog", Font.PLAIN, 21));
		maxScoreTitle.setForeground(Color.WHITE);
		add(maxScoreTitle, "cell 1 1,alignx center");
		
		JLabel deathsTitle = new JLabel("Total Deaths | ");
		deathsTitle.setFont(new Font("Dialog", Font.PLAIN, 21));
		deathsTitle.setForeground(Color.WHITE);
		add(deathsTitle, "cell 2 1,alignx center");
		
		JLabel winsTitle = new JLabel("Total Wins | ");
		winsTitle.setFont(new Font("Dialog", Font.PLAIN, 21));
		winsTitle.setForeground(Color.WHITE);
		add(winsTitle, "cell 3 1,alignx center");
		
		JLabel lossesTitle = new JLabel("Total Losses");
		lossesTitle.setFont(new Font("Dialog", Font.PLAIN, 21));
		lossesTitle.setForeground(Color.WHITE);
		add(lossesTitle, "cell 4 1, alignx center");
	}
	private void showCurrentGameStatistics(GameStatistics currentGameStatistics) {
		JLabel currentGameTitle = new JLabel("Current Game");
		currentGameTitle.setFont(new Font("Dialog", Font.PLAIN, 20));
		currentGameTitle.setForeground(Color.BLUE);
		add(currentGameTitle, "cell 0 4, alignx center");
		
		List<LevelStatistics> levels = currentGameStatistics.getLevels();
		System.out.println(levels.size());
		
		for(int i = 0; i < levels.size(); i++) {
			JLabel levelNameLabel = new JLabel("Level " + levels.get(i).getLevelNumber());
			levelNameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
			levelNameLabel.setForeground(Color.WHITE);
			add(currentGameTitle, "cell 0 " + (i+5) + ", alignx center");
			
			JLabel scoreLabel = new JLabel(Integer.toString(levels.get(i).getScore()));
			scoreLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
			scoreLabel.setForeground(Color.WHITE);
			add(scoreLabel, "cell 1 " + (i+5) + ", alignx center");
			
			JLabel deathsLabel = new JLabel(Integer.toString(levels.get(i).getDeaths()));
			deathsLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
			deathsLabel.setForeground(Color.WHITE);
			add(scoreLabel, "cell 2 " + (i+5) + ", alignx center");
		}
	}
	
	/**
	 * Displays the total statistics
	 */
	private void addTotalStatistics() {
		UserProfile user = CurrentUser.getInstance();
		
		for(int i = 0; i < 4; i++) {
			JLabel levelNameLabel = new JLabel("Level " + i);
			levelNameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
			levelNameLabel.setForeground(Color.WHITE);
			add(levelNameLabel, "cell 0 " + (i + 5) + ", alignx center");
			
			JLabel scoreLabel = new JLabel(Integer.toString(user.getMaxScoreByLevel(i)));
			scoreLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
			scoreLabel.setForeground(Color.WHITE);
			add(scoreLabel, "cell 1 " + (i + 5) + ", alignx center");
			
			JLabel deathsLabel = new JLabel(Integer.toString(user.getDeathsByLevel(i)));
			deathsLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
			deathsLabel.setForeground(Color.WHITE);
			add(deathsLabel, "cell 2 " + (i + 5) + ", alignx center");
			
			JLabel winsLabel = new JLabel(Integer.toString(user.getWinsByLevel(i)));
			winsLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
			winsLabel.setForeground(Color.WHITE);
			add(winsLabel, "cell 3 " + (i + 5) + ", alignx center");
			
			JLabel lossesLabel = new JLabel(Integer.toString(user.getLossesByLevel(i)));
			lossesLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
			lossesLabel.setForeground(Color.WHITE);
			add(lossesLabel, "cell 4 " + (i + 5) + ", alignx center");
		}
	}
}
