package user;


import java.util.ArrayList;

import storage._warehouse;

public class ipmask_factory extends _warehouse<ipmask> {
	private static final long serialVersionUID = 4897544196537665426L;
	
	private static ipmask_factory instance = new ipmask_factory();
	
	public static ipmask_factory get_instance() {
		// no lazy loading
		return instance;
	}
	
	private ipmask_factory() {
		super(System.getProperty("user.dir") + "/data/ipmasks.jbn");
		load();
	}
	
	public ipmask get_ipmask(String mask) {
		return get_entries().get(mask.toLowerCase());
	}
	
	public ArrayList<ipmask> get_matches(String ip) {
		ArrayList<ipmask> matches = new ArrayList<ipmask>();
		
		for (ipmask mask : get_collection())
			if (mask.matches(ip))
				matches.add(mask);
		
		return matches;
	}
	
	public boolean ipmask_exists(String mask) {
		return get_entries().get(mask.toLowerCase()) != null ? true : false;
	}
}
