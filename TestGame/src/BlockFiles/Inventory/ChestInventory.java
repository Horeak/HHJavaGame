package BlockFiles.Inventory;

import Items.Utils.IInventory;
import Items.Utils.ItemStack;

public class ChestInventory implements IInventory {

	public ItemStack[] items = new ItemStack[ getInventorySize()];

	@Override
	public ItemStack[] getItems() {
		return items;
	}

	@Override
	public ItemStack getItem( int i ) {
		return items[i];
	}

	@Override
	public void setItem( int i, ItemStack item ) {
		items[i] = item;
	}

	@Override
	public int getInventorySize() {
		return 20;
	}

	@Override
	public String getInventoryName() {
		return "Chest";
	}
}
