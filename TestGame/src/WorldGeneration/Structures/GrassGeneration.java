package WorldGeneration.Structures;

import BlockFiles.BlockDirt;
import BlockFiles.BlockGrass;
import BlockFiles.Blocks;
import Main.MainFile;
import WorldFiles.Biome;
import WorldFiles.Chunk;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class GrassGeneration extends StructureGeneration {

	@Override
	public boolean canGenerate( Chunk chunk ) {
		return true;
	}


	//TODO Seems to sometimes ignore heightMap?
	@Override
	public void generate( Chunk chunk ) {
		for(int x = 0; x < Chunk.chunkSize; x++){
			for(int y = 0; y < Chunk.chunkSize; y++){
				int dx = x + (chunk.chunkX * Chunk.chunkSize);
				int dy = y + (chunk.chunkY * Chunk.chunkSize);

				//TODO Improve dirt/grass. Dirt is generating above grass?
				if(Biome.heightMap.get(dx) != null){
					int h = Biome.heightMap.get(dx); //TODO Does it load the wrong value from the heightMap?

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
	public String getGenerationName() {
		return "Grass and Dirt Generation";
	}

	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.NORMAL_PRIORITY;
	}
}
