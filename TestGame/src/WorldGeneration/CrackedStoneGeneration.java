package WorldGeneration;

import Blocks.BlockCrackedStone;
import Blocks.BlockStone;
import Main.MainFile;
import WorldFiles.World;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.WorldGenPriority;

public class CrackedStoneGeneration extends GenerationBase {
	@Override
	public boolean canGenerate( World world, int x, int y ) {
		return world.getBlock(x, y) != null && world.getBlock(x, y) instanceof BlockStone && MainFile.random.nextInt(150) == 10;
	}

	@Override
	public void generate( World world, int x, int y ) {
		world.setBlock(new BlockCrackedStone(), x, y);
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
