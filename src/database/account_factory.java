package database;

public class account_factory extends _factory<account> {
	public account_factory() {
		super(System.getProperty("user.dir") + "/data/accounts.jbn");
	}
	
	public boolean create_account(String username, String password, int flags) {
		boolean exists = false;
		
		for (account a : entries) {
			if (a.username.equalsIgnoreCase(username)) {
				exists = true;
				break;
			}
		}
		
		if (exists)
			return false;
		
		add(new account(username, password, flags));
		save();
		
		return true;
	}
}
