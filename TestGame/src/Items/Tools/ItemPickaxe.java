package Items.Tools;

import BlockFiles.Util.Material;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import WorldFiles.World;

public abstract class ItemPickaxe extends Item implements ITool{

	public abstract int getValueOnProperMaterial();

	//TODO Need to add material system!
	public int getBlockDamageValue( World world, int x, int y, ItemStack stack ) {
		if(stack.getStackDamage() < getMaxItemDamage()) {
			if(world.getBlock(x, y).getBlockMaterial() == Material.ROCK){
				return getValueOnProperMaterial();
			}
		}
		return 1;
	}
}
