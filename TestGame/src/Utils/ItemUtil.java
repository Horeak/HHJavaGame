package Utils;

import Items.IItem;

public class ItemUtil {
	public static IItem getItem( IItem item, int stackSize ) {
		return getItem(item, stackSize, 0);
	}

	public static IItem getItem( IItem item, int stackSize, int stackDamage ) {
		item.setStackSize(stackSize);
		item.setItemDamage(stackDamage);

		return item;
	}
}
