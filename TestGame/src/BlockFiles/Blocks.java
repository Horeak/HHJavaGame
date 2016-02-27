package BlockFiles;

import BlockFiles.Util.Block;

import java.util.HashMap;
import java.util.Map;

public class Blocks {
	public static HashMap<Block, Integer> blockRegistry = new HashMap<>();

	public static <T extends Block> Block addBlock(T bl, int id){
		blockRegistry.put(bl, id);
		return bl;
	}

	public static <T extends Block> Block addBlock(T bl){
		return addBlock(bl, blockRegistry.size() + 1);
	}

	public static int getId(Block bl){
		for(Map.Entry<Block, Integer> ent :blockRegistry.entrySet()){
			if(bl.getItemName() == ent.getKey().getItemName()){
				return ent.getValue();
			}
		}

		return -1;
	}

	public static Block getBlock(int i){
		for(Map.Entry<Block, Integer> ent : blockRegistry.entrySet()){
			if(i == ent.getValue()){
				return ent.getKey();
			}
		}

		return null;
	}


	public static Block blockAir = addBlock(new BlockAir(), -1);

	public static Block blockGrass = addBlock(new BlockGrass());
	public static Block blockDirt = addBlock(new BlockDirt());

	public static Block blockStone = addBlock(new BlockStone());
	public static Block blockCrackedStone =  addBlock(new BlockCrackedStone());

	public static Block blockLeaves = addBlock(new BlockLeaves());
	public static Block blockWood = addBlock(new BlockWood());
	public static Block blockPlanks = addBlock(new BlockWoodenPlanks());
	public static Block blockSapling = addBlock(new BlockSapling());

	public static Block blockTorch = addBlock(new BlockTorch());
}
