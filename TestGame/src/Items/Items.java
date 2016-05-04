package Items;

import BlockFiles.Util.Material;
import Items.DebugTools.ItemChunkDestoryer;
import Items.DebugTools.ItemChunkRegenerator;
import Items.DebugTools.ItemChunkReloader;
import Items.DebugTools.ItemStructureDestroyer;
import Items.Tools.ITool;
import Items.Tools.ToolMaterial;
import Items.Utils.Item;
import Utils.LoggerUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Items {
	public static HashMap<Item, String> ItemRegistry = new HashMap<>();
	
	public static Item addItem(Item bl, String id){
		bl.registryValue = ItemRegistry.size() + 1;
		ItemRegistry.put(bl, id);

		LoggerUtil.out.log(Level.INFO, "Item registried: " + bl.getClass().getName() + ", id=" + id + ", registryNum=" + bl.registryValue);

		return bl;
	}
	
	public static Item addItem(Item bl){
		return addItem(bl, "item_" + bl.getClass().getName() + ":" + bl.getItemName().replace(" ", "_"));
	}

	public static String getId(Item bl){
		for(Map.Entry<Item, String> ent : ItemRegistry.entrySet()){
			if(bl.getItemName().equalsIgnoreCase(ent.getKey().getItemName())){
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

	/**
	 *
	 * @param c the class of which the item is to be registered from
	 * @param name the name of the tool type
	 * @param textureLoc the location of the tool texture
	 * @param textureName the name of the tool texture
	 * @param material tool material
	 * @param effectiveOn which materials this tool is effective on
	 * @return
	 */
	public static Item addToolItem(Class<? extends ITool> c, String name, String textureLoc, String textureName, ToolMaterial material, Material[] effectiveOn){
		ITool tool = null;

		try {

			tool = c.newInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		tool.name = name;
		tool.textureLoc = textureLoc;
		tool.textureName = textureName;
		tool.material = material;
		tool.effectiveMaterials = effectiveOn;

		return addItem(tool);
	}


	//Wood
	public static Item itemWoodAxe         = addToolItem(ITool.class, "Axe", "items/tools/wood", "woodAxe", ToolMaterial.WOOD, new Material[]{Material.WOOD});
	public static Item itemWoodPickaxe     = addToolItem(ITool.class, "Pickaxe", "items/tools/wood", "woodPickaxe", ToolMaterial.WOOD, new Material[]{Material.ROCK, Material.ORE});
	public static Item itemWoodShovel      = addToolItem(ITool.class, "Shovel", "items/tools/wood", "woodShovel", ToolMaterial.WOOD, new Material[]{Material.DIRT, Material.SAND, Material.SAND});

	//Stone
	public static Item itemStoneAxe        = addToolItem(ITool.class, "Axe", "items/tools/stone", "stoneAxe", ToolMaterial.STONE, new Material[]{Material.WOOD});
	public static Item itemStonePickaxe    = addToolItem(ITool.class, "Pickaxe", "items/tools/stone", "stonePickaxe", ToolMaterial.STONE, new Material[]{Material.ROCK, Material.ORE});
	public static Item itemStoneShovel     = addToolItem(ITool.class, "Shovel", "items/tools/stone", "stoneShovel", ToolMaterial.STONE, new Material[]{Material.DIRT, Material.SAND, Material.SAND});

	//Iron
	public static Item itemIronAxe         = addToolItem(ITool.class, "Axe", "items/tools/iron", "ironAxe", ToolMaterial.IRON, new Material[]{Material.WOOD});
	public static Item itemIronPickaxe     = addToolItem(ITool.class, "Pickaxe", "items/tools/iron", "ironPickaxe", ToolMaterial.IRON, new Material[]{Material.ROCK, Material.ORE});
	public static Item itemIronShovel      = addToolItem(ITool.class, "Shovel", "items/tools/iron", "ironShovel", ToolMaterial.IRON, new Material[]{Material.DIRT, Material.SAND, Material.SAND});

	//Gold
	public static Item itemGoldAxe         = addToolItem(ITool.class, "Axe", "items/tools/gold", "goldAxe", ToolMaterial.GOLD, new Material[]{Material.WOOD});
	public static Item itemGoldPickaxe     = addToolItem(ITool.class, "Pickaxe", "items/tools/gold", "goldPickaxe", ToolMaterial.GOLD, new Material[]{Material.ROCK, Material.ORE});
	public static Item itemGoldShovel      = addToolItem(ITool.class, "Shovel", "items/tools/gold", "goldShovel", ToolMaterial.GOLD, new Material[]{Material.DIRT, Material.SAND, Material.SAND});

	//Silver
	public static Item itemSilverAxe       = addToolItem(ITool.class, "Axe", "items/tools/silver", "silverAxe", ToolMaterial.SILVER, new Material[]{Material.WOOD});
	public static Item itemSilverPickaxe   = addToolItem(ITool.class, "Pickaxe", "items/tools/silver", "silverPickaxe", ToolMaterial.SILVER, new Material[]{Material.ROCK, Material.ORE});
	public static Item itemSilverShovel    = addToolItem(ITool.class, "Shovel", "items/tools/silver", "silverShovel", ToolMaterial.SILVER, new Material[]{Material.DIRT, Material.SAND, Material.SAND});


	public static Item itemStick = addItem(new ItemStick());

	public static Item itemCoal = addItem(new ItemCoal());
	public static Item itemIronIngot = addItem(new ItemIronIngot());
	public static Item itemSilverIngot = addItem(new ItemSilverIngot());
	public static Item itemGoldIngot = addItem(new ItemGoldIngot());


	//TODO Make it where these are only registered if game is in debug launch mode
	public static Item debugChunkDestoryer = addItem(new ItemChunkDestoryer());
	public static Item debugChunkReloader = addItem(new ItemChunkReloader());
	public static Item debugChunkRegenerator = addItem(new ItemChunkRegenerator());
	public static Item debugStructureDestroyer = addItem(new ItemStructureDestroyer());
	
}
