package nl.giantit.minecraft.giantcore.core.Eco.Engines;

import nl.giantit.minecraft.giantcore.GiantCore;
import nl.giantit.minecraft.giantcore.core.Eco.iEco;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

import com.iConomy.iConomy;

/**
 *
 * @author Giant
 */
public class ic5_Engine implements iEco {
	
	private GiantCore plugin;
	private iConomy eco;
	
	public ic5_Engine(GiantCore plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(new EcoListener(this), plugin);
		if(eco == null) {
			Plugin ecoEn = plugin.getServer().getPluginManager().getPlugin("iConomy");

			if(ecoEn != null && ecoEn.isEnabled() && ecoEn.getClass().getName().equals("com.iConomy.iConomy")) {
				eco = (iConomy) ecoEn;
				plugin.getLogger().log(Level.INFO, "Succesfully hooked into iConomy 5!");
			}
		}
	}
	
	@Override
	public boolean isLoaded() {
		return eco != null;
	}
	
	@Override
	public double getBalance(Player player) {
		return this.getBalance(player.getName());
	}
	
	@Override
	public double getBalance(String player) {
		return eco.getAccount(player).getHoldings().balance();
	}
	
	@Override
	public boolean withdraw(Player player, double amount) {
		return this.withdraw(player.getName(), amount);
	}
	
	@Override
	public boolean withdraw(String player, double amount) {
		if(amount > 0) {
			double balance = eco.getAccount(player).getHoldings().balance();
			if((balance - amount) >= 0) {
				eco.getAccount(player).getHoldings().subtract(amount);
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean deposit(Player player, double amount) {
		return this.deposit(player.getName(), amount);
	}
	
	@Override
	public boolean deposit(String player, double amount) {
		if(amount > 0) {
			eco.getAccount(player).getHoldings().add(amount);
			return true;
		}
		return false;
	}
	
	public class EcoListener implements Listener {
		private ic5_Engine eco;
		
		public EcoListener(ic5_Engine eco) {
			this.eco = eco;
		}
		
		@EventHandler()
		public void onPluginEnable(PluginEnableEvent event) {
			if(eco.eco == null) {
				Plugin ecoEn = plugin.getServer().getPluginManager().getPlugin("iConomy");
				
				if(ecoEn != null && ecoEn.isEnabled() && ecoEn.getClass().getName().equals("com.iConomy.iConomy")) {
					eco.eco = (iConomy) ecoEn;
					plugin.getLogger().log(Level.INFO, "Succesfully hooked into iConomy 5!");
				}
			}
		}
		
		@EventHandler()
		public void onPluginDisable(PluginDisableEvent event) {
			if(eco.eco != null) {
				if(event.getPlugin().getDescription().getName().equals("iConomy")) {
					eco.eco = null;
					plugin.getLogger().log(Level.INFO, "Succesfully unhooked into iConomy 5!");
				}
			}
		}
	}
}
