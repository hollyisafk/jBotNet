package user;

import storage._product;

public class ipmask extends _product {
	private static final long serialVersionUID = 2914008190537014119L;
	
	public enum permission {
		None,
		Banned,
		Safe,
		Moderator,
		Admin
	}
	
	private String _mask;
	private permission _permissions;
	
	public ipmask(String mask, permission permissions) {
		_mask = mask;
		_permissions = permissions;
	}
	
	public permission get_permissions() {
		return _permissions;
	}
	
	public boolean matches(String ip) {
        String [] cards = _mask.split("\\*");

        for (String card : cards)
        {
            int idx = ip.indexOf(card);
            
            if(idx == -1)
            {
                return false;
            }
            
            ip = ip.substring(idx + card.length());
        }
        
        return true;

	}
}
