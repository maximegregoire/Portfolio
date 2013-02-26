package storage;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import users.UserProfile;

public class SimpleStorage implements PersistentStorage {
	
		private File userfile;
		
		public SimpleStorage(String filepath) {
			this.userfile = new File(filepath);
		}

		public List<UserProfile> getUsers() throws IOException {
			FileInputStream fileIn;
			try {
				fileIn = new FileInputStream(userfile);
			} catch (FileNotFoundException e) {
				userfile.createNewFile();
				fileIn = new FileInputStream(userfile);
			}
			try {
				ObjectInputStream in = new ObjectInputStream(fileIn);
				try {
					return (List<UserProfile>) in.readObject();
				} catch (Exception e) { //Refine to whatever exception is a bad typecast
					throw new IOException();
				} finally {
					in.close();
					fileIn.close();
				}
			} catch (EOFException e) {
				return new ArrayList<UserProfile>();
			}
		}
		
		public void saveUsers(List<UserProfile> users) throws IOException {
			FileOutputStream fileOut = new FileOutputStream(userfile, false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(users);
			out.close();
			fileOut.close();
		}	
}
