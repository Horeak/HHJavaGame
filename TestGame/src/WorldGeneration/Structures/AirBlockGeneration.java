package WorldGeneration.Structures;

import BlockFiles.Blocks;
import WorldFiles.World;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class AirBlockGeneration extends StructureGeneration {
	@Override
	public boolean canGenerate( World world ) {
		return true;
	}
	
	@Override
	public void generate( World world ) {
		for (int x = 0; x < world.worldSize.xSize; x++) {
			for (int y = 0; y < world.worldSize.ySize; y++) {
				if (world.getBlock(x, y) == null) {
					world.setBlock(Blocks.blockAir, x, y);
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
