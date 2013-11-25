package nl.giantit.minecraft.giantcore.database.drivers.h2;

import nl.giantit.minecraft.giantcore.database.Driver;
import nl.giantit.minecraft.giantcore.database.QueryResult;
import nl.giantit.minecraft.giantcore.database.query.Group;
import nl.giantit.minecraft.giantcore.database.query.Query;
import nl.giantit.minecraft.giantcore.database.query.TruncateQuery;

/**
 *
 * @author Giant
 */
public class H2TruncateQuery implements TruncateQuery {

	private final Driver db;
	
	private String table;
	
	private boolean prepared = false;
	private String query;
	
	public H2TruncateQuery(Driver db) {
		this.db = db;
	}
	
	@Override
	public TruncateQuery setTable(String table) {
		if(!this.isParsed()) {
			this.table = table;
		}
		
		return this;
	}

	@Override
	public Query parse() {
		if(!this.prepared) {
			this.prepared = true;
			
			StringBuilder sB = new StringBuilder();
			sB.append("TRUNCATE TABLE ");
			sB.append(table.replace("#__", this.db.getPrefix()));
			sB.append("\n");
			
			this.query = sB.toString();
		}
		
		return this;
	}
	
	@Override
	public boolean isParsed() {
		return this.prepared;
	}
	
	@Override
	public String getParsedQuery() {
		if(!this.prepared) {
			return "";
		}
		
		return this.query;
	}

	@Override
	public QueryResult exec() {
		return this.exec(false);
	}

	@Override
	public QueryResult exec(boolean debug) {
		if(debug) {
			// Send H2 Query syntax to console for debugging purposes!
			this.db.getPlugin().getLogger().info(this.query);
		}
		
		return this.db.updateQuery(this);
	}
	
	@Override
	public Group createGroup() {
		return new H2Group(this.db);
	}
}
