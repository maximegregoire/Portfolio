package storage;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import storage.SimpleStorage;
import users.HashFunction;
import users.SimpleHash;
import users.UserProfile;

public class SimpleStorageTest {
	final static String FILEPATH = "etc/userstest";
	final static File f = new File(FILEPATH);
	static SimpleStorage s;
	
	HashFunction testHash = new SimpleHash();
	UserProfile normalUser = new UserProfile("testuser", "testpassword", testHash);
	UserProfile shortUser = new UserProfile("", "", testHash);
	UserProfile longUser = new UserProfile("arbitrarilylongusername", "arbitrarilylongpassword", testHash);
	
	List<UserProfile> userIn = new ArrayList<UserProfile>();
	List<UserProfile> userOut = new ArrayList<UserProfile>();
	UserProfile[] userList = new UserProfile[]{normalUser, shortUser, longUser};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		if (f.exists()) {
			
			throw new RuntimeException("SimpleStorage unit test was not properly torn down last time it ran (file size = " + f.length() + ")");
		}
		s = new SimpleStorage(FILEPATH);
	}

	@AfterClass
	public static void tearDownAfterClass() {
		f.delete();
	}
	
	@Test
	public void testSimpleStorage() {
		new SimpleStorage(FILEPATH);
	}
	
	/**
	 *  Saves userIn to file, retrieves it and stores in userOut;
	 *  checks for equality.
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	
	private void CheckIO() {
		try {
			s.saveUsers(userIn);
		} catch (IOException e) {
			fail("IOException while writing users to file");
		}

		try {
			userOut = s.getUsers();
		} catch (IOException e) {
			fail("IOException while reading users from file");
		}
			
		if (!userIn.equals(userOut)) {
			fail("input and output are different");
		}
		
	}

	@Test
	public void testRetrieval() {
		//Test case #1: adding users
		for (UserProfile u : userList) {
			userIn.add(u);
			CheckIO();
		}
		
		//Test case #2: removing users
		//Test case #3: zero users
		for (UserProfile u : userList) {
			userIn.remove(u);
			CheckIO();
		}
	}
}
