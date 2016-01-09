package WorldGeneration.Structures;


import Blocks.BlockStone;
import Render.SimplexNoise;
import WorldFiles.World;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class StoneGeneration extends StructureGeneration {
	@Override
	public boolean canGenerate( World world ) {
		return true;
	}

	@Override
	public void generate( World world ) {
		SimplexNoise noise = new SimplexNoise();

		int frequency = (world.worldSize.ySize) / world.worldSize.div;

		for (int x = 0; x < world.worldSize.xSize; x++) {
			float h = (noise.noise((float) x / frequency, 0) + 1) / 2; // make noise 0 to 1
			for (int y = world.worldSize.ySize - 1; y > 0; y--) {

				try {
					if (world.getBlock(x, y - 1) != null && world.getBlock(x, y - 2) != null && world.getBlock(x, y - 3) != null && world.getBlock(x, y - 4) != null && world.getBlock(x, y - 5) != null) {

						int ySize = (world.worldSize.ySize) / 4;

						float current = (float) (ySize - y) / ySize;
						if (current <= h) {
							world.setBlock(new BlockStone(), x, y);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
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
