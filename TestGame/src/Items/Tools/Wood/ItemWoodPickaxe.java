package Items.Tools.Wood;

import Items.Tools.ITool;
import Items.Tools.ItemPickaxe;
import Items.Utils.ItemStack;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemWoodPickaxe extends ItemPickaxe implements ITool {


	public static Image texture;

	@Override
	public int getMaxItemDamage() {
		return 50;
	}

	@Override
	public Image getTexture( ItemStack stack ) {
		return texture;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		texture =  imageLoader.getImage("items/tools/wood","woodPickaxe");
	}

	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return "Wood Pickaxe";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}

	@Override
	public int getValueOnProperMaterial() {
		return 2;
	}
}
