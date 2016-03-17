package Items.Tools.Stone;

import BlockFiles.BlockStone;
import Items.Items;
import Items.Tools.ITool;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemIronPickaxe extends Item implements ITool {


	public static Image texture;

	@Override
	public int getMaxItemDamage() {
		return 200;
	}

	@Override
	public Image getTexture( ItemStack stack ) {
		return texture;
	}

	@Override
	public void loadTextures() {
		texture =  MainFile.game.imageLoader.getImage("items/tools/iron","ironPickaxe");
	}

	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return "Iron Pickaxe";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return false;
	}

	//Make it work on ores aswell when implemented
	public int getBlockDamageValue( World world, int x, int y, ItemStack stack ) {
		if(stack.getStackDamage() < getMaxItemDamage()) {
			if(world.getBlock(x, y) instanceof BlockStone){
				return 8;
			}
		}
		return 1;
	}
}
