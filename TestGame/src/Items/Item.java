package Items;

import Items.Rendering.ItemRenderer;
import WorldFiles.World;

public interface Item {
	int getItemDamage();

	int getItemStackSize();

	int getItemMaxStackSize();

	void decreaseStackSize( int i );

	void increaseStackSize( int i );

	void setStackSize( int i );

	String getItemID();

	String getItemName();

	ItemRenderer getRender();

	//Return TRUE/FALSE if it was sucsessfull
	boolean useItem( World world, int x, int y ) throws IllegalAccessException, InstantiationException;

	void onItemUsed( int hotbarSlot );
}
