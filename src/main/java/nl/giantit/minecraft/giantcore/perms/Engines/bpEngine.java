package nl.giantit.minecraft.giantcore.perms.Engines;

import nl.giantit.minecraft.giantcore.GiantCore;
import nl.giantit.minecraft.giantcore.perms.Permission;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

import de.bananaco.bpermissions.imp.Permissions;
import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;

public class bpEngine implements Permission {

	private GiantCore plugin;
	private Boolean permission = false;
	private Boolean opHasPerms;
	
	public bpEngine(GiantCore plugin, Boolean opHasPerms) {
		this.plugin = plugin;
		this.opHasPerms = opHasPerms;
		
		Plugin perms = plugin.getServer().getPluginManager().getPlugin("bPermissions");
        if (perms != null && perms.isEnabled()) {
        	permission = true;
			plugin.getLogger().log(Level.INFO, "Successfully hooked into bPermissions");
        }else{
        	//It's not enabled yet, let's set up a listener!
        	plugin.getServer().getPluginManager().registerEvents(new PluginListener(), plugin);
        }
	}
	@Override
	public boolean has(String p, String perm) {
		Player pl = plugin.getServer().getPlayer(p);
		if(pl == null)
			return false;
		
		return this.has(pl, perm);
	}

	@Override
	public boolean has(Player p, String perm) {
		return this.has(p.getName(), perm, p.getWorld().getName());
	}

	@Override
	public boolean has(String p, String perm, String world) {
		Player pl = plugin.getServer().getPlayer(p);
		if(pl == null)
			return false;
		
		if(opHasPerms && pl.isOp())
			return true;
		
		return ApiLayer.hasPermission(world, CalculableType.USER, p, perm);
	}

	@Override
	public boolean has(Player p, String perm, String world) {
		return this.has(p.getName(), perm, world);
	}

	@Override
	public boolean groupHasPerm(String group, String perm) {
		String world = plugin.getServer().getWorlds().get(0).getName();
		return this.groupHasPerm(group, world, perm);
	}

	@Override
	public boolean groupHasPerm(String group, String world, String perm) {
		return ApiLayer.hasPermission(world, CalculableType.GROUP, group, perm);
	}

	@Override
	public boolean inGroup(String p, String g) {
		Player pl = plugin.getServer().getPlayer(p);
		if(pl == null)
			return false;
		
		return false;
	}

	@Override
	public boolean inGroup(Player p, String group) {
		return this.inGroup(p.getName(), group, p.getWorld().getName());
	}

	@Override
	public boolean inGroup(String p, String g, String world) {
		return ApiLayer.hasGroup(world, CalculableType.USER, p, g);
	}

	@Override
	public boolean inGroup(Player p, String group, String world) {
		return this.inGroup(p.getName(), group, p.getWorld().getName());
	}

	@Override
	public void setGroup(String p, String group) {
		Player pl = plugin.getServer().getPlayer(p);
		if(pl != null) {
			this.setGroup(pl, group);
		}
	}

	@Override
	public void setGroup(Player p, String group) {
		this.setGroup(p, group, p.getWorld().getName());
	}

	@Override
	public void setGroup(String p, String group, String world) {
		ApiLayer.addGroup(world, CalculableType.USER, p, group);
	}

	@Override
	public void setGroup(Player p, String group, String world) {
		this.setGroup(p.getName(), group, world);
	}

	@Override
	public String getGroup(String p) {
		Player pl = plugin.getServer().getPlayer(p);
		if(pl == null)
			return null;

		return this.getGroup(p);
	}

	@Override
	public String getGroup(Player p) {
		return this.getGroup(p.getName(), p.getWorld().getName());
	}

	@Override
	public String getGroup(String p, String world) {
		String[] grp = ApiLayer.getGroups(world, CalculableType.USER, p);
		return grp != null && grp.length > 0 ? grp[0] : null;
	}

	@Override
	public String getGroup(Player p, String world) {
		return this.getGroup(p.getName(), world);
	}

	@Override
	public String[] getGroups(String p) {
		Player pl = plugin.getServer().getPlayer(p);
		if(pl == null)
			return null;

		return this.getGroups(p);
	}

	@Override
	public String[] getGroups(Player p) {
		return this.getGroups(p.getName(), p.getWorld().getName());
	}

	@Override
	public String[] getGroups(String p, String world) {
		return ApiLayer.getGroups(world, CalculableType.USER, p);
	}

	@Override
	public String[] getGroups(Player p, String world) {
		return this.getGroups(p.getName());
	}
	
	@Override
	public void setPrefix(Player p, String prefix) {
		this.setPrefix(p.getName(), prefix, p.getWorld().getName());
	}
	
	@Override
	public void setPrefix(String p, String prefix, String world) {
		plugin.getLogger().log(Level.WARNING, "bPermissions does not support player prefixes!");
	}
	
	@Override
	public void setPrefix(Player p, String prefix, String world) {
		this.setPrefix(p.getName(), prefix, world);
	}
	
	@Override
	public String getPrefix(Player p) {
		return this.getPrefix(p.getName(), p.getWorld().getName());
	}
	
	@Override
	public String getPrefix(String p, String world) {
		return "";
	}
	
	@Override
	public String getPrefix(Player p, String world) {
		return this.getPrefix(p.getName(), world);
	}
	
	@Override
	public String getGroupPrefix(Player p) {
		return this.getGroupPrefix(p, p.getWorld().getName());
	}
	
	@Override
	public String getGroupPrefix(Player p, String world) {
		return this.getGroupPrefix(p.getName(), this.getGroup(p), world);
	}

	@Override
	public String getGroupPrefix(String p, String g, String world) {
		return "";
	}
	
	@Override
	public boolean isEnabled() {
		return permission;
	}
	
	private class PluginListener implements Listener {
		
		public PluginListener() {}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onPluginEnable(PluginEnableEvent event) {
			if(permission == false) {
				Plugin p = event.getPlugin();
				if(p instanceof Permissions) {
		        	permission = true;
					plugin.getLogger().log(Level.INFO, "Successfully hooked into bPermissions");
				}
			}
		}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onPluginDisable(PluginDisableEvent event) {
			if(permission != false) {
				if(event.getPlugin().getDescription().getName().equals("bPermissions")) {
					permission = false;
					plugin.getLogger().log(Level.INFO, "unhooked from bPermissions");
				}
			}
		}
	}

}
