package Items;

import Items.Utils.Item;

import java.util.HashMap;
import java.util.Map;

public class Items {
	public static HashMap<Item, Integer> ItemRegistry = new HashMap<>();
	
	public static <T extends Item> Item addItem(T bl, int id){
		ItemRegistry.put(bl, id);
		return bl;
	}
	
	public static <T extends Item> Item addItem(T bl){
		return addItem(bl, ItemRegistry.size() + 1);
	}
	
	public static int getId(Item bl){
		for(Map.Entry<Item, Integer> ent :ItemRegistry.entrySet()){
			if(bl.getItemName() == ent.getKey().getItemName()){
				return ent.getValue();
			}
		}
		
		return -1;
	}
	
	public static Item getItem(int i){
		for(Map.Entry<Item, Integer> ent : ItemRegistry.entrySet()){
			if(i == ent.getValue()){
				return ent.getKey();
			}
		}
		
		return null;
	}
	

	public static Item itemAxe = addItem(new ItemAxe());
	public static Item itemPickaxe = addItem(new ItemPickaxe());
	public static Item itemShovel = addItem(new ItemShovel());

	public static Item itemStick = addItem(new ItemStick());
	
}
