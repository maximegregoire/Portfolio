package users;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import storage.PersistentStorage;


public class Users implements Serializable {
	private static Users instance;
	private List<UserProfile> users;
	private HashFunction hashFunction; //Should be retrieved from persistent storage
	private PersistentStorage storage;
	
	public Users(PersistentStorage storage, HashFunction function) throws IOException {
		this.storage = storage;
		users = storage.getUsers();
		this.hashFunction = function;
		instance = this;
	}
	
	public static Users getInstance() {
		return instance;
	}
	
	public void save() throws IOException {
		storage.saveUsers(users);
	}
	
	public UserProfile addUser(String username, String password) throws UsernameExistsException, IOException {
		if (userExists(username)) {
			throw new UsernameExistsException();
		} else {
			UserProfile res = new UserProfile(username, password, this.hashFunction);
			users.add(res);
			this.save();
			return res;
		}
	}
	
	public void removeUser(UserProfile user) throws IOException {
		users.remove(user);
		this.save();
	}

	public List<UserProfile> getUsers() {
		return users;
	}

	public boolean userExists(String username) {
		try {
			getUser(username);
			return true;
		} catch (UsernameNotFoundException e) {
			return false;
		}
	}
	
	private UserProfile getUser(String username) throws UsernameNotFoundException {
		for(UserProfile u : users) {
			if(u.getUsername().equals(username)) {
				return u;
			}
		}
		throw new UsernameNotFoundException();
	}
	
	public UserProfile getUser(String username, String password) throws UsernameNotFoundException, WrongPasswordException {
		UserProfile res = getUser(username);
		if (!res.isRightPassword(password)) {
			throw new WrongPasswordException();
		}
		return res;
	}

	public HashFunction getHashFunction() {
		return hashFunction;
	}

	public void setHashFunction(HashFunction hashFunction) {
		this.hashFunction = hashFunction;
	}
}
