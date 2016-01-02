package WorldGeneration;

import Blocks.BlockCrackedStone;
import Blocks.BlockStone;
import WorldFiles.World;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.WorldGenPriority;

import java.util.Random;

public class CrackedStoneGeneration extends GenerationBase {
	@Override
	public boolean canGenerate( World world, int x, int y ) {
		return world.getBlock(x, y) instanceof BlockStone && new Random().nextInt(20) == 1;
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
