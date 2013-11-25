package nl.giantit.minecraft.giantcore.database;

import nl.giantit.minecraft.giantcore.database.query.AlterQuery;
import nl.giantit.minecraft.giantcore.database.query.CreateQuery;
import nl.giantit.minecraft.giantcore.database.query.DeleteQuery;
import nl.giantit.minecraft.giantcore.database.query.DropQuery;
import nl.giantit.minecraft.giantcore.database.query.IndexQuery;
import nl.giantit.minecraft.giantcore.database.query.InsertQuery;
import nl.giantit.minecraft.giantcore.database.query.Query;
import nl.giantit.minecraft.giantcore.database.query.SelectQuery;
import nl.giantit.minecraft.giantcore.database.query.TruncateQuery;
import nl.giantit.minecraft.giantcore.database.query.UpdateQuery;

import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Giant
 */
public interface Driver {
	
	public enum Type {
		SELECT,
		UPDATE,
		INSERT,
		DELETE,
		TRUNCATE,
		CREATE,
		INDEX,
		ALTER,
		DROP;
	}
	
	public void close();
	
	public boolean isConnected();
	
	public boolean tableExists(String table);
	
	public QueryResult execQuery(Query q);
	
	public QueryResult updateQuery(Query q);
	
	public SelectQuery select(String f);
	public SelectQuery select(String... fields);
	public SelectQuery select(List<String> fields);
	public SelectQuery select(Map<String, String> fields);
	
	public UpdateQuery update(String table);
	
	public InsertQuery insert(String table);
	
	public DeleteQuery delete(String table);
	
	public TruncateQuery Truncate(String table);
	
	public CreateQuery create(String table);
	
	public IndexQuery createIndex(String index);

	public AlterQuery alter(String table);
	
	public DropQuery drop(String table);
	
	public String getPrefix();
	
	public Query create(Type t);
	
	public DatabaseType getType();
	
	public Plugin getPlugin();
}
