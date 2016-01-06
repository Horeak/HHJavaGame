package Items.Utils;

public interface IInventory {

	ItemStack[] getItems();

	ItemStack getItem( int i );

	void setItem( int i, ItemStack item );

	int getInvetorySize();

	String getInventoryName();
}
