package database;

import java.io.Serializable;
import java.util.ArrayList;

public class database implements Serializable {
	private static final long serialVersionUID = 8659010261409419224L;
	
	public String name;
	public String read_password;
	public String write_password;
	public String admin_password;
	
	public ArrayList<database_entry> entries;
	
	public database(String n, String rp, String wp, String ap) {
		name = n;
		read_password = rp;
		write_password = wp;
		admin_password = ap;
		entries = new ArrayList<database_entry>();
	}
}
