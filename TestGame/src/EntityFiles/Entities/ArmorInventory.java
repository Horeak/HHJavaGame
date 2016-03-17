package EntityFiles.Entities;

import Items.Utils.IInventory;
import Items.Utils.ItemStack;

public class ArmorInventory implements IInventory{

	public ItemStack[] armorItems = new ItemStack[ getInventorySize()];

	@Override
	public ItemStack[] getItems() {
		return armorItems;
	}

	@Override
	public ItemStack getItem( int i ) {
		return armorItems[i];
	}

	@Override
	public void setItem( int i, ItemStack item ) {
		armorItems[i] = item;
	}

	@Override
	public int getInventorySize() {
		return 4;
	}

	@Override
	public String getInventoryName() {
		return "Armor";
	}
}
