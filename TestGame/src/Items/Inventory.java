package Items;

public interface Inventory {

	Item[] getItems();

	Item getItem( int i );

	void setItem( int i, Item item );

	int getInvetorySize();

	String getInventoryName();
}
