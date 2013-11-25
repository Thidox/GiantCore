package nl.giantit.minecraft.giantcore.database.query;

/**
 *
 * @author Giant
 */
public interface CreateQuery extends Query {

	public CreateQuery create(String table);
	
	public Column addColumn(String column);
	public Column addColumn(Column column);
	
	public ForeignKey addForeignKey(String name);
	public ForeignKey addForeignKey(ForeignKey fk);
	
	/*public IndexQuery addIndex(String index);
	public IndexQuery addIndex(IndexQuery index);*/
	
}
