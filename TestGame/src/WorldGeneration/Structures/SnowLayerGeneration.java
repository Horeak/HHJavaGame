package WorldGeneration.Structures;

import BlockFiles.Blocks;
import WorldFiles.Chunk;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class SnowLayerGeneration extends StructureGeneration {

	@Override
	public boolean canGenerate( Chunk chunk ) {
		return true;
	}

	@Override
	public void generate( Chunk chunk ) {
		for(int x = 0; x < Chunk.chunkSize; x++){
			for(int y = 0; y < Chunk.chunkSize; y++){
				int dx = x + (chunk.chunkX * Chunk.chunkSize);
				int dy = y + (chunk.chunkY * Chunk.chunkSize);

				//TODO Snow is generating above snow...
				//TODO Add height filter to prevent underground snow!
//				if(y - Chunk.chunkSize >= chunk.world.getHeight(x) || y + Chunk.chunkSize <= chunk.world.getHeight(x)) {
					if (chunk.getBlock(x, y + 1) != null && chunk.getBlock(x, y + 1).isBlockSolid() && chunk.getBlock(x, y) == null || chunk.getBlock(x, y) == null && chunk.world.getBlock(dx, dy + 1) != null && chunk.world.getBlock(dx, dy + 1).isBlockSolid()) {

						chunk.setBlock(Blocks.blockSnowLayer, x, y);
					}
//				}
			}
		}
	}


	//Low priority because it generates ontop of other blocks
	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.LOWEST_PRIORITY;
	}
}
