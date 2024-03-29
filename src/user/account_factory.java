package user;

import java.util.regex.*;

import storage._warehouse;

public class account_factory extends _warehouse<account> {
	private static final long serialVersionUID = 8879979329784644841L;

	private static account_factory instance = new account_factory();
	
	public static account_factory get_instance() {
		// no lazy loading
		return instance;
	}
	
	private account_factory() {
		super(System.getProperty("user.dir") + "/data/accounts.jbn");
		load();
	}
	
	public account get_account(String username) {
		return get_entries().get(username.toLowerCase());
	}
	
	public boolean account_exists(String username) {
		return get_entries().get(username.toLowerCase()) != null ? true : false;
	}
	
	public String create_account(String username, String password, int flags) {
		try {
			Pattern p = Pattern.compile("^[A-Za-z0-9_]*$");
			Matcher m = p.matcher(username);
			boolean b = m.matches();
			
			if (!b)
				return "Invalid account name";
			
			add(new account(username, password, flags));
			save();
			
			return null;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public boolean change_password(String username, String newpassword) {
		try {
			if (!account_exists(username))
				return false;
			
			get_account(username).set_password(newpassword);
			save();
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
