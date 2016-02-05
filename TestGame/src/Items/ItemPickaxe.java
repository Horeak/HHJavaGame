package Items;

import Blocks.BlockStone;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemPickaxe extends Item {


	public static Image texture;

	@Override
	public int getMaxItemDamage() {
		return 200;
	}

	@Override
	public Image getTexture() {
		if(texture == null) texture =  MainFile.game.imageLoader.getImage("items","pickaxe");

		return texture;
	}

	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return "Pickaxe";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}

	//Make it work on ores aswell when implemented
	public int getBlockDamageValue( World world, int x, int y, ItemStack stack ) {
		if(stack.getStackDamage() < getMaxItemDamage()) {
			if(world.getBlock(x, y) instanceof BlockStone){
				return 4;
			}
		}
		return 1;
	}
}
