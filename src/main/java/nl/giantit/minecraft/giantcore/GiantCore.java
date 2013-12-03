package nl.giantit.minecraft.giantcore;

import nl.giantit.minecraft.database.Database;
import nl.giantit.minecraft.giantcore.core.Eco.Eco;
import nl.giantit.minecraft.giantcore.core.Items.Items;
import nl.giantit.minecraft.giantcore.perms.PermHandler;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author Giant
 */
public class GiantCore extends JavaPlugin {
	
	private static GiantCore instance;
	private final double protocolVersion = 0.3;
	private final HashMap<PermHandler.Engines, HashMap<Boolean, PermHandler>> permHandler = new HashMap<PermHandler.Engines, HashMap<Boolean, PermHandler>>();
	private final HashMap<Eco.Engines, Eco> ecoHandler = new HashMap<Eco.Engines, Eco>();
	private final HashMap<Plugin, Database> dbList = new HashMap<Plugin, Database>();
	private Items itemHandler;
	
	private String dir;
	
	private void setInstance() {
		GiantCore.instance = this;
	}
	
	public GiantCore() {
		this.setInstance();
	}
	
	@Override
	public void onEnable() {
		
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
			getDataFolder().setWritable(true);
			getDataFolder().setExecutable(true);
		}
		
		this.dir = getDataFolder().toString();
		
		itemHandler = new Items(this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public double getProtocolVersion() {
		return this.protocolVersion;
	}
	
	public String getDir() {
		return this.dir;
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
	
	public Items getItemHandler() {
		return this.itemHandler;
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
	
	public Eco getEcoHandler(Eco.Engines ecoEngine) {
		if(this.ecoHandler.containsKey(ecoEngine)) {
			return this.ecoHandler.get(ecoEngine);
		}
		
		Eco eH = new Eco(this, ecoEngine);
		this.ecoHandler.put(ecoEngine, eH);
		
		return eH;
	}
	
	public void extract(String file) {
		this.extractDefaultFile(file);
	}
	
	public void extract(File file, String sourceFile, String resPath) {
		this.extractDefaultFile(file, sourceFile, resPath);
	}
	
	public void extract(File file, InputStream input) {
		this.extractDefaultFile(file, input);
	}
	
	private void extractDefaultFile(String file) {
		this.extractDefaultFile(new File(getDataFolder(), file), file, "/core/Default/");
	}
	
	private void extractDefaultFile(File file, String sourceFile, String resPath) {
		if (!file.exists()) {
			String path = resPath + sourceFile;
			InputStream input = this.getClass().getResourceAsStream("/nl/giantit/minecraft/" + getDescription().getName().toLowerCase() + path);
			
			this.extract(file, input);
		}
	}
	
	private void extractDefaultFile(File file, InputStream input) {
		if (!file.exists()) {
			try {
			 file.createNewFile();
			}catch(IOException e) {
				getLogger().log(Level.SEVERE, "Can't extract the requested file!!", e);
			}
		}
		if (input != null) {
			FileOutputStream output = null;

			try {
				output = new FileOutputStream(file);
				byte[] buf = new byte[8192];
				int length;

				while ((length = input.read(buf)) > 0) {
					output.write(buf, 0, length);
				}

				getLogger().log(Level.INFO, "copied default file: " + file);
				output.close();
			} catch (Exception e) {
				getServer().getPluginManager().disablePlugin(this);
				getLogger().log(Level.SEVERE, "AAAAAAH!!! Can't extract the requested file!!", e);
			} finally {
				try {
					input.close();
				} catch (Exception e) {
					getServer().getPluginManager().disablePlugin(this);
					getLogger().log(Level.SEVERE, "AAAAAAH!!! Severe error!!", e);	
				}
				try {
					output.close();
				} catch (Exception e) {
					getServer().getPluginManager().disablePlugin(this);
					getLogger().log(Level.SEVERE, "AAAAAAH!!! Severe error!!", e);
				}
			}
		}
	}
	
	public static GiantCore getInstance() {
		return instance;
	}
	
}
