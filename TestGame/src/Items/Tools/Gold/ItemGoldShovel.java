package Items.Tools.Stone;

import Items.Tools.ITool;
import Items.Tools.ItemShovel;
import Items.Utils.ItemStack;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemGoldShovel extends ItemShovel implements ITool {
	public static Image texture;

	@Override
	public int getMaxItemDamage() {
		return 100;
	}

	@Override
	public Image getTexture( ItemStack stack ) {
		return texture;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		texture =  imageLoader.getImage("items/tools/gold","goldShovel");
	}


	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return "Gold Shovel";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}

	@Override
	public int getValueOnProperMaterial() {
		return 10;
	}

}
