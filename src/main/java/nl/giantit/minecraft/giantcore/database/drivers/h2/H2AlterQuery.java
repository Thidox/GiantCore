package nl.giantit.minecraft.giantcore.database.drivers.h2;

import nl.giantit.minecraft.giantcore.database.Driver;
import nl.giantit.minecraft.giantcore.database.QueryResult;
import nl.giantit.minecraft.giantcore.database.query.AlterQuery;
import nl.giantit.minecraft.giantcore.database.query.Column;
import nl.giantit.minecraft.giantcore.database.query.Group;
import nl.giantit.minecraft.giantcore.database.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Giant
 */
public class H2AlterQuery implements AlterQuery {

	private final Driver db;
	
	private String t;
	
	private boolean ren = false;
	private String renTo;
	
	private final List<Column> cL = new ArrayList<Column>();
	
	private boolean prepared = false;
	private String query;
	
	public H2AlterQuery(Driver db) {
		this.db = db;
	}
	
	@Override
	public AlterQuery setTable(String table) {
		this.t = table;
		
		return this;
	}
	
	@Override
	public AlterQuery rename(String table) {
		this.ren = true;
		this.renTo = table;
		
		return this;
	}
	
	@Override
	public Column addColumn(String column) {
		return this.addColumn(new H2Column().setName(column));
	}
	
	@Override
	public Column addColumn(Column column) {
		this.cL.add(column);
		return column;
	}
	
	@Override
	public Query parse() {
		if(!this.prepared) {
			this.prepared = true;
			
			StringBuilder sB = new StringBuilder();
			sB.append("ALTER TABLE ");
			sB.append(this.t.replace("#__", this.db.getPrefix()));
			sB.append("\n ");
			
			if(this.ren) {
				sB.append(" RENAME TO ");
				sB.append(this.renTo.replace("#__", this.db.getPrefix()));
			}else{
				sB.append(" ADD ");
				int i = 0;
				for(Column c : this.cL) {
					if(!c.isParsed()) {
						c.parse();
					}
					
					if(i > 0) {
						sB.append(", ");
					}else{
						++i;
					}
					
					sB.append(c.getParsedColumn());
				}
				
				sB.append(" ");
			}
			
			sB.append(";");
			
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
