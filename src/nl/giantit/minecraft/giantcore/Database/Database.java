package nl.giantit.minecraft.giantcore.Database;

import nl.giantit.minecraft.giantcore.Database.drivers.MySQL;
import nl.giantit.minecraft.giantcore.Database.drivers.SQLite;
import nl.giantit.minecraft.giantcore.Database.drivers.hTwo;

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
	
	private HashMap<String, iDriver> instance = new HashMap<String, iDriver>();
	private Plugin p;
	
	public Database(Plugin p, HashMap<String, String> conf, String instID) {
		this.p = p;
		
		if(instID == null)
			instID = "0";
		
		this.instance = new HashMap<String, iDriver>();
		
		String d = conf.get("driver");
		
		iDriver dbDriver;
		if(d.equalsIgnoreCase("MySQL")) {
			dbDriver = new MySQL(p, conf);
		}else if(d.equalsIgnoreCase("h2")) {
			dbDriver = new hTwo(p, conf);
		}else{
			dbDriver = new SQLite(p, conf);
		}
		
		this.instance.put(instID, dbDriver);
	}
	
	public iDriver getEngine() {
		return this.getEngine(null);
	}
	
	public iDriver getEngine(String instID) {
		if(instID == null)
			instID = "0";
		
		if(this.instance.containsKey(instID))
			return this.instance.get(instID);
		
		// Instance doesn't exist, nor were there any config data passed to init
		return null;
	}
	
	public iDriver getEngine(String instID, HashMap<String, String> conf) {
		if(instID == null)
			instID = "0";
		
		if(this.instance.containsKey(instID))
			return this.instance.get(instID);
		
		String d = conf.get("driver");
		
		iDriver dbDriver;
		if(d.equalsIgnoreCase("MySQL")) {
			dbDriver = new MySQL(p, conf);
		}else if(d.equalsIgnoreCase("h2")) {
			dbDriver = new hTwo(p, conf);
		}else{
			dbDriver = new SQLite(p, conf);
		}
		
		this.instance.put(instID, dbDriver);
		return dbDriver;
	}
	
}
