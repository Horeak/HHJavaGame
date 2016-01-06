package WorldGeneration.Structures;

import Blocks.BlockDirt;
import Blocks.BlockGrass;
import Render.SimplexNoise;
import WorldFiles.World;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class GrassGeneration extends StructureGeneration {
	@Override
	public boolean canGenerate( World world ) {
		return true;
	}

	@Override
	public void generate( World world ) {
		SimplexNoise noise = new SimplexNoise();

		int frequency = world.worldSize.ySize / world.worldSize.div;

		for (int x = 0; x < world.worldSize.xSize; x++) {
			float h = (noise.noise((float) x / frequency, 0) + 1) / 2; // make noise 0 to 1
			for (int y = 0; y < (world.worldSize.ySize); y++) {

				int ySize = (world.worldSize.ySize) / 4;
				float current = (float) (ySize - y) / ySize;

				if (current < h) {
					world.setBlock(new BlockGrass(), x, y);

					if(!world.getBlock(x, y).canBlockSeeSky(world, x, y)){
						world.setBlock(new BlockDirt(), x, y);
					}
				}
			}
		}
	}

	@Override
	public String getGenerationName() {
		return "Grass Generation";
	}

	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.NORMAL_PRIORITY;
	}
}
