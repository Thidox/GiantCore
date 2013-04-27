package nl.giantit.minecraft.giantcore.Database.drivers;

import nl.giantit.minecraft.giantcore.Database.iDriver;
import nl.giantit.minecraft.giantcore.Database.DatabaseType;
import nl.giantit.minecraft.giantcore.Database.QueryResult;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class hTwo implements iDriver {
	
	private DatabaseType type = DatabaseType.SQLite;
	
	private Plugin plugin;
	
	private ArrayList<HashMap<String, String>> sql = new ArrayList<HashMap<String, String>>();
	
	private String db, user, pass, prefix;
	private Connection con = null;
	private boolean dbg = false;
	
	private boolean parseBool(String s, boolean d) {
		if(s.equalsIgnoreCase("1") || s.equalsIgnoreCase("y") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("true"))
			return true;
		
		return d;
	}

	public hTwo(Plugin p, HashMap<String, String> c) {
		plugin = p;
		
		this.db = c.get("database");
		this.prefix = c.get("prefix");
		this.user = c.get("user");
		this.pass = c.get("password");
		this.dbg = (c.containsKey("debug")) ? this.parseBool(c.get("debug"), false) : false;

		String dbPath = "jdbc:h2:" + plugin.getDataFolder() + java.io.File.separator + this.db;
		try{
			Class.forName("org.h2.Driver");
			this.con = DriverManager.getConnection(dbPath, this.user, this.pass);
		}catch(SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to connect to database: SQL error!");
			if(this.dbg) {
				plugin.getLogger().log(Level.INFO, e.getMessage(), e);
				
			}
		}catch(ClassNotFoundException e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to connect to database: h2 library not found!");
			if(this.dbg) {
				plugin.getLogger().log(Level.INFO, e.getMessage(), e);
				
			}
		}
	}

	@Override
	public void close() {
		try {
			if(con.isClosed())
				return;
			
			this.con.close();
		}catch(SQLException e) {
			//ignore
		}
	}
	
	@Override
	public boolean isConnected() {
		try {
			return con != null && !con.isClosed() && con.isValid(0);
		}catch(SQLException e) {
			return false;
		}
	}

	@Override
	public boolean tableExists(String table) {
		ResultSet res = null;
		table = table.replace("#__", prefix);
		
		try {
			DatabaseMetaData data = this.con.getMetaData();
			res = data.getTables(null, null, table.toUpperCase(), null);

			return res.next();
		}catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, " Could not load table " + table);
			if(this.dbg) {
				plugin.getLogger().log(Level.INFO, e.getMessage(), e);
				
			}
            return false;
		} finally {
			try {
				if(res != null) {
					res.close();
				}
			}catch (Exception e) {
				plugin.getLogger().log(Level.SEVERE, " Could not close result connection to database");
				if(this.dbg) {
					plugin.getLogger().log(Level.INFO, e.getMessage(), e);
					
				}
				return false;
			}
		}
	}

	@Override
	public void buildQuery(String string) {
		this.buildQuery(string, false);
	}

	@Override
	public void buildQuery(String string, boolean add) {
		this.buildQuery(string, add, false);
	}

	@Override
	public void buildQuery(String string, boolean add, boolean finalize) {
		this.buildQuery(string, add, finalize, false);
	}

	@Override
	public void buildQuery(String string, boolean add, boolean finalize, boolean debug) {
		this.buildQuery(string, add, finalize, debug, false);
	}

	@Override
	public void buildQuery(String string, boolean add, boolean finalize, boolean debug, boolean table) {
		if(!add) {
			if(table)
				string = string.replace("#__", prefix);
			
			HashMap<String, String> ad = new HashMap<String, String>();
			ad.put("sql", string);
			
			if(finalize)
				ad.put("finalize", "true");
			
			if(debug)
				ad.put("debug", "true");
			
			sql.add(ad);
		}else{
			int last = sql.size() - 1;
			
			this.buildQuery(string, last, finalize, debug, table);
		}
	}

	@Override
	public void buildQuery(String string, Integer add) {
		this.buildQuery(string, add, false);
	}

	@Override
	public void buildQuery(String string, Integer add, boolean finalize) {
		this.buildQuery(string, add, finalize, false);
	}

	@Override
	public void buildQuery(String string, Integer add, boolean finalize, boolean debug) {
		this.buildQuery(string, add, finalize, debug, false);
	}

	@Override
	public void buildQuery(String string, Integer add, boolean finalize, boolean debug, boolean table) {
		if(table)
			string = string.replace("#__", prefix);
		
		try {
			HashMap<String, String> SQL = sql.get(add);
			if(SQL.containsKey("sql")) {
				if(SQL.containsKey("finalize")) {
					if(true == debug)
						plugin.getLogger().log(Level.SEVERE, " SQL syntax is finalized!");
					return;
				}else{
					SQL.put("sql", SQL.get("sql") + string);
					
					if(true == finalize)
						SQL.put("finalize", "true");

					sql.add(add, SQL);
				}
			}else
				if(true == debug)
					plugin.getLogger().log(Level.SEVERE, add.toString() + " is not a valid SQL query!");
		
			if(debug == true)
				plugin.getLogger().log(Level.INFO, sql.get(add).get("sql"));
		}catch(NullPointerException e) {
			if(true == debug)
				plugin.getLogger().log(Level.SEVERE, "Query " + add.toString() + " could not be found!");
		}
	}

	@Override
	public QueryResult execQuery() {
		Integer queryID = ((sql.size() - 1 > 0) ? (sql.size() - 1) : 0);
		
		return this.execQuery(queryID);
	}
	
	@Override
	public QueryResult execQuery(Integer queryID) {
		Statement st = null;
		
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			HashMap<String, String> SQL = sql.get(queryID);
			if(SQL.containsKey("sql")) {
				try {
					st = con.createStatement();
					//query.add(queryID, st.executeQuery(SQL.get("sql")));
					ResultSet res = st.executeQuery(SQL.get("sql"));
					while(res.next()) {
						HashMap<String, String> row = new HashMap<String, String>();

						ResultSetMetaData rsmd = res.getMetaData();
						int columns = rsmd.getColumnCount();
						for(int i = 1; i < columns + 1; i++) {
							row.put(rsmd.getColumnName(i).toLowerCase(), res.getString(i));
						}
						data.add(row);
					}
				}catch (SQLException e) {
					plugin.getLogger().log(Level.SEVERE, " Could not execute query!");
					if(this.dbg) {
						plugin.getLogger().log(Level.INFO, e.getMessage(), e);
					}
				} finally {
					try {
						if(st != null) {
							st.close();
						}
					}catch (Exception e) {
						plugin.getLogger().log(Level.SEVERE, " Could not close database connection");
						if(this.dbg) {
							plugin.getLogger().log(Level.INFO, e.getMessage(), e);
							
						}
					}
				}
			}
		}catch(NullPointerException e) {
			plugin.getLogger().log(Level.SEVERE, "Query " + queryID.toString() + " could not be found!");
		}
		
		return QueryResult.QR(data);
	}

	@Override
	public void updateQuery() {
		Integer queryID = ((sql.size() - 1 > 0) ? (sql.size() - 1) : 0);
		
		this.updateQuery(queryID);
	}
	
	@Override
	public void updateQuery(Integer queryID) {
		Statement st = null;
		
		try {
			HashMap<String, String> SQL = sql.get(queryID);
			if(SQL.containsKey("sql")) {
				try {
					st = con.createStatement();
					st.executeUpdate(SQL.get("sql"));
				}catch (SQLException e) {
					plugin.getLogger().log(Level.SEVERE, " Could not execute query!");
					if(this.dbg) {
						plugin.getLogger().log(Level.INFO, e.getMessage(), e);
					}
				} finally {
					try {
						if(st != null) {
							st.close();
						}
					}catch (Exception e) {
						plugin.getLogger().log(Level.SEVERE, " Could not close database connection");
						if(this.dbg) {
							plugin.getLogger().log(Level.INFO, e.getMessage(), e);
						}
					}
				}
			}
		}catch(NullPointerException e) {
			plugin.getLogger().log(Level.SEVERE, "Query " + queryID.toString() + " could not be found!");
		}
	}
	
	@Override
	public iDriver select(String field) {
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(field);
		return this.select(fields);
	}
	
	@Override
	public iDriver select(String... fields) {
		ArrayList<String> f = new ArrayList<String>();
		if(fields.length > 0) {
			f.addAll(Arrays.asList(fields));
		}

		return this.select(f);
	}
	
	@Override
	public iDriver select(ArrayList<String> fields) {
		if(fields.size() > 0) {
			String SQL = "SELECT ";
			int i = 0;
			for(String field : fields) {
				if(i > 0)
					SQL += ", ";
				
				SQL += field;
				i++;
			}
			
			this.buildQuery(SQL + " \n", false, false, false);
		}
		
		return this;
	}
	
	@Override
	public iDriver select(HashMap<String, String> fields) {
		if(fields.size() > 0) {
			String SQL = "SELECT ";
			int i = 0;
			for(Map.Entry<String, String> field : fields.entrySet()) {
				if(i > 0)
					SQL += ", ";
				
				SQL += field.getKey() + " AS " + field.getValue();
				i++;
			}
			
			this.buildQuery(SQL + " \n", false, false, false);
		}
		
		return this;
	}
	
	@Override
	public iDriver from(String table) {
		table = table.replace("#__", prefix);
		this.buildQuery("FROM " + table + " \n", true, false, false);
		
		return this;
	}
	
	@Override
	public iDriver where(HashMap<String, String> fields) {
		if(fields.size() > 0) {
			String SQL = "WHERE ";
			int i = 0;
			
			for(Map.Entry<String, String> field : fields.entrySet()) {
				if(i > 0)
					SQL += " AND ";
				
				SQL += field.getKey() + "='" + field.getValue() + "'";
				i++;
			}
			
			this.buildQuery(SQL + " \n", true, false, false);
		}
		
		return this;
	}
	
	@Override
	public iDriver where(HashMap<String, HashMap<String, String>> fields, boolean shite) {
		if(fields.size() > 0) {
			String SQL = "WHERE ";
			int i = 0;
			
			for(Map.Entry<String, HashMap<String, String>> field : fields.entrySet()) {
				String t = (field.getValue().containsKey("type") && field.getValue().get("type").equalsIgnoreCase("OR")) ? "OR" : "AND";
				if(i > 0)
					SQL += " " + t + " ";
				
				if(field.getValue().containsKey("kind") && field.getValue().get("kind").equals("int")) {
					SQL += field.getKey() + "=" + field.getValue().get("data");
				}else if(field.getValue().containsKey("kind") && field.getValue().get("kind").equalsIgnoreCase("NULL")) {
					SQL += field.getKey() + " IS NULL";
				}else if(field.getValue().containsKey("kind") && field.getValue().get("kind").equalsIgnoreCase("NOTNULL")) {
					SQL += field.getKey() + " IS NOT NULL";
				}else if(field.getValue().containsKey("kind") && field.getValue().get("kind").equalsIgnoreCase("NOT")) {
					SQL += field.getKey() + "!='" + field.getValue().get("data")+"'";
				}else
					SQL += field.getKey() + "='" + field.getValue().get("data")+"'";
				
				i++;
			}
			
			this.buildQuery(SQL + " \n", true, false, false);
		}
		
		return this;
	}
	
	@Override
	public iDriver orderBy(HashMap<String, String> fields) {
		if(fields.size() > 0) {
			String SQL = "ORDER BY ";
			int i = 0;
			
			for(Map.Entry<String, String> field : fields.entrySet()) {
				if(i > 0)
					SQL += ", ";
				
				SQL += field.getKey() + " " + ((field.getValue().equalsIgnoreCase("ASC")) ? "ASC" : "DESC");
				i++;
			}
			
			this.buildQuery(SQL + " \n", true, false, false);
		}
		
		return this;
	}
	
	@Override
	public iDriver limit(int limit) {
		return this.limit(limit, null);
	}
	
	@Override
	public iDriver limit(int limit, Integer start) {
		this.buildQuery("LIMIT " + ((start != null) ? start + ", " + limit : limit) + " \n", true, false, false);
		return this;
	}
	
	@Override
	public iDriver insert(String table, ArrayList<String> fields, HashMap<Integer, HashMap<String, String>> values) {
		ArrayList<HashMap<Integer, HashMap<String, String>>> t = new ArrayList<HashMap<Integer, HashMap<String, String>>>();
		t.add(values);
		this.insert(table, fields, t);
		
		return this;
	}
	
	@Override
	public iDriver insert(String table, ArrayList<String> fields, ArrayList<HashMap<Integer, HashMap<String, String>>> values) {
		table = table.replace("#__", prefix);
		this.buildQuery("INSERT INTO " + table + " \n", false, false, false);
		
		if(fields.size() > 0) {
			String insFields = "(";
			int i = 0;
			for(String field : fields) {
				if(i > 0)
					insFields += ", ";
				
				insFields += field;
				i++;
			}
			
			insFields += ") \n";
			this.buildQuery(insFields, true, false, false);
		}
		
		this.buildQuery(" VALUES \n", true, false, false);
		String insValues = "";
		int i = 0;
		for(HashMap<Integer, HashMap<String, String>> value : values) {
			insValues += "(";
			int a = 0;
			for(Map.Entry<Integer, HashMap<String, String>> val : value.entrySet()) {
				if(a > 0)
					insValues += ", ";
				
				a++;
				if(val.getValue().containsKey("kind") && val.getValue().get("kind").equalsIgnoreCase("INT")) {
					insValues += val.getValue().get("data");
				}else
					insValues += "'" + val.getValue().get("data") + "'";
			}
			
			i++;
			insValues += (i < values.size()) ? "), \n" : ");";
		}
		this.buildQuery(insValues, true, false, false);
		
		return this;
	}
	
	@Override
	public iDriver update(String table) {
		table = table.replace("#__", prefix);
		this.buildQuery("UPDATE " + table + " \n", false, false, false);
		
		return this;
	}
	
	@Override
	public iDriver set(HashMap<String, String> fields) {
		if(fields.size() > 0) {
			String SQL = "SET ";
			int i = 0;
			
			for(Map.Entry<String, String> field : fields.entrySet()) {
				if(i > 0)
					SQL += ", ";
				
				SQL += field.getKey() + "='" + field.getValue() + "'";
				i++;
			}
			
			this.buildQuery(SQL + " \n", true, false, false);
		}
		
		return this;
	}
	
	@Override
	public iDriver set(HashMap<String, HashMap<String, String>> fields, boolean shite) {
		if(fields.size() > 0) {
			String SQL = "SET ";
			int i = 0;
			
			for(Map.Entry<String, HashMap<String, String>> field : fields.entrySet()) {
				if(i > 0)
					SQL += ", ";
				
				if(field.getValue().containsKey("kind") && field.getValue().get("kind").equalsIgnoreCase("INT")) {
					SQL += field.getKey() + "=" + field.getValue().get("data");
				}else
					SQL += field.getKey() + "='" + field.getValue().get("data") + "'";
				
				i++;
			}
			
			this.buildQuery(SQL + " \n", true, false, false);
		}
		
		return this;
	}
	
	@Override
	public iDriver delete(String table) {
		table = table.replace("#__", prefix);
		this.buildQuery("DELETE FROM " + table + " \n", false, false, false);
		
		return this;
	}
	
	@Override
	public iDriver Truncate(String table) {
		table = table.replace("#__", prefix);
		this.buildQuery("TRUNCATE TABLE " + table + ";", false, false, false);
		
		return this;
	}

	@Override
	public iDriver create(String table) {
		table = table.replace("#__", prefix);
		this.buildQuery("CREATE TABLE " + table + "\n", false, false, false);
		
		return this;
	}
	
	@Override
	public iDriver fields(HashMap<String, HashMap<String, String>> fields) {
		String P_KEY = "";
		this.buildQuery("(", true, false, false);
		
		int i = 0;
		for(Map.Entry<String, HashMap<String, String>> entry : fields.entrySet()) {
			i++;
			HashMap<String, String> data = entry.getValue();
			
			String field = entry.getKey();
			String t = "VARCHAR";
			Integer length = 100;
			boolean NULL = false;
			String def = "";
			boolean aincr = false;
			
			if(data.containsKey("TYPE")) {
				t = data.get("TYPE");
			}
			
			if(data.containsKey("LENGTH")) {
				if(null != data.get("LENGTH")) {
					try{
						length = Integer.parseInt(data.get("LENGTH"));
						length = length < 0 ? 100 : length;
					}catch(NumberFormatException e) {}
				}else
					length = null;
			}
			
			if(data.containsKey("NULL")) {
				NULL = Boolean.parseBoolean(data.get("NULL"));
			}
			
			if(data.containsKey("DEFAULT")) {
				def = data.get("DEFAULT");
			}
			
			if(data.containsKey("A_INCR")) {
				aincr = Boolean.parseBoolean(data.get("A_INCR"));
			}
			
			if(data.containsKey("P_KEY")) {
				if(Boolean.parseBoolean(data.get("P_KEY"))) {
					P_KEY = field;
				}
			}
			
			if(length != null)
				t += "(" + length + ")";
			
			String n = (!NULL) ? " NOT NULL" : " DEFAULT NULL";
			String d = (!def.equalsIgnoreCase("")) ? " DEFAULT " + def : ""; 
			String a = (aincr) ? " AUTO_INCREMENT" : "";
			String c = (i < fields.size()) ? ",\n" : ""; 
			
			this.buildQuery(field + " " + t + n + d + a + c, true);
		}
		
		if(!P_KEY.equalsIgnoreCase(""))
			this.buildQuery("\n, PRIMARY KEY(" + P_KEY + ")", true, false, false);
		
		this.buildQuery(");", true, false, false);
		
		return this;
	}

	@Override
	public iDriver alter(String table) {
		table = table.replace("#__", prefix);
		this.buildQuery("ALTER TABLE " + table + "\n", false, false, false);
		
		return this;
	}
	
	@Override
	public iDriver add(HashMap<String, HashMap<String, String>> fields) {
		int i = 0;
		for(Map.Entry<String, HashMap<String, String>> entry : fields.entrySet()) {
			i++;
			HashMap<String, String> data = entry.getValue();
			
			String field = entry.getKey();
			String t = "VARCHAR";
			Integer length = 100;
			boolean NULL = false;
			String def = "";
			
			if(data.containsKey("TYPE")) {
				t = data.get("TYPE");
			}
			
			if(data.containsKey("LENGTH")) {
				if(null != data.get("LENGTH")) {
					try{
						length = Integer.parseInt(data.get("LENGTH"));
						length = length < 0 ? 100 : length;
					}catch(NumberFormatException e) {}
				}else
					length = null;
			}
			
			if(data.containsKey("NULL")) {
				NULL = Boolean.parseBoolean(data.get("NULL"));
			}
			
			if(data.containsKey("DEFAULT")) {
				def = data.get("DEFAULT");
			}
			
			if(length != null)
				t += "(" + length + ")";
			
			String n = (!NULL) ? " NOT NULL" : " DEFAULT NULL";
			String d = (!def.equalsIgnoreCase("")) ? " DEFAULT " + def : "";
			String c = (i < fields.size()) ? ",\n" : ""; 
			
			this.buildQuery("ADD " + field + " " + t + n + d + c, true);
		}
		
		return this;
	}
	
	@Override
	public iDriver debug(boolean dbg) {
		this.buildQuery("", true, false, dbg);
		return this;
	}
	
	@Override
	public iDriver Finalize() {
		this.buildQuery("", true, true, false);
		return this;
	}
	
	@Override
	public iDriver debugFinalize(boolean dbg) {
		this.buildQuery("", true, true, dbg);
		return this;
	}
	
	@Override
	public DatabaseType getType() {
		return this.type;
	}

}
