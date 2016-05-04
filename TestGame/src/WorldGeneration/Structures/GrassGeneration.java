package WorldGeneration.Structures;

import BlockFiles.Blocks;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class GrassGeneration extends StructureGeneration {

	@Override
	public boolean canGenerate( World world, Chunk chunk ) {
		return true;
	}

	@Override
	public void generate( World world, Chunk chunk ) {
		for(int x = 0; x < Chunk.chunkSize; x++){
			for(int y = 0; y < Chunk.chunkSize; y++){
				int dx = x + (chunk.chunkX * Chunk.chunkSize);
				int dy = y + (chunk.chunkY * Chunk.chunkSize);

				if(chunk.world.containesHeight(dx)){
					int h = chunk.world.getHeight(dx);

					if(dy == h)
					chunk.setBlock(Blocks.blockGrass, x, y);

					if((dy < (h + 7)) && dy > h){
						chunk.setBlock(Blocks.blockDirt, x, y);
					}
				}
			}
		}
	}

	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.NORMAL_PRIORITY;
	}
}
