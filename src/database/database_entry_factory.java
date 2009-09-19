package database;

public class database_entry_factory extends _factory<database_entry> {
	public database_entry_factory() {
		super("");
	}
	
	public database_entry get_entry(String username) {
		return entries.get(username);
	}
}
