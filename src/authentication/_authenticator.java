package authentication;

import user.*;

public interface _authenticator {
	public boolean can_do(session client, _action action);
}
