package nl.giantit.minecraft.giantcore.Database.drivers;

import nl.giantit.minecraft.giantcore.Database.DatabaseType;
import nl.giantit.minecraft.giantcore.Database.QueryResult;
import nl.giantit.minecraft.giantcore.Database.iDriver;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class central implements iDriver {
	
	private DatabaseType type = DatabaseType.Central;
	
	private static HashMap<String, MySQL> instance = new HashMap<String, MySQL>();
	private Plugin plugin;
	
	private String prefix;
	
	private central(Plugin p, HashMap<String, String> c) {
		this.plugin = p;
		
		this.prefix = c.get("prefix");
		
	}

	@Override
	public void close() {
		
	}

	@Override
	public boolean isConnected() {
		return this.plugin != null && plugin.isEnabled();
	}

	@Override
	public boolean tableExists(String table) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void buildQuery(String string) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void buildQuery(String string, boolean add) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void buildQuery(String string, boolean add, boolean finalize) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void buildQuery(String string, boolean add, boolean finalize, boolean debug) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void buildQuery(String string, boolean add, boolean finalize, boolean debug, boolean table) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void buildQuery(String string, Integer add) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void buildQuery(String string, Integer add, boolean finalize) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void buildQuery(String string, Integer add, boolean finalize, boolean debug) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void buildQuery(String string, Integer add, boolean finalize, boolean debug, boolean table) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public QueryResult execQuery() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public QueryResult execQuery(Integer queryID) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void updateQuery() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void updateQuery(Integer queryID) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver select(String field) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver select(String... fields) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver select(ArrayList<String> fields) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver select(HashMap<String, String> fields) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver from(String table) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver where(HashMap<String, String> fields) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver where(HashMap<String, HashMap<String, String>> fields, boolean shite) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver orderBy(HashMap<String, String> fields) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver limit(int limit) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver limit(int limit, Integer start) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver update(String table) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver set(HashMap<String, String> fields) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver set(HashMap<String, HashMap<String, String>> fields, boolean shite) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver insert(String table, ArrayList<String> fields, HashMap<Integer, HashMap<String, String>> values) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver insert(String table, ArrayList<String> fields, ArrayList<HashMap<Integer, HashMap<String, String>>> values) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver delete(String table) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver Truncate(String table) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver create(String table) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver fields(HashMap<String, HashMap<String, String>> fields) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver alter(String table) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver add(HashMap<String, HashMap<String, String>> fields) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver debug(boolean dbg) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver Finalize() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public iDriver debugFinalize(boolean dbg) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public DatabaseType getType() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	
	
}
