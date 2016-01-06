package Items.Utils;

import Items.Rendering.IItemRenderer;
import Render.EnumRenderMode;
import Utils.ConfigValues;
import WorldFiles.World;

import java.util.ArrayList;

public interface IItem extends Cloneable {
	int getItemMaxStackSize();
	String getItemName();

	default EnumRenderMode getRenderMode(){return ConfigValues.renderMod;}
	IItemRenderer getRender();

	default ArrayList<String> getTooltips(ItemStack stack){return null;}

	boolean useItem( World world, int x, int y, ItemStack stack );
	default int getBlockDamageValue( World world, int x, int y, ItemStack stack ) {
		return 1;
	}


	default boolean equals( IItem item ) {
		return item != null && item.getItemName().equals(getItemName());
	}
	IItem clone() throws CloneNotSupportedException;

}
