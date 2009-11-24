package authentication;

import java.util.*;
import user.*;

public class sentry implements _authenticator {
	private ArrayList<_authenticator> _auth_modules;
	
	private sentry() {
		_auth_modules = new ArrayList<_authenticator>();
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
	
	/* singleton */
	private static sentry instance = new sentry();
	
	public static sentry get_instance() {
		// no lazy loading
		return instance;
	}
	/* singleton */
}
