package WorldGeneration.Structures;


import BlockFiles.Blocks;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class StoneGeneration extends StructureGeneration {

	@Override
	public boolean canGenerate( World world, Chunk chunk ) {
		return true;
	}

	@Override
	public void generate( World world, Chunk chunk ) {
		for(int x = 0; x < Chunk.chunkSize; x++){
			for(int y = 0; y < Chunk.chunkSize; y++){
				int dy = y + (chunk.chunkY * Chunk.chunkSize);
				int dx = x + (chunk.chunkX * Chunk.chunkSize);

				if(chunk.world.containesHeight(dx)){
					if(dy >= (chunk.world.getHeight(dx) + (3 + (MainFile.random.nextInt(2))))){
						if(chunk.getBlock(x, y) == null) {
							chunk.setBlock(Blocks.blockStone, x, y);
						}
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
