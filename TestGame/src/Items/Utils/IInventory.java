package Items.Utils;

import java.io.Serializable;

public interface IInventory extends Serializable {

	ItemStack[] getItems();
	ItemStack getItem( int i );
	void setItem( int i, ItemStack item );
	int getInvetorySize();
	String getInventoryName();
}
