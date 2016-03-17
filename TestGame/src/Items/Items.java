package Items;

import Items.DebugTools.ItemChunkDestoryer;
import Items.DebugTools.ItemChunkRegenerator;
import Items.DebugTools.ItemChunkReloader;
import Items.Utils.Item;
import Main.MainFile;
import Utils.ConfigValues;

import java.util.HashMap;
import java.util.Map;

public class Items {
	public static HashMap<Item, String> ItemRegistry = new HashMap<>();
	
	public static <T extends Item> Item addItem(T bl, String id){
		ItemRegistry.put(bl, id);
		return bl;
	}
	
	public static <T extends Item> Item addItem(T bl){
		return addItem(bl, "item_" + bl.getClass().getName());
	}

	public static String getId(Item bl){
		for(Map.Entry<Item, String> ent :ItemRegistry.entrySet()){
			if(bl.getItemName() == ent.getKey().getItemName()){
				return ent.getValue();
			}
		}
		
		return null;
	}
	
	public static Item getItem(String i){
		for(Map.Entry<Item, String> ent : ItemRegistry.entrySet()){
			if(ent.getValue().equalsIgnoreCase(i)){
				return ent.getKey();
			}
		}
		
		return null;
	}
	

	public static Item itemAxe = addItem(new ItemAxe());
	public static Item itemPickaxe = addItem(new ItemPickaxe());
	public static Item itemShovel = addItem(new ItemShovel());

	public static Item itemStick = addItem(new ItemStick());

	public static Item itemIronIngot = addItem(new ItemIronIngot());


	public static Item debugChunkDestoryer = addItem(new ItemChunkDestoryer());
	public static Item debugChunkReloader = addItem(new ItemChunkReloader());
	public static Item debugChunkRegenerator = addItem(new ItemChunkRegenerator());
	
}
