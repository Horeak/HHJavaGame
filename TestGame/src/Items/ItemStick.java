package Items;

import BlockFiles.Util.IFuel;
import Items.Utils.IArmor;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemStick extends Item implements IArmor, IFuel{

	public static Image texture;

	@Override
	public int getMaxItemDamage() {
		return -1;
	}

	@Override
	public Image getTexture() {
		return texture;
	}

	@Override
	public void loadTextures() {
		texture =  MainFile.game.imageLoader.getImage("items","woodstick");
	}

	@Override
	public int getItemMaxStackSize() {
		return 64;
	}

	@Override
	public String getItemName() {
		return "Wood stick";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}

	@Override
	public int getFuelValue() {
		return 1;
	}
}
