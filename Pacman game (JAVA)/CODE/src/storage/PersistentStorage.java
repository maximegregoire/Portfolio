package storage;

import java.io.IOException;
import java.util.List;

import users.UserProfile;

public interface PersistentStorage {
	public List<UserProfile> getUsers() throws IOException;
	public void saveUsers(List<UserProfile> users) throws IOException;
}
