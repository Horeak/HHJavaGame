package WorldGeneration.Structures;
import Blocks.BlockDirt;
import Blocks.BlockGrass;
import Blocks.Util.Block;
import Render.SimplexNoise;
import WorldFiles.World;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class GrassGeneration extends StructureGeneration {
	@Override
	public boolean canGenerate( World world ) {
		return true;
	}


	//TODO Randomize frequency
	@Override
	public void generate( World world ) {
		SimplexNoise noise = new SimplexNoise();

		int frequency = world.worldSize.ySize / world.worldSize.div;

		for (int x = 0; x < world.worldSize.xSize; x++) {
			float h = (noise.noise((float) x / frequency, 0) + 1) / 2; // make noise 0 to 1
			for (int y = 0; y < (world.worldSize.ySize); y++) {

				int ySize = (world.worldSize.ySize) / 4;
				float current = (float) (ySize - y) / ySize;

				Block b = null;
				Block temp = world.getBlock(x, y + 1);

				if (temp == null || !temp.isBlockSolid()) b = new BlockGrass();
				else if (temp != null && temp.isBlockSolid()) b = new BlockDirt();

				if (current < h) {
					world.setBlock(b, x, y);
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
