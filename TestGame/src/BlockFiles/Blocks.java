package BlockFiles;

import BlockFiles.Util.Block;

import java.util.HashMap;
import java.util.Map;

public class Blocks {
	public static HashMap<Block, String> blockRegistry = new HashMap<>();

	public static <T extends Block> Block addBlock(T bl, String id){
		blockRegistry.put(bl, id);
		return bl;
	}

	public static <T extends Block> Block addBlock(T bl){
		return addBlock(bl, "block_" + bl.getClass().getName());
	}

	public static String getId(Block bl){
		for(Map.Entry<Block, String> ent :blockRegistry.entrySet()){
			if(bl == null || ent.getKey() == null) continue;

			if(bl.getItemName() == ent.getKey().getItemName()){
				return ent.getValue();
			}
		}

		return null;
	}

	public static Block getBlock(String i){
		for(Map.Entry<Block, String> ent : blockRegistry.entrySet()){
			if(ent.getValue().equalsIgnoreCase(i)){
				return ent.getKey();
			}
		}

		return blockAir;
	}


	public static Block blockAir = addBlock(new BlockAir());

	public static Block blockGrass = addBlock(new BlockGrass());
	public static Block blockDirt = addBlock(new BlockDirt());

	public static Block blockStone = addBlock(new BlockStone());
	public static Block blockCrackedStone =  addBlock(new BlockCrackedStone());

	public static Block blockLeaves = addBlock(new BlockLeaves());
	public static Block blockWood = addBlock(new BlockWood());
	public static Block blockPlanks = addBlock(new BlockWoodenPlanks());
	public static Block blockSapling = addBlock(new BlockSapling());

	public static Block blockTorch = addBlock(new BlockTorch());

	public static Block blockFurnace = addBlock(new BlockFurnace());
	public static Block blockChest = addBlock(new BlockChest());

	//TODO Add more ores and redo some of those textures
	//TODO (Copper, Titanium...?)
	public static Block blockCoalOre = addBlock(new BlockCoalOre());
	public static Block blockIronOre = addBlock(new BlockIronOre());
	public static Block blockGoldOre = addBlock(new BlockGoldOre());
	public static Block blockSilverOre = addBlock(new BlockSilverOre());
}
