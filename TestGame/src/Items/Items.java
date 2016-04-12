package Items;

import Items.DebugTools.ItemChunkDestoryer;
import Items.DebugTools.ItemChunkRegenerator;
import Items.DebugTools.ItemChunkReloader;
import Items.Tools.Stone.*;
import Items.Tools.Wood.ItemWoodAxe;
import Items.Tools.Wood.ItemWoodPickaxe;
import Items.Tools.Wood.ItemWoodShovel;
import Items.Utils.Item;
import Utils.LoggerUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Items {
	public static HashMap<Item, String> ItemRegistry = new HashMap<>();
	
	public static <T extends Item> Item addItem(T bl, String id){
		bl.registryValue = ItemRegistry.size() + 1;
		ItemRegistry.put(bl, id);

		LoggerUtil.out.log(Level.INFO, "Item registried: " + bl + ", id=" + id + ", registryNum=" + bl.registryValue);

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
	

	//Wood
	public static Item itemWoodAxe = addItem(new ItemWoodAxe());
	public static Item itemWoodPickaxe = addItem(new ItemWoodPickaxe());
	public static Item itemWoodShovel = addItem(new ItemWoodShovel());

	//Stone
	public static Item itemStoneAxe = addItem(new ItemStoneAxe());
	public static Item itemStonePickaxe = addItem(new ItemStonePickaxe());
	public static Item itemStoneShovel = addItem(new ItemStoneShovel());

	//Iron
	public static Item itemIronAxe = addItem(new ItemIronAxe());
	public static Item itemIronPickaxe = addItem(new ItemIronPickaxe());
	public static Item itemIronShovel = addItem(new ItemIronShovel());

	//Gold
	public static Item itemGoldAxe = addItem(new ItemGoldAxe());
	public static Item itemGoldPickaxe = addItem(new ItemGoldPickaxe());
	public static Item itemGoldShovel = addItem(new ItemGoldShovel());

	//Silver
	public static Item itemSilverAxe = addItem(new ItemSilverAxe());
	public static Item itemSilverPickaxe = addItem(new ItemSilverPickaxe());
	public static Item itemSilverShovel = addItem(new ItemSilverShovel());


	public static Item itemStick = addItem(new ItemStick());

	public static Item itemCoal = addItem(new ItemCoal());
	public static Item itemIronIngot = addItem(new ItemIronIngot());
	public static Item itemSilverIngot = addItem(new ItemSilverIngot());
	public static Item itemGoldIngot = addItem(new ItemGoldIngot());


	public static Item debugChunkDestoryer = addItem(new ItemChunkDestoryer());
	public static Item debugChunkReloader = addItem(new ItemChunkReloader());
	public static Item debugChunkRegenerator = addItem(new ItemChunkRegenerator());
	
}
