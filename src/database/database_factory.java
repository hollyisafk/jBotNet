package database;

public class database_factory extends _factory<database> {
	public database_factory() {
		super(System.getProperty("user.dir") + "/data/databases.jbn");
	}
}
