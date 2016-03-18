package WorldGeneration.Dungeons;

import BlockFiles.Blocks;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldGeneration.Structures.ChunkStructure;
import WorldGeneration.Util.DungeonLootGenerator;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class Dungeon5Genertion extends StructureGeneration {
	@Override
	public boolean canGenerate(Chunk chunk) {
		boolean height = ((chunk.chunkY - 20) * Chunk.chunkSize) > (chunk.world.getBiome(chunk.chunkX * Chunk.chunkSize).getHeight(chunk.chunkX * Chunk.chunkSize));
		boolean rand = MainFile.random.nextInt(200) == 0;
		return rand && height;
	}

	@Override
	public void generate(Chunk chunk) {
		int length = 8;
		int height = 8;

		int x = MainFile.random.nextInt(Chunk.chunkSize - length);
		int y = MainFile.random.nextInt(Chunk.chunkSize - height);

		ChunkStructure dungeonStructure = new ChunkStructure(chunk.world, "Dungeon level 5", chunk);

		//TODO Make special mobs spawn inside dungeons! (Maybe something similar like Minecraft mob spawners?)
		for(int xx = x; xx <= (x + length); xx++){
			for(int yy = y; yy <= (y + height); yy++){
				if(xx == x || yy == y || xx == (x + length) || yy == (y + height)){
					dungeonStructure.setBlock(Blocks.blockYellowDungeonBricks, xx, yy);
				}else {
					dungeonStructure.setBlock(Blocks.blockAir, xx, yy);
				}
			}
		}

		for(int xx = x; xx <= (x + length); xx++){
			dungeonStructure.setBlock(Blocks.blockYellowDungeonBricks, xx, (y + height) - 4);
		}

		DungeonLootGenerator.generateDungeonChestInStructure(chunk.world, chunk,  (x + 3), (y + (height - 1)), 5, dungeonStructure);
		DungeonLootGenerator.generateDungeonChestInStructure(chunk.world, chunk,  (x + 4), (y + (height - 1)), 5, dungeonStructure);
		DungeonLootGenerator.generateDungeonChestInStructure(chunk.world, chunk,  (x + 5), (y + (height - 1)), 5, dungeonStructure);

		DungeonLootGenerator.generateDungeonChestInStructure(chunk.world, chunk,  (x + 3), (y + (height - 5)), 5, dungeonStructure);
		DungeonLootGenerator.generateDungeonChestInStructure(chunk.world, chunk,  (x + 5), (y + (height - 5)), 5, dungeonStructure);

		dungeonStructure.finishGen(chunk);

	}

	//Generate dungeons as one of the last world gens to prevent things like ores generating inside!
	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.LOWEST_PRIORITY;
	}
}
