package WorldGeneration.Util;

import BlockFiles.Blocks;
import Items.Items;
import Items.Utils.ItemStack;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Structures.Structure;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DungeonLootGenerator {
	public static ConcurrentHashMap<ItemStack, Integer> lootTable = new ConcurrentHashMap<>();

	//TODO Add proper loot!
	//TODO When adding stats make items with stats spawn in dungeons (like pickaxe with speed, armor with health increase, armor with health regeneration increase...)
	public static void addLootTable(){
		lootTable.put(new ItemStack(Items.itemIronIngot, 2), 1);
		lootTable.put(new ItemStack(Items.itemIronIngot, 4), 1);
		lootTable.put(new ItemStack(Items.itemIronIngot, 8), 2);
		lootTable.put(new ItemStack(Items.itemIronIngot, 16), 3);
		lootTable.put(new ItemStack(Items.itemIronIngot, 32), 4);

		lootTable.put(new ItemStack(Items.itemGoldIngot, 3), 2);
		lootTable.put(new ItemStack(Items.itemGoldIngot, 6), 3);
		lootTable.put(new ItemStack(Items.itemGoldIngot, 12), 4);

		lootTable.put(new ItemStack(Items.itemSilverIngot, 5), 2);
		lootTable.put(new ItemStack(Items.itemSilverIngot, 10), 4);
		lootTable.put(new ItemStack(Items.itemSilverIngot, 16), 5);


		lootTable.put(new ItemStack(Items.itemCoal, 5), 1);
		lootTable.put(new ItemStack(Items.itemCoal, 8), 1);
		lootTable.put(new ItemStack(Items.itemCoal, 16), 2);
		lootTable.put(new ItemStack(Items.itemCoal, 32), 3);
		lootTable.put(new ItemStack(Items.itemCoal, 64), 4);

		lootTable.put(new ItemStack(Blocks.blockTorch, 16), 1);

		lootTable.put(new ItemStack(Items.itemIronPickaxe), 2);
		lootTable.put(new ItemStack(Items.itemGoldPickaxe), 2);
		lootTable.put(new ItemStack(Items.itemSilverPickaxe), 2);
	}

	public static ItemStack getRandomLoot(int rar)
	{
		ArrayList<ItemStack> stacks = new ArrayList<>();

		for(Map.Entry<ItemStack, Integer> ent : lootTable.entrySet()){
			if(rar >= ent.getValue()){
				stacks.add(ent.getKey());
			}
		}
		ItemStack stack = stacks.get(MainFile.random.nextInt(stacks.size()));

		return new ItemStack(stack.getItem(), stack.getStackSize(), stack.getStackDamage());
	}

	public static void generateDungeonChest(World world, int x, int y, int rare){
		world.setBlock(Blocks.blockChest, x, y);

		if(world.getInventory(x, y) != null) {
			int t = MainFile.random.nextInt(world.getInventory(x, y).getInventorySize());

			for (int i = 0; i < t; i++) {
				if(MainFile.random.nextInt(4) != 0){
					world.getInventory(x, y).setItem(i, getRandomLoot(rare));
				}

			}
		}
	}

	public static void generateDungeonChestInStructure( World world, int x, int y, int rare, Structure st){
		st.setBlock(Blocks.blockChest, x, y);

		if(world.getInventory(x, y) != null) {
			int t = MainFile.random.nextInt(world.getInventory(x, y).getInventorySize());

			for (int i = 0; i < t; i++) {
				if(MainFile.random.nextInt(4) != 0){
					world.getInventory(x, y).setItem(i, getRandomLoot(rare));
				}

			}
		}
	}


	//Used when generating through a Chunk
	public static void generateDungeonChest(World world, Chunk chunk, int x, int y, int rare){
		world.setBlock(Blocks.blockChest, x, y);

		if(chunk.getInventory(x, y) != null) {
			int t = MainFile.random.nextInt(chunk.getInventory(x, y).getInventorySize());

			for (int i = 0; i < t; i++) {
				if(MainFile.random.nextInt(4) != 0){
					chunk.getInventory(x, y).setItem(i, getRandomLoot(rare));
				}

			}
		}
	}

	//Used when generating through a Chunk using ChunkStructure
	public static void generateDungeonChestInStructure( World world, Chunk chunk, int x, int y, int rare, Structure st){
		st.setBlock(Blocks.blockChest, x, y);

		if(chunk.getInventory(x, y) != null) {
			int t = MainFile.random.nextInt(chunk.getInventory(x, y).getInventorySize() / 2);

			for (int i = 0; i < t; i++) {
				if(MainFile.random.nextInt(4) != 0){
					chunk.getInventory(x, y).setItem(i, getRandomLoot(rare));
				}

			}
		}
	}

}
