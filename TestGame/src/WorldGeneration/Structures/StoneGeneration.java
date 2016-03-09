package WorldGeneration.Structures;


import BlockFiles.Blocks;
import Main.MainFile;
import Utils.LoggerUtil;
import WorldFiles.Biome;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class StoneGeneration extends StructureGeneration {

	@Override
	public boolean canGenerate( Chunk chunk ) {
		return true;
	}

	@Override
	public void generate( Chunk chunk ) {
		for(int x = 0; x < Chunk.chunkSize; x++){
			for(int y = 0; y < Chunk.chunkSize; y++){
				int dy = y + (chunk.chunkY * Chunk.chunkSize);
				int dx = x + (chunk.chunkX * Chunk.chunkSize);

				if(Biome.heightMap.containsKey(dx)){
					if(dy >= (Biome.heightMap.get(dx) + (3 + (MainFile.random.nextInt(2))))){
						chunk.setBlock(Blocks.blockStone, x, y);
					}
				}
			}
		}
	}

	@Override
	public String getGenerationName() {
		return "Stone Generation";
	}

	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.NORMAL_PRIORITY;
	}
}
