package users;

import java.io.Serializable;

public abstract class HashFunction implements Serializable {
	public abstract int hash(String s);
}
