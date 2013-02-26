package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import users.CurrentUser;
import users.GuestProfile;
import users.UserProfile;
import users.UsernameNotFoundException;
import users.Users;
import users.WrongPasswordException;

/**
 * This class defines the user profile display. The user
 * information is displayed, and the user can modify some
 * of the informations as well as delete their account.
 * @author Antoine Tu
 *
 */
public class ProfileDisplay extends JPanel {
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JPasswordField oldPasswordField;
	private JPasswordField newPasswordField1;
	private JPasswordField newPasswordField2;

	/**
	 * Initializes a new profile display graphical interfac for the current user.
	 * @param user the user's whose profile is being shown.
	 * @throws IOException
	 * @throws UsernameNotFoundException
	 */
	public ProfileDisplay(final UserProfile user) throws IOException, UsernameNotFoundException {
		
		setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		setLayout(new MigLayout("", "[150px,grow][grow][150px]", "[][][][][][][][][][]"));
		
		JLabel titleLabel = new JLabel("Profile");
		titleLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLabel, "cell 1 0,alignx center");
		
		JLabel usernameTitleLabel = new JLabel("Username:");
		usernameTitleLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		usernameTitleLabel.setForeground(Color.WHITE);
		add(usernameTitleLabel, "cell 0 1, alignx trailing");
		
		JLabel usernameLabel = new JLabel("");
		usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setText(user.getUsername());
		add(usernameLabel, "cell 1 1");
		
		JLabel oldPasswordLabel = new JLabel("Old Password");
		oldPasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		oldPasswordLabel.setForeground(Color.WHITE);
		add(oldPasswordLabel, "cell 0 2,alignx trailing");
		
		oldPasswordField = new JPasswordField();
		add(oldPasswordField, "cell 1 2,growx");
		
		JLabel newPasswordLabel = new JLabel("New Password");
		newPasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		newPasswordLabel.setForeground(Color.WHITE);
		add(newPasswordLabel, "cell 0 3,alignx trailing");
		
		newPasswordField1 = new JPasswordField();
		add(newPasswordField1, "cell 1 3,growx");
		
		JLabel confirmPasswordLabel = new JLabel("Confirm");
		confirmPasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		confirmPasswordLabel.setForeground(Color.WHITE);
		add(confirmPasswordLabel, "cell 0 4,alignx trailing");
		
		newPasswordField2 = new JPasswordField();
		add(newPasswordField2, "cell 1 4,growx");
		
		
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		firstNameLabel.setForeground(Color.WHITE);
		add(firstNameLabel, "cell 0 5,alignx trailing");
		
		firstNameField = new JTextField();
		firstNameField.setText(user.getFirstName());
		add(firstNameField, "cell 1 5,growx");
		firstNameField.setColumns(10);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		lastNameLabel.setForeground(Color.WHITE);
		
		add(lastNameLabel, "cell 0 6,alignx trailing");
		
		lastNameField = new JTextField();
		lastNameField.setText(user.getLastName());
		add(lastNameField, "cell 1 6,growx");
		lastNameField.setColumns(10);
		
		JLabel emailLabel = new JLabel("Email Address");
		emailLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		emailLabel.setForeground(Color.WHITE);
		add(emailLabel, "cell 0 7,alignx trailing");
		
		emailField = new JTextField();
		emailField.setText(user.getEmail());
		add(emailField, "cell 1 7,growx");
		emailField.setColumns(10);
		
		final JLabel deleteMessage = new JLabel("To delete your profile, enter your password.");
		deleteMessage.setForeground(Color.WHITE);
		add(deleteMessage, "cell 0 8 2 1");
		
		passwordField = new JPasswordField();
		add(passwordField, "flowx,cell 0 9,growx");
		
		JButton saveButton = new JButton("Save");
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				user.setFirstName(firstNameField.getText());
				user.setLastName(lastNameField.getText());
				user.setEmail(emailField.getText());
				try {
					String newPassword1 = new String(newPasswordField1.getPassword());
					String newPassword2 = new String(newPasswordField2.getPassword());
					if(newPassword1.equals(newPassword2)) {
						user.changePassword(new String(oldPasswordField.getPassword()), newPassword1);
						Users.getInstance().save();
					} else {
						deleteMessage.setText("The passwords do not match.");
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (WrongPasswordException e) {
					deleteMessage.setText("You have entered the wrong password.");
					return;
				}

				WindowManager.openMainMenu();
			}
		});
		add(saveButton, "flowx,cell 2 9");
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				WindowManager.openMainMenu();
			}
		});
		add(cancelButton, "cell 2 9");
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				/*try {*/
					//Add func
					//CurrentUser.getInstance().isRightPassword(new String(passwordField.getPassword()), func);
					try {
						Users.getInstance().removeUser(CurrentUser.getInstance());
					} catch (IOException e) {
						e.printStackTrace();
					}
					CurrentUser.setInstance(new GuestProfile());
					WindowManager.openMainMenu();
				/*} catch (WrongPasswordException e) {
					deleteMessage.setText("You have entered the wrong password.");
				}*/
			}
		});
		deleteButton.setForeground(Color.RED);
		add(deleteButton, "cell 0 9");
	}
	
	/**
	 * Dispose of this window.
	 */
	public void dispose() {
		this.dispose();
	}

}
