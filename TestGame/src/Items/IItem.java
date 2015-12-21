package Items;

import Items.Rendering.IItemRenderer;
import WorldFiles.World;

public interface IItem extends Cloneable {
	int getItemDamage();

	int getItemStackSize();

	int getItemMaxStackSize();

	void decreaseStackSize( int i );

	void increaseStackSize( int i );

	void setStackSize( int i );

	String getItemID();
	String getItemName();

	IItemRenderer getRender();

	//Return TRUE/FALSE if it was sucsessfull
	boolean useItem( World world, int x, int y ) throws IllegalAccessException, InstantiationException;
	void onItemUsed( int hotbarSlot );

	default int getBlockDamageValue( World world, int x, int y ) {
		return 1;
	}

	IItem clone() throws CloneNotSupportedException;
}
