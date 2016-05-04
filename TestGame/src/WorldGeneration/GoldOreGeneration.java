package WorldGeneration;

import BlockFiles.BlockStone;
import BlockFiles.Blocks;
import Main.MainFile;
import NoiseGenerator.PerlinNoiseGenerator;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Structures.ChunkStructure;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.WorldGenPriority;

public class GoldOreGeneration extends GenerationBase {
	@Override
	public boolean canGenerate( World world, Chunk chunk, int x, int y ) {
		return chunk.getBlock(x, y) instanceof BlockStone && (y + (chunk.chunkY * Chunk.chunkSize)) > 50 && MainFile.random.nextInt(200) == 0;
	}

	@Override
	public void generate( World world, Chunk chunk, int x, int y ) {
		PerlinNoiseGenerator noiseGenerator = new PerlinNoiseGenerator(chunk.world.worldSeed);
		int range = 2 + MainFile.random.nextInt(2);

		ChunkStructure chunkStructure = new ChunkStructure(chunk, "Gold Vein");

		int vein = 0;
		for(int xx = x - (range / 2); xx < x + (range / 2); xx++){
			for(int yy = y - (range / 2); yy < y + (range / 2); yy++){
				double d = noiseGenerator.noise(xx, yy) * 10;

				if(d > 1.5){
					chunkStructure.setBlock(Blocks.blockGoldOre, xx, yy);
					vein += 1;
				}

			}
		}

		chunk.setStructure(chunkStructure);
	}


	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.NORMAL_PRIORITY;
	}
}
