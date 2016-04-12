package Items.Tools.Stone;

import Items.Tools.ITool;
import Items.Tools.ItemPickaxe;
import Items.Utils.ItemStack;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemSilverPickaxe extends ItemPickaxe implements ITool {


	public static Image texture;

	@Override
	public int getMaxItemDamage() {
		return 400;
	}

	@Override
	public Image getTexture( ItemStack stack ) {
		return texture;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		texture =  imageLoader.getImage("items/tools/silver","silverPickaxe");
	}

	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return "Silver Pickaxe";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}

	@Override
	public int getValueOnProperMaterial() {
		return 6;
	}
}
