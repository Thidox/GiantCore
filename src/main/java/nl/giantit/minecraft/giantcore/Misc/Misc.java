package nl.giantit.minecraft.giantcore.Misc;

import org.bukkit.Bukkit;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Giant
 */
public class Misc {
	
	private final static HashMap<String, OfflinePlayer> players = new HashMap<String, OfflinePlayer>();

	private static OfflinePlayer getOfflinePlayer(final String name) {
	
		OfflinePlayer found = null;
		int lastLength = Integer.MAX_VALUE;
		for(OfflinePlayer p : Bukkit.getServer().getOfflinePlayers()) {
			if(p.getFirstPlayed() <= 0)
				continue;
			
			if (p.getName().toLowerCase().startsWith(name.toLowerCase())) {
				int length = p.getName().length() - name.length();
				if(length < lastLength) {
					found = p;
					lastLength = length;
				}
				
				if(length == 0)
					break;
			}
		}
		
		if(found != null)
			players.put(name, found);
		
		return found;
	}
	
	public static boolean isEither(String target, String is, String either) {
		if(target.equals(is) || target.equals(either))
			return true;
		
		return false;
	}
	
	public static boolean isEitherIgnoreCase(String target, String is, String either) {
		if(target.equalsIgnoreCase(is) || target.equalsIgnoreCase(either))
			return true;
		
		return false;
	}
	
	public static boolean isAny(String target, String... arr) {
		for(String is : arr) {
			if(target.equals(is))
				return true;
		}
		
		return false;
	}
	
	public static boolean isAnyIgnoreCase(String target, String... arr) {
		for(String is : arr) {
			if(target.equalsIgnoreCase(is))
				return true;
		}
		
		return false;
	}
	
	public static int startsWithAny(String needle, String... haystack) {
		int i = -1;
		for(String straw : haystack) {
			++i;
			
			if(needle.startsWith(straw)) {
				break;
			}
		}
		
		return i;
	}
	
	public static int startsWithAnyIgnoreCase(String needle, String... haystack) {
		int i = -1;
		for(String straw : haystack) {
			++i;
			
			if(needle.toLowerCase().startsWith(straw.toLowerCase())) {
				break;
			}
		}
		
		return i;
	}
	
	public static Boolean contains(List<String> haystack, String needle) {
		for(String hay : haystack) {
			hay = hay.replace("[", "");
			hay = hay.replace("]", "");
			if(hay.toLowerCase().equalsIgnoreCase(needle.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public static String join(String glue, String... input) {
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		for(String string : input) {
			sb.append(string);
			if(i < input.length - 1)
				sb.append(glue);
			
			i++;
		}
		
		return sb.toString();
    }
	
	public static double Round(double r, int precision) {
		if(precision < 1)
			precision = 1;
		
		double p = Math.pow(10,precision);
		
		return Math.round(r * p) / p;
	}
	
	public static float Round(float r, int precision) {
		if(precision < 1)
			precision = 1;
		
		float p = (float)Math.pow(10,precision);
		
		return Math.round(r * p) / p;
	}
	
	/**
	 * 
	 * Attempt to get a player.
	 * 
	 * @param name Partial name of player
	 * @return null if player not found, else OfflinePlayer
	 */
	public static OfflinePlayer getPlayer(String name) {
		if(players.containsKey(name))
			return players.get(name);
		
		return getOfflinePlayer(name);
	}
	
	public static boolean constainsKeyIgnoreCase(Set<String> haystack, String needle) {
		for(String straw : haystack) {
			if(straw.equalsIgnoreCase(needle))
				return true;
		}
		
		return false;
	}
	
	public static Object getIgnoreCase(Map<String, ?> haystack, String needle) {
		for(String straw : haystack.keySet()) {
			if(straw.equalsIgnoreCase(needle))
				return haystack.get(straw);
		}
		
		return null;
	}
}
