package Items;

public interface IInventory {

	IItem[] getItems();

	IItem getItem( int i );

	void setItem( int i, IItem item );

	int getInvetorySize();

	String getInventoryName();
}
