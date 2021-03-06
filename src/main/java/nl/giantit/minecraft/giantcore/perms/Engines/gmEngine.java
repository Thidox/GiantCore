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

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;

import java.util.logging.Level;

import org.anjocaido.groupmanager.data.User;

public class gmEngine implements Permission {

	private GiantCore plugin;
	private GroupManager permission;
	private Boolean opHasPerms;

	public gmEngine(GiantCore plugin, Boolean opHasPerms) {
		this.plugin = plugin;
		this.opHasPerms = opHasPerms;

		Plugin perms = plugin.getServer().getPluginManager().getPlugin("GroupManager");
		if(perms != null && perms.isEnabled()) {
			permission = (GroupManager)perms;
			plugin.getLogger().log(Level.INFO, "Successfully hooked into GroupManager");
		}else{
			//It's not enabled yet, let's set up a listener!
			plugin.getServer().getPluginManager().registerEvents(new PluginListener(), plugin);
		}
	}

	@Override
	public boolean has(String p, String perm) {
		Player player = plugin.getServer().getPlayer(p);
		if(player != null) {
			return this.has(player, perm);
		}

		return false;
	}

	@Override
	public boolean has(Player p, String perm) {
		if(opHasPerms && p.isOp()) {
			return true;
		}

		AnjoPermissionsHandler h = permission.getWorldsHolder().getWorldPermissionsByPlayerName(p.getName());
		if(h != null) {
			return h.has(p, perm);
		}

		return false;
	}

	@Override
	public boolean has(String p, String perm, String world) {
		Player player = plugin.getServer().getPlayer(p);
		if(player != null) {
			return this.has(player, perm, world);
		}

		return false;
	}

	@Override
	public boolean has(Player p, String perm, String world) {
		if(opHasPerms && p.isOp()) {
			return true;
		}

		AnjoPermissionsHandler h = permission.getWorldsHolder().getWorldPermissions(world);
		if(h != null) {
			return h.has(p, perm);
		}

		return false;
	}

	@Override
	public boolean groupHasPerm(String group, String perm) {
		OverloadedWorldHolder h = permission.getWorldsHolder().getDefaultWorld();
		if(h != null) {
			Group g = h.getGroup(group);
			if(g != null) {
				g.hasSamePermissionNode(perm);
			}
		}

		return false;
	}

	@Override
	public boolean groupHasPerm(String group, String perm, String world) {
		OverloadedWorldHolder h = permission.getWorldsHolder().getWorldData(world);
		if(h != null) {
			Group g = h.getGroup(group);
			if(g != null) {
				g.hasSamePermissionNode(perm);
			}
		}

		return false;
	}

	@Override
	public boolean inGroup(String p, String group) {
		AnjoPermissionsHandler h = permission.getWorldsHolder().getWorldPermissionsByPlayerName(p);
		if(h != null) {
			return h.inGroup(p, group);
		}

		return false;
	}

	@Override
	public boolean inGroup(Player p, String group) {
		return this.inGroup(p.getName(), group);
	}

	@Override
	public boolean inGroup(String p, String group, String world) {
		AnjoPermissionsHandler h = permission.getWorldsHolder().getWorldPermissions(p);
		if(h != null) {
			return h.inGroup(p, group);
		}

		return false;
	}

	@Override
	public boolean inGroup(Player p, String group, String world) {
		return this.inGroup(p.getName(), group, world);
	}

	@Override
	public void setGroup(String p, String group) {
	}

	@Override
	public void setGroup(Player p, String group) {
	}

	@Override
	public void setGroup(String p, String group, String world) {
	}

	@Override
	public void setGroup(Player p, String group, String world) {
	}

	@Override
	public String getGroup(String p) {
		AnjoPermissionsHandler h = permission.getWorldsHolder().getWorldPermissionsByPlayerName(p);
		if(h != null) {
			return h.getGroup(p);
		}

		return null;
	}

	@Override
	public String getGroup(Player p) {
		return this.getGroup(p.getName());
	}

	@Override
	public String getGroup(String p, String world) {
		AnjoPermissionsHandler h = permission.getWorldsHolder().getWorldPermissions(world);
		if(h != null) {
			return h.getGroup(p);
		}

		return null;
	}

	@Override
	public String getGroup(Player p, String world) {
		return this.getGroup(p.getName(), world);
	}

	@Override
	public String[] getGroups(String p) {
		AnjoPermissionsHandler h = permission.getWorldsHolder().getWorldPermissionsByPlayerName(p);
		if(h != null) {
			return h.getGroups(p);
		}

		return null;
	}

	@Override
	public String[] getGroups(Player p) {
		return this.getGroups(p.getName());
	}

	@Override
	public String[] getGroups(String p, String world) {
		AnjoPermissionsHandler h = permission.getWorldsHolder().getWorldPermissions(world);
		if(h != null) {
			return h.getGroups(p);
		}

		return null;
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
		OverloadedWorldHolder owh;
		if(world == null) {
			owh = permission.getWorldsHolder().getWorldDataByPlayerName(world);
		}else{
			owh = permission.getWorldsHolder().getWorldData(world);
		}
		if(owh == null) {
			return;
		}

		User user = owh.getUser(p);
		if(user == null) {
			return;
		}

		user.getVariables().addVar("prefix", prefix);
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
		AnjoPermissionsHandler h;
		if(world == null) {
			h = permission.getWorldsHolder().getWorldPermissionsByPlayerName(p);
		}else{
			h = permission.getWorldsHolder().getWorldPermissions(world);
		}

		if(h == null) {
			return "";
		}

		return h.getUserPrefix(p);
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
		AnjoPermissionsHandler h;
		if(world == null) {
			h = permission.getWorldsHolder().getDefaultWorld().getPermissionsHandler();
		}else{
			h = permission.getWorldsHolder().getWorldPermissions(world);
		}

		if(h == null) {
			return "";
		}

		return h.getGroupPrefix(g);
	}

	@Override
	public boolean isEnabled() {
		return permission.isEnabled();
	}

	private class PluginListener implements Listener {

		public PluginListener() {
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPluginEnable(PluginEnableEvent event) {
			if(permission == null) {
				Plugin p = event.getPlugin();
				if(p instanceof GroupManager) {
					permission = (GroupManager)p;
					plugin.getLogger().log(Level.INFO, "Successfully hooked into GroupManager");
				}
			}
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPluginDisable(PluginDisableEvent event) {
			if(permission != null) {
				if(event.getPlugin().getDescription().getName().equals("GroupManager")) {
					permission = null;
					plugin.getLogger().log(Level.INFO, "unhooked from GroupManager");
				}
			}
		}
	}
}
