package WorldGeneration.Dungeons;

import BlockFiles.Blocks;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldGeneration.Structures.ChunkStructure;
import WorldGeneration.Util.DungeonLootGenerator;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class Dungeon1Genertion extends StructureGeneration {
	@Override
	public boolean canGenerate(Chunk chunk) {
		boolean height = ((chunk.chunkY - 2) * Chunk.chunkSize) > (chunk.world.getBiome(chunk.chunkX * Chunk.chunkSize).getHeight(chunk.chunkX * Chunk.chunkSize));
		boolean rand = MainFile.random.nextInt(20) == 0;
		return rand && height;
	}

	//TODO Make higher level dungeons either alot more rare or add some kind of danger to them!
	@Override
	public void generate(Chunk chunk) {
		int length = 6;
		int height = 5;

		int x = MainFile.random.nextInt(Chunk.chunkSize - length);
		int y = MainFile.random.nextInt(Chunk.chunkSize - height);

		ChunkStructure dungeonStructure = new ChunkStructure(chunk.world, "Dungeon level 1", chunk);

		//TODO Make special mobs spawn inside dungeons! (Maybe something similar like Minecraft mob spawners?)
		for(int xx = x; xx <= (x + length); xx++){
			for(int yy = y; yy <= (y + height); yy++){
				if(xx == x || yy == y || xx == (x + length) || yy == (y + height)){
					dungeonStructure.setBlock(Blocks.blockCrackedStone, xx, yy);
				}else {
					dungeonStructure.setBlock(Blocks.blockAir, xx, yy);
				}
			}
		}

		DungeonLootGenerator.generateDungeonChestInStructure(chunk.world, chunk,  ((x + 1) + ((length - 2) / 2)), (y + (height - 1)), 1, dungeonStructure);
		dungeonStructure.finishGen(chunk);

	}

	//Generate dungeons as one of the last world gens to prevent things like ores generating inside!
	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.LOWEST_PRIORITY;
	}
}
