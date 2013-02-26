package users;

public class SimpleHash extends HashFunction {

	@Override
	public int hash(String s) {
		return s.hashCode();
	}
}
