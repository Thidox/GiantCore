package nl.giantit.minecraft.database;

import nl.giantit.minecraft.database.drivers.h2.H2Driver;
import nl.giantit.minecraft.database.drivers.mysql.MySQLDriver;
import nl.giantit.minecraft.database.drivers.sqlite.SQLiteDriver;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class Database {
	
	private enum dbType {
		MySQL("MySQL"),
		hTwo("h2"),
		SQLite("SQLite");
		
		String value;
		
		private dbType(String s) {
			this.value = s;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
	
	private HashMap<String, Driver> instance = new HashMap<String, Driver>();
	private Plugin p;
	
	public Database(Plugin p, HashMap<String, String> conf, String instID) {
		this.p = p;
		
		if(instID == null)
			instID = "0";
		
		this.instance = new HashMap<String, Driver>();
		
		String d = conf.get("driver");
		
		Driver dbDriver;
		if(d.equalsIgnoreCase("MySQL")) {
			dbDriver = new MySQLDriver(p, conf);
		}else if(d.equalsIgnoreCase("h2")) {
			dbDriver = new H2Driver(p, conf);
		}else{
			dbDriver = new SQLiteDriver(p, conf);
		}
		
		this.instance.put(instID, dbDriver);
	}
	
	public Driver getEngine() {
		return this.getEngine(null);
	}
	
	public Driver getEngine(String instID) {
		if(instID == null)
			instID = "0";
		
		if(this.instance.containsKey(instID))
			return this.instance.get(instID);
		
		// Instance doesn't exist, nor were there any config data passed to init
		return null;
	}
	
	public Driver getEngine(String instID, HashMap<String, String> conf) {
		if(instID == null)
			instID = "0";
		
		if(this.instance.containsKey(instID))
			return this.instance.get(instID);
		
		String d = conf.get("driver");
		
		Driver dbDriver;
		if(d.equalsIgnoreCase("MySQL")) {
			dbDriver = new MySQLDriver(p, conf);
		}else if(d.equalsIgnoreCase("h2")) {
			dbDriver = new H2Driver(p, conf);
		}else{
			dbDriver = new SQLiteDriver(p, conf);
		}
		
		this.instance.put(instID, dbDriver);
		return dbDriver;
	}
}
