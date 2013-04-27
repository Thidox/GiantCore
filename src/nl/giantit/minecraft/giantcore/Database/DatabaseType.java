package nl.giantit.minecraft.giantcore.Database;

/**
 *
 * @author Giant
 */
public enum DatabaseType {
	
	SQLite("SQLite"),
	MySQL("MySQL"),
	hTwo("h2"),
	Central("Central library");
	
	private String name;
	
	private DatabaseType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}
