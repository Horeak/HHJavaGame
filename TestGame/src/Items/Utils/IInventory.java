package Items.Utils;

import java.io.Serializable;

public interface IInventory extends Serializable {

	ItemStack[] getItems();
	ItemStack getItem( int i );
	void setItem( int i, ItemStack item );
	int getInventorySize();
	String getInventoryName();

	boolean validItemForSlot(ItemStack stack, int slot);
	boolean canConsumeFromSlot(int slot);

	//Used to see if an item can be removed from the slot without player interaction (ItemMover)
	default boolean canOutputfromSlot(int slot){return true;}

	default int getMaxStackSize(ItemStack stack)
	{
		return stack.getMaxStackSize();
	}

	default boolean consumeItem( ItemStack item ) {
		for (int i = 0; i < getInventorySize(); i++) {
			if(!canConsumeFromSlot(i)) continue;
			if(consumeFromSlot(item, i) <= 0) return true;
		}

		return false;
	}

	//item can be set as null to ignore which item is in the slot but decrease anyway
	default int consumeFromSlot(ItemStack item, int i){
		if(!canConsumeFromSlot(i)) return item != null ? item.getStackSize() : 0;

		ItemStack it = getItem(i);
		if (it != null && item != null) {
			if (it.equals(item) && it.getStackSize() >= item.getStackSize()) {
				getItem(i).decreaseStackSize(item.getStackSize());

				if (getItem(i).getStackSize() <= 0) {
					setItem(i, null);
				}

				return 0;

			} else if (it.equals(item) && it.getStackSize() < item.getStackSize()) {
				int g = item.getStackSize() - it.getStackSize();
				setItem(i, null);

				return g;
			}
		}else if(item == null && it != null){
			it.decreaseStackSize(1);

			if(it.getStackSize() <= 0){
				setItem(i, null);
			}
		}

		return item == null ? 0 : item.getStackSize();
	}

	default int addItemToSlot(ItemStack item, int slot){
		if (getItem(slot) == null) {
			if (item.getStackSize() > getMaxStackSize(item)) {
				int temp = item.getStackSize() - getMaxStackSize(item);

				item.setStackSize(getMaxStackSize(item));
				setItem(slot, item);

				item.setStackSize(temp);
			} else {
				setItem(slot, item);
				return 0;
			}

		} else if (getItem(slot) != null && getItem(slot).equals(item)) {
			if (getItem(slot).getStackSize() < getMaxStackSize(getItem(slot))) {
				int t = getMaxStackSize(getItem(slot)) - getItem(slot).getStackSize();

				if (item.getStackSize() <= t) {
					getItem(slot).setStackSize(getItem(slot).getStackSize() + item.getStackSize());
					item.decreaseStackSize(t);
				} else {
					item.decreaseStackSize(t);
					getItem(slot).setStackSize(getMaxStackSize(getItem(slot)));
				}
			}
		}

		return item == null ? 0 : item.getStackSize();
	}

	default boolean addItem( ItemStack item ) {
		if(item == null)
			return false;

		for(int i = 0; i < getInventorySize(); i++) {
			if(!validItemForSlot(item, i)) continue;
			if(addItemToSlot(item, i) <= 0) return true;
		}

		return false;
	}

}
