package nl.giantit.minecraft.giantcore;

import nl.giantit.minecraft.giantcore.Database.Database;
import nl.giantit.minecraft.giantcore.perms.PermHandler;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class GiantCore extends JavaPlugin {
	
	private static GiantCore instance;
	private double protocolVersion = 0.2;
	private HashMap<PermHandler.Engines, HashMap<Boolean, PermHandler>> permHandler = new HashMap<PermHandler.Engines, HashMap<Boolean, PermHandler>>();
	private HashMap<Plugin, Database> dbList = new HashMap<Plugin, Database>();
	//private Messages msgHandler;
	
	private void setInstance() {
		GiantCore.instance = this;
	}
	
	public GiantCore() {
		this.setInstance();
	}
	
	@Override
	public void onEnable() {
		//this.permHandler = new PermHandler(this, "", true);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public double getProtocolVersion() {
		return this.protocolVersion;
	}
	
	public Database getDB(Plugin p) {
		if(this.dbList.containsKey(p)) {
			return this.dbList.get(p);
		}
		
		return null;
	}
	
	public Database getDB(Plugin p, String instance, HashMap<String, String> conf) {
		if(this.dbList.containsKey(p)) {
			return this.dbList.get(p);
		}
		
		Database db = new Database(p, conf, instance);
		this.dbList.put(p, db);
		
		return db;
	}
	
	public PermHandler getPermHandler(PermHandler.Engines permEngine) {
		return this.getPermHandler(permEngine, true);
	}
	
	public PermHandler getPermHandler(PermHandler.Engines permEngine, boolean opHasPerms) {
		if(this.permHandler.containsKey(permEngine)) {
			HashMap<Boolean, PermHandler> handlers = this.permHandler.get(permEngine);
			
			if(handlers.containsKey(opHasPerms)) {
				return handlers.get(opHasPerms);
			}
			
			PermHandler pH = new PermHandler(this, permEngine, opHasPerms);
			handlers.put(opHasPerms, pH);
			this.permHandler.put(permEngine, handlers);
			
			return pH;
		}
		
		PermHandler pH = new PermHandler(this, permEngine, opHasPerms);
		HashMap<Boolean, PermHandler> handlers = new HashMap<Boolean, PermHandler>();
		handlers.put(opHasPerms, pH);
		this.permHandler.put(permEngine, handlers);
		
		return pH;
	}
	
	public static GiantCore getInstance() {
		return instance;
	}
	
}
