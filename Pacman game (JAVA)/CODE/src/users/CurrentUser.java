package users;


/**
 * This class defines the instance of a logged-in user or guest.
 * @author Carl Patenaude Poulin
 * @author Antoine Tu
 *
 */
public class CurrentUser {
	private static UserProfile user;
	
	/**
	 * Gets the current user instance.
	 * @return the current user object
	 */
	
	public static UserProfile getInstance() {
		return user;
	}
	
	/**
	 * Sets the current user.
	 * @param UserProfile the profile of the current user.
	 */
	public static void setInstance(UserProfile u) {
		CurrentUser.user = u;
	}
}
