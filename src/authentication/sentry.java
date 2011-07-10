package authentication;

import java.util.*;

import net.listener;
import user.*;

public class sentry implements _authenticator {
	private ArrayList<_authenticator> _auth_modules;
	
	private sentry() {
		_auth_modules = new ArrayList<_authenticator>();
		register(new chat_protection());
		register(new data_protection());
		register(new login_procedure());
		register(new server_protection());
		register(new user_protection());
	}
	
	public boolean register(_authenticator module) {
		if (_auth_modules.contains(module))
			return false;
		else
			_auth_modules.add(module);
			return true;
	}
	
	public boolean can_do(session client, _action action) {
		for (_authenticator module : _auth_modules)
			if (!module.can_do(client, action))
				return false;
		
		return true;
	}
	
	public boolean perform(listener client, _action action) {
		try {
			session s = client.get_session();
			if (!can_do(s, action))
				return false;
			
			return action.perform(client);
		} catch (Exception e) {
			return false;
		}
	}
	
	/* singleton */
	private static sentry instance = new sentry();
	
	public static sentry get_instance() {
		// no lazy loading
		return instance;
	}
	/* singleton */
}
