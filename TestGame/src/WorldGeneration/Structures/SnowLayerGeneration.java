package WorldGeneration.Structures;

import BlockFiles.BlockSnowLayer;
import BlockFiles.Blocks;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class SnowLayerGeneration extends StructureGeneration {

	@Override
	public boolean canGenerate( World world, Chunk chunk ) {
		return chunk.chunkY * Chunk.chunkSize >= chunk.world.getHeight(chunk.chunkX * Chunk.chunkSize) - Chunk.chunkSize || chunk.chunkY * Chunk.chunkSize <= chunk.world.getHeight(chunk.chunkX * Chunk.chunkSize) + Chunk.chunkSize;
	}

	@Override
	public void generate( World world, Chunk chunk ) {
		for(int x = 0; x < Chunk.chunkSize; x++){
			for(int y = 0; y < Chunk.chunkSize; y++){
				int dx = x + (chunk.chunkX * Chunk.chunkSize);
				int dy = y + (chunk.chunkY * Chunk.chunkSize);

				//TODO Must fix gen height! (Snow is generating below ground...)
				if(chunk.chunkY * Chunk.chunkSize >= chunk.world.getHeight(chunk.chunkX * Chunk.chunkSize) - Chunk.chunkSize || chunk.chunkY * Chunk.chunkSize <= chunk.world.getHeight(chunk.chunkX * Chunk.chunkSize) + Chunk.chunkSize){
					if (chunk.getBlock(x, y + 1) != null && !(chunk.getBlock(x, y + 1) instanceof BlockSnowLayer) && chunk.getBlock(x, y) == null || chunk.getBlock(x, y) == null && world.getBlock(dx, dy + 1) != null && !(world.getBlock(dx, dy + 1) instanceof BlockSnowLayer)) {

					//	boolean canSeeSky = world.getBlock(dx, dy + 1) != null && world.getBlock(dx, dy + 1).canBlockSeeSky(world, dx, dy + 1);
						//TODO Make canSeeSky work

						chunk.setBlock(Blocks.blockSnowLayer, x, y);
					}
				}
			}
		}
	}


	//Low priority because it generates ontop of other blocks
	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.LOWEST_PRIORITY;
	}
}
