package database;

public class account_factory extends _factory<account> {
	public account_factory() {
		super(System.getProperty("user.dir") + "/data/accounts.jbn");
	}
	
	public account get_account(String username) {
		return entries.get(username);
	}
	
	public boolean account_exists(String username) {
		return entries.get(username) != null ? true : false;
	}
	
	public boolean create_account(String username, String password, int flags) {
		if (account_exists(username))
			return false;
		
		add(new account(username, password, flags));
		save();
		
		return true;
	}
	
	public boolean change_password(String username, String newpassword) {
		if (!account_exists(username))
			return false;
		
		get_account(username).password = newpassword;
		save();
		
		return true;
	}
}
