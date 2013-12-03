package nl.giantit.minecraft.giantcore;

import nl.giantit.minecraft.database.Database;
import nl.giantit.minecraft.giantcore.Misc.Messages;
import nl.giantit.minecraft.giantcore.core.Eco.Eco;
import nl.giantit.minecraft.giantcore.perms.PermHandler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author Giant
 */
public abstract class GiantPlugin extends JavaPlugin {

	private final static Map<String, GiantPlugin> instance = new HashMap<String, GiantPlugin>();

	protected boolean addInstance(String path, GiantPlugin p) {
		if(!instance.containsKey(path)) {
			instance.put(path, p);
			return true;
		}

		return false;
	}

	public abstract GiantCore getGiantCore();

	public abstract String getPubName();

	public abstract String getDir();

	public abstract Database getDB();

	public abstract PermHandler getPermHandler();

	public abstract Eco getEcoHandler();

	public abstract Messages getMsgHandler();

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
		if(!file.exists()) {
			String path = resPath + sourceFile;
			//getLogger().severe("/nl/giantit/minecraft/" + getDescription().getName().toLowerCase() + path);
			InputStream input = this.getClass().getResourceAsStream("/nl/giantit/minecraft/" + getDescription().getName().toLowerCase() + path);

			this.extractDefaultFile(file, input);
		}
	}

	private void extractDefaultFile(File file, InputStream input) {
		// Extract files async to avoid extracting collisions.
		(new FileExtractTask(this, file, input)).run();
	}

	private class FileExtractTask extends BukkitRunnable {

		private final GiantPlugin p;
		private final File file;
		private final InputStream input;

		public FileExtractTask(final GiantPlugin p, final File file, final InputStream input) {
			this.p = p;
			this.file = file;
			this.input = input;
		}

		@Override
		public void run() {
			if(!file.exists()) {
				try {
					file.createNewFile();
				}catch(IOException e) {
					p.getLogger().log(Level.SEVERE, "Can't extract the requested file!!", e);
				}
			}
			
			if(input != null) {
				FileOutputStream output = null;

				try {
					output = new FileOutputStream(file);
					byte[] buf = new byte[8192];
					int length;

					while((length = input.read(buf)) > 0) {
						output.write(buf, 0, length);
					}

					p.getLogger().log(Level.INFO, "copied default file: " + file);
					output.close();
				}catch(Exception e) {
					p.getServer().getPluginManager().disablePlugin(p);
					p.getLogger().log(Level.SEVERE, "AAAAAAH!!! Can't extract the requested file!!", e);
				}finally {
					try {
						input.close();
					}catch(Exception e) {
						p.getServer().getPluginManager().disablePlugin(p);
						p.getLogger().log(Level.SEVERE, "AAAAAAH!!! Severe error!!", e);
					}
					try {
						if(null != output) {
							output.close();
						}
					}catch(Exception e) {
						p.getServer().getPluginManager().disablePlugin(this.p);
						p.getLogger().log(Level.SEVERE, "AAAAAAH!!! Severe error!!", e);
					}
				}
			}
		}

	}

	public static GiantPlugin getPlugin(Object c) {
		String[] parts = c.getClass().getName().split(".");
		String pkg = null;
		for(String part : parts) {
			if(null == pkg) {
				pkg = part;
			}else{
				pkg = "." + part;
			}

			if(instance.containsKey(pkg)) {
				return instance.get(pkg);
			}
		}

		return null;
	}
}
