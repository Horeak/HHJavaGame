package Items.Utils;

import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import Items.Items;

public class IItemRegistry {
	public static String getID(IItem item){
		if(item instanceof Item) return getIDFromItem((Item)item);
		if(item instanceof Block) return getIDFromBlock((Block)item);

		return null;
	}

	public static String getIDFromBlock(Block bl){
		return Blocks.getId(bl);
	}

	public static String getIDFromItem(Item item){
		return Items.getId(item);
	}

	public static IItem getFromID(String id){
		if(id == null) return Blocks.blockAir;

		if(id.startsWith("item_")){
			return Items.getItem(id);
		}else{
			return Blocks.getBlock(id);
		}
	}
}
