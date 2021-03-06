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
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsPlugin;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class zpEngine implements Permission {

	private GiantCore plugin;
	private ZPermissionsService permission;
	private Boolean opHasPerms;

	private String quote(String input) {
		input = input.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
		if(input.matches(".*\\s.*")) {
			return "\"" + input + "\""; // Enclose in quotes
		}else{
			return input;
		}
	}

	public zpEngine(GiantCore plugin, Boolean opHasPerms) {
		this.plugin = plugin;
		this.opHasPerms = opHasPerms;

		Plugin perms = plugin.getServer().getPluginManager().getPlugin("zPermissions");
		if(perms != null && perms.isEnabled()) {
			permission = plugin.getServer().getServicesManager().load(ZPermissionsService.class);
			plugin.getLogger().log(Level.INFO, "Successfully hooked into zPermissions");
		}else{
			//It's not enabled yet, let's set up a listener!
			plugin.getServer().getPluginManager().registerEvents(new PluginListener(), plugin);
		}
	}

	@Override
	public boolean has(String p, String perm) {
		Player pl = plugin.getServer().getPlayer(p);
		if(pl == null){
			return false;
		}

		return this.has(pl, perm);
	}

	@Override
	public boolean has(Player p, String perm) {
		return this.has(p.getName(), perm, p.getWorld().getName());
	}

	@Override
	public boolean has(String p, String perm, String world) {
		Player pl = plugin.getServer().getPlayer(p);
		if(pl == null){
			return false;
		}

		if(opHasPerms && pl.isOp()){
			return true;
		}

		Map<String, Boolean> perms = permission.getPlayerPermissions(world, null, p);
		return perms.containsKey(perm) && perms.get(perm);
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
		Map<String, Boolean> perms = permission.getGroupPermissions(world, null, group);
		return perms.containsKey(perm) && perms.get(perm);
	}

	@Override
	public boolean inGroup(String p, String g) {
		Set<String> groups = permission.getPlayerGroups(p);
		for(String group : groups) {
			if(g.equalsIgnoreCase(group)){
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean inGroup(Player p, String group) {
		return this.inGroup(p.getName(), group);
	}

	@Override
	public boolean inGroup(String p, String g, String world) {
		return this.inGroup(p, g);
	}

	@Override
	public boolean inGroup(Player p, String group, String world) {
		return this.inGroup(p.getName(), group);
	}

	@Override
	public void setGroup(String p, String group) {
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "permissions group " + group + " add " + p);
	}

	@Override
	public void setGroup(Player p, String group) {
		this.setGroup(p.getName(), group);
	}

	@Override
	public void setGroup(String p, String group, String world) {
		this.setGroup(p, group);
	}

	@Override
	public void setGroup(Player p, String group, String world) {
		this.setGroup(p.getName(), group, world);
	}

	@Override
	public String getGroup(String p) {
		List<String> groups = permission.getPlayerAssignedGroups(p);
		return (!groups.isEmpty()) ? groups.get(0) : null;
	}

	@Override
	public String getGroup(Player p) {
		return this.getGroup(p.getName());
	}

	@Override
	public String getGroup(String p, String world) {
		return this.getGroup(p);
	}

	@Override
	public String getGroup(Player p, String world) {
		return this.getGroup(p.getName());
	}

	@Override
	public String[] getGroups(String p) {
		return permission.getPlayerGroups(p).toArray(new String[0]);
	}

	@Override
	public String[] getGroups(Player p) {
		return this.getGroups(p.getName());
	}

	@Override
	public String[] getGroups(String p, String world) {
		return this.getGroups(p);
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
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "permissions player " + p + " metadata set prefix " + quote(prefix));
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
		String result = permission.getPlayerMetadata(p, "prefix", String.class);
		return (result == null) ? "" : result;
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
		String result = permission.getGroupMetadata(g, "prefix", String.class);
		return (result == null) ? "" : result;
	}

	@Override
	public boolean isEnabled() {
		return permission != null;
	}

	private class PluginListener implements Listener {

		public PluginListener() {
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPluginEnable(PluginEnableEvent event) {
			if(permission == null) {
				Plugin p = event.getPlugin();
				if(p instanceof ZPermissionsPlugin) {
					permission = plugin.getServer().getServicesManager().load(ZPermissionsService.class);
					plugin.getLogger().log(Level.INFO, "Successfully hooked into zPermissions");
				}
			}
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPluginDisable(PluginDisableEvent event) {
			if(permission != null) {
				if(event.getPlugin().getDescription().getName().equals("zPermissions")) {
					permission = null;
					plugin.getLogger().log(Level.INFO, "unhooked from zPermissions");
				}
			}
		}
	}

}
