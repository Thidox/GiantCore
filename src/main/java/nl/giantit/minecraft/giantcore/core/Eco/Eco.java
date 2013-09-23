package nl.giantit.minecraft.giantcore.core.Eco;

import nl.giantit.minecraft.giantcore.GiantCore;

import nl.giantit.minecraft.giantcore.core.Eco.Engines.AEco_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.bose6_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.bose7_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.Craftconomy_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.CurrencyCore_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.Essentials_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.ic4_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.ic5_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.ic6_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.McMoney_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.MineConomy_Engine;
import nl.giantit.minecraft.giantcore.core.Eco.Engines.MultiCurrency_Engine;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

/**
 *
 * @author Giant
 */
public class Eco {
	
	private iEco Engine = null;
	private GiantCore plugin;
	private Engines engine;
	
	public enum Engines {
		AECO("AECO", AEco_Engine.class),
		CRAFTCONOMY("Craftconomy", CurrencyCore_Engine.class),
		CURRENCYCORE("CurrencyCore", Craftconomy_Engine.class),
		ESSENTIALS("Essentials Economy", Essentials_Engine.class),
		MCMONEY("McMoney", McMoney_Engine.class),
		MINECONOMY("MineConomy", MineConomy_Engine.class),
		MULTICURRENCY("MultiCurrency", MultiCurrency_Engine.class),
		BOSE6("BOSEconomy 6", bose6_Engine.class),
		BOSE7("BOSEconomy 7", bose7_Engine.class),
		ICONOMY4("iConomy 4", ic4_Engine.class),
		ICONOMY5("iConomy 5", ic5_Engine.class),
		ICONOMY6("iConomy 6", ic6_Engine.class);
		
		private final String value;
		private final Class gameClass;
		
		private Engines(String s, Class gameClass) {
			this.value = s;
			this.gameClass = gameClass;
		}
		
		public Class getGameClass() {
			return this.gameClass;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
	
	private boolean packageExists(String...Packages) {
		try{
			for(String pckg : Packages) {
				Class.forName(pckg);
			}
			return true;
		}catch(ClassNotFoundException e) {
			return false;
		}
	}
	
	public static Engines findEngine(String engine) {
		if(engine.equalsIgnoreCase("AECO")) {
			return Engines.AECO;
		}else if(engine.equalsIgnoreCase("CRAFTCONOMY")) {
			return Engines.CRAFTCONOMY;
		}else if(engine.equalsIgnoreCase("CURRENCYCORE")) {
			return Engines.CURRENCYCORE;
		}else if(engine.equalsIgnoreCase("ESSENTIALS")) {
			return Engines.ESSENTIALS;
		}else if(engine.equalsIgnoreCase("MCMONEY")) {
			return Engines.MCMONEY;
		}else if(engine.equalsIgnoreCase("MINECONOMY")) {
			return Engines.MINECONOMY;
		}else if(engine.equalsIgnoreCase("MULTICURRENCY")) {
			return Engines.MULTICURRENCY;
		}else if(engine.equalsIgnoreCase("BOSE6")) {
			return Engines.BOSE6;
		}else if(engine.equalsIgnoreCase("BOSE7")) {
			return Engines.BOSE7;
		}else if(engine.equalsIgnoreCase("ICONOMY4")) {
			return Engines.ICONOMY4;
		}else if(engine.equalsIgnoreCase("ICONOMY5")) {
			return Engines.ICONOMY5;
		}else if(engine.equalsIgnoreCase("ICONOMY6")) {
			return Engines.ICONOMY6;
		}
		
		return null;
	}
	
	public Eco(GiantCore plugin, Engines engine) {
		this.plugin = plugin;
		try {
			this.Engine = (iEco)engine.getGameClass().getConstructor(iEco.class).newInstance(this);
		}catch(NoSuchMethodException ex) {
			plugin.getLogger().log(Level.SEVERE, null, ex);
		}catch(InstantiationException ex) {
			plugin.getLogger().log(Level.SEVERE, null, ex);
		}catch(IllegalAccessException ex) {
			plugin.getLogger().log(Level.SEVERE, null, ex);
		}catch(IllegalArgumentException ex) {
			plugin.getLogger().log(Level.SEVERE, null, ex);
		}catch(InvocationTargetException ex) {
			plugin.getLogger().log(Level.SEVERE, null, ex);
		}
	}
	
	public boolean isLoaded() {
		return (this.Engine != null && this.Engine.isLoaded());
	}
	
	public iEco getEngine() {
		return this.Engine;
	}
	
	public String getEngineName() {
		return this.engine.toString();
	}
}
