package WorldGeneration.Structures;

import BlockFiles.Blocks;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class AirBlockGeneration extends StructureGeneration {

	@Override
	public boolean canGenerate( Chunk chunk ) {
		return true;
	}

	@Override
	public void generate( Chunk chunk ) {
		for (int x = 0; x < Chunk.chunkSize; x++) {
			for (int y = 0; y < Chunk.chunkSize; y++) {
				if (chunk.getBlock(x, y) == null) {
					chunk.setBlock(Blocks.blockAir, x, y);
				}
			}
		}
	}

	@Override
	public String getGenerationName() {
		return "Air blocks";
	}
	
	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.LOWEST_PRIORITY;
	}
}
