package Items.Tools.Stone;

import BlockFiles.BlockWood;
import Items.Items;
import Items.Tools.ITool;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemGoldAxe extends Item implements ITool{

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
	public void loadTextures() {
		texture =  MainFile.game.imageLoader.getImage("items/tools/gold","goldAxe");
	}

	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return "Gold Axe";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}

	public int getBlockDamageValue( World world, int x, int y, ItemStack stack ) {
		if(stack.getStackDamage() < getMaxItemDamage()) {
			if(world.getBlock(x, y) instanceof BlockWood){
				return 10;
			}
		}
		return 1;
	}
}
