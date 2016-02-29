package WorldGeneration.Structures;

import BlockFiles.Blocks;
import WorldFiles.Biome;
import WorldFiles.Chunk;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class GrassGeneration extends StructureGeneration {

	@Override
	public boolean canGenerate( Chunk chunk ) {
		return true;
	}

	@Override
	public void generate( Chunk chunk ) {
		for(int x = 0; x < Chunk.chunkSize; x++){
			for(int y = 0; y < Chunk.chunkSize; y++){
				chunk.setBlock_(Blocks.blockGrass, x, y);
			}
		}

//		SimplexNoiseGenerator noise = new SimplexNoiseGenerator(chunk.world.worldSeed);
//
//		int frequency = (chunk.chunkY) / 2;
//
//		for (int x = 0; x < Chunk.chunkSize; x++) {
//			float h = ((float)noise.noise((float) x / frequency, 0) + 1) / 2; // make noise 0 to 1
//			for (int y = 0; y < Chunk.chunkSize; y++) {
//
//				int ySize = (chunk.chunkY * Chunk.chunkSize) / 4;
//				float current = (float) (ySize - y) / ySize;
//				current = -10000;
//
//				if (current < h) {
//					if(chunk.getBlock(x, y - 1) != null){
//						chunk.setBlock_(Blocks.blockDirt, x, y);
//					}else {
//						chunk.setBlock_(Blocks.blockGrass, x, y);
//					}
//				}
//			}
//		}
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
