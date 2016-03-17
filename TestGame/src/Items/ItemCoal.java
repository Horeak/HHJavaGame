package Items;

import BlockFiles.Util.IFuel;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemCoal extends Item implements IFuel{

	public static Image icon;

	@Override
	public int getMaxItemDamage() {
		return -1;
	}

	@Override
	public Image getTexture( ItemStack stack ) {
		return icon;
	}

	@Override
	public void loadTextures() {
		icon = MainFile.game.imageLoader.getImage("items", "coal");
	}

	@Override
	public int getItemMaxStackSize() {
		return 64;
	}

	@Override
	public String getItemName() {
		return "Coal";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}

	@Override
	public int getFuelValue() {
		return 10;
	}
}
