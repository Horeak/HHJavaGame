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
		return chunk.getBlock(x, y) != null && chunk.getBlock(x, y) instanceof BlockStone && MainFile.random.nextInt(150) == 10;
	}

	@Override
	public void generate( Chunk chunk, int x, int y ) {
			chunk.setBlock(Blocks.blockCrackedStone, x, y);
	}

	@Override
	public String getGenerationName() {
		return "Cracked Stone generation";
	}

	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.NORMAL_PRIORITY;
	}
}
