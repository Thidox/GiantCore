package nl.giantit.minecraft.giantcore.perms.Engines;

import nl.giantit.minecraft.giantcore.GiantCore;
import nl.giantit.minecraft.giantcore.perms.Permission;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.logging.Level;

public class pexEngine implements Permission {

	private GiantCore plugin;
	private PermissionsEx permission;
	private Boolean opHasPerms;
	
	public pexEngine(GiantCore plugin, Boolean opHasPerms) {
		this.plugin = plugin;
		this.opHasPerms = opHasPerms;
		
		Plugin perms = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
        if (perms != null && perms.isEnabled()) {
        	permission = (PermissionsEx) perms;
			plugin.getLogger().log(Level.INFO, "Successfully hooked into PermissionsEx");
        }else{
        	//It's not enabled yet, let's set up a listener!
        	plugin.getServer().getPluginManager().registerEvents(new PluginListener(), plugin);
        }
	}
	
	@Override
	public boolean has(String p, String perm) {
		Player player = plugin.getServer().getPlayer(p);
		if(player != null)
			return this.has(player, perm);
		
		return false;
	}

	@Override
	public boolean has(Player p, String perm) {
		if(opHasPerms && p.isOp())
			return true;
		
		return permission.has(p, perm);
	}
	
	@Override
	public boolean has(String p, String perm, String world) {
		Player player = plugin.getServer().getPlayer(p);
		if(player != null)
			return this.has(player, perm);
		
		return false;
	}

	@Override
	public boolean has(Player p, String perm, String world) {
		if(opHasPerms && p.isOp())
			return true;
		
		return permission.has(p, perm, world);
	}

	@Override
	public boolean groupHasPerm(String group, String perm) {
		PermissionGroup g = PermissionsEx.getPermissionManager().getGroup(group);
		if(g != null)
			return g.has(perm);
		
		return false;
	}

	@Override
	public boolean groupHasPerm(String group, String perm, String world) {
		PermissionGroup g = PermissionsEx.getPermissionManager().getGroup(group);
		if(g != null)
			return g.has(perm, world);
		
		return false;
	}

	@Override
	public boolean inGroup(String p, String group) {
		return PermissionsEx.getPermissionManager().getUser(p).inGroup(group);
	}

	@Override
	public boolean inGroup(Player p, String group) {
		return this.inGroup(p.getName(), group);
	}
	
	@Override
	public boolean inGroup(String p, String group, String world) {
		return PermissionsEx.getPermissionManager().getUser(p).inGroup(group, world);
	}

	@Override
	public boolean inGroup(Player p, String group, String world) {
		return this.inGroup(p.getName(), group, world);
	}

	@Override
	public void setGroup(String p, String group) {
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "pex user " + p + " group set " + group);
	}

	@Override
	public void setGroup(Player p, String group) {
		this.setGroup(p.getName(), group);
	}

	@Override
	public void setGroup(String p, String group, String world) {
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "pex user " + p + " group set " + group + " " + world);
	}

	@Override
	public void setGroup(Player p, String group, String world) {
		this.setGroup(p.getName(), group, world);
	}

	@Override
	public String getGroup(String p) {
		String[] groups = PermissionsEx.getPermissionManager().getUser(p).getGroupsNames();
		if(groups.length > 0)
			return groups[0];
		
		return null;
	}

	@Override
	public String getGroup(Player p) {
		return this.getGroup(p.getName());
	}

	@Override
	public String getGroup(String p, String world) {
		String[] groups = PermissionsEx.getPermissionManager().getUser(p).getGroupsNames(world);
		if(groups.length > 0)
			return groups[0];
		
		return null;
	}

	@Override
	public String getGroup(Player p, String world) {
		return this.getGroup(p.getName(), world);
	}

	@Override
	public String[] getGroups(String p) {
		return PermissionsEx.getPermissionManager().getUser(p).getGroupsNames();
	}

	@Override
	public String[] getGroups(Player p) {
		return this.getGroups(p.getName());
	}

	@Override
	public String[] getGroups(String p, String world) {
		return PermissionsEx.getPermissionManager().getUser(p).getGroupsNames(world);
	}

	@Override
	public String[] getGroups(Player p, String world) {
		return this.getGroups(p.getName(), world);
	}
	
	@Override
	public void setPrefix(Player p, String prefix) {
		this.setPrefix(p.getName(), prefix, p.getWorld().getName());
	}
	
	@Override
	public void setPrefix(String p, String prefix, String world) {
		PermissionsEx.getPermissionManager().getUser(p).setPrefix(prefix, world);
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
		return PermissionsEx.getPermissionManager().getUser(p).getPrefix(world);
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
		return PermissionsEx.getPermissionManager().getGroup(g).getPrefix(world);
	}
	
	@Override
	public boolean isEnabled() {
		return permission.isEnabled();
	}
	
	private class PluginListener implements Listener {
		
		public PluginListener() {}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onPluginEnable(PluginEnableEvent event) {
			if(permission == null) {
				Plugin p = event.getPlugin();
				if(p instanceof PermissionsEx) {
					permission = (PermissionsEx) p;
					plugin.getLogger().log(Level.INFO, "Successfully hooked into PermissionsEx");
				}
			}
		}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onPluginDisable(PluginDisableEvent event) {
			if(permission != null) {
				if(event.getPlugin().getDescription().getName().equals("PermissionsEx")) {
					permission = null;
					plugin.getLogger().log(Level.INFO, "unhooked from PermissionsEx");
				}
			}
		}
	}
}
