package WorldGeneration;

import BlockFiles.BlockCrackedStone;
import BlockFiles.BlockStone;
import BlockFiles.Blocks;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.WorldGenPriority;

public class CrackedStoneGeneration extends GenerationBase {

	@Override
	public boolean canGenerate( Chunk chunk, int x, int y ) {
		return chunk.getBlock(x, y) instanceof BlockStone && (y + (chunk.chunkY * Chunk.chunkSize)) > 25 && MainFile.random.nextInt(150) == 10;
	}

	@Override
	public void generate( Chunk chunk, int x, int y ) {
			chunk.setBlock(Blocks.blockCrackedStone, x, y);
	}


	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.NORMAL_PRIORITY;
	}
}
