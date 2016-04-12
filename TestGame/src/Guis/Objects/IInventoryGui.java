package Guis.Objects;

import Items.Utils.IInventory;


//This class is made to indicate that this GUI handles an inventory which is not the players
//This is used for things like shift-clicking items
public interface IInventoryGui {
	public IInventory getInvetory();
}
