package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import users.CurrentUser;

import levels.AbstractLevel;
import levels.Level1;
import levels.Level3;
import levels.Level2;
import levels.Level0;
import net.miginfocom.swing.MigLayout;

/**
 * This class defines the game selection menu.
 * When a game level is unlocked, it can be clicked.
 * @author Antoine Tu
 *
 */
public class SelectLevelMenu extends JPanel {
	AbstractLevel[] levels = {new Level1(), new Level2(), new Level3(), new Level0()};
	public SelectLevelMenu() {
		boolean[] levelIsUnlocked = CurrentUser.getInstance().getLevelsUnlocked();
		
		setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		setLayout(new MigLayout("", "[25px][grow][25px]", "[][][][]"));
		
		//Add title on top of screen
		JLabel selectLevelTitle = new JLabel("Select Level");
		selectLevelTitle.setFont(new Font("Dialog", Font.PLAIN, 30));
		selectLevelTitle.setForeground(Color.WHITE);
		add(selectLevelTitle, "cell 1 0,alignx center");
		
		for(int i = 0; i < levels.length; i++) {
			JLabel levelLabel = new JLabel("Level " + (i+1) + " " + (levelIsUnlocked[i] ? "(Unlocked)" : "(Locked)"));
			if (levelIsUnlocked[i]) {
				levelLabel.setForeground(Color.BLUE);
				
				final int levelNumber = i; //Proper scoping requires a constant
				
				levelLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						WindowManager.startGame(levels[levelNumber]);
					}
				});
			} else {
				levelLabel.setForeground(Color.GRAY);
			}
			
			levelLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
			add(levelLabel, "flowx,cell 1 " + (i + 1) + ",alignx center");
		}
	}

}
