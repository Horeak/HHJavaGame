package Items.Tools.Wood;

import BlockFiles.BlockDirt;
import BlockFiles.BlockGrass;
import Items.Tools.ITool;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemWoodShovel extends Item implements ITool {
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
	public void loadTextures() {
		texture =  MainFile.game.imageLoader.getImage("items/tools/wood","woodShovel");
	}


	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return "Wood Shovel";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}

	//Make it work on ores aswell when implemented
	public int getBlockDamageValue( World world, int x, int y, ItemStack stack ) {
		if(stack.getStackDamage() < getMaxItemDamage()) {
			if(world.getBlock(x, y) instanceof BlockDirt || world.getBlock(x, y) instanceof BlockGrass){
				return 2;
			}
		}
		return 1;
	}

}
