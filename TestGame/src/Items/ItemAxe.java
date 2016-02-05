package Items;

import Blocks.BlockWood;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemAxe extends Item {

	public static Image texture;

	@Override
	public int getMaxItemDamage() {
		return 200;
	}

	@Override
	public Image getTexture() {
		if(texture == null)  texture =  MainFile.game.imageLoader.getImage("items","axe");

		return texture;
	}

	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return "Axe";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}

	public int getBlockDamageValue( World world, int x, int y, ItemStack stack ) {
		if(stack.getStackDamage() < getMaxItemDamage()) {
			if(world.getBlock(x, y) instanceof BlockWood){
				return 4;
			}
		}
		return 1;
	}
}
