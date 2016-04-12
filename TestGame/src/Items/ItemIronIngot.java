package Items;

import Items.Utils.Item;
import Items.Utils.ItemStack;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemIronIngot extends Item {

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
	public void loadTextures(TextureLoader imageLoader) {
		icon = imageLoader.getImage("items", "ironIngot");
	}

	@Override
	public int getItemMaxStackSize() {
		return 64;
	}

	@Override
	public String getItemName() {
		return "Iron Ingot";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}
}
