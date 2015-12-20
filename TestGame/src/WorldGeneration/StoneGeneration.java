package WorldGeneration;
/*
* Project: Random Java Creations
* Package: WorldGeneration.Util
* Created: 26.07.2015
*/

import Blocks.BlockCrackedStone;
import Blocks.BlockStone;
import Main.MainFile;
import Render.SimplexNoise;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

import java.util.Random;

public class StoneGeneration extends StructureGeneration {
	int frequency = 30;

	@Override
	public boolean canGenerate() {
		return true;
	}

	@Override
	public void generate() {
		SimplexNoise noise = new SimplexNoise();

		for (int x = 0; x < MainFile.currentWorld.worldSize.xSize; x++) {
			float h = (noise.noise((float) x / frequency, 0) + 1) / 2; // make noise 0 to 1
			for (int y = MainFile.currentWorld.worldSize.ySize - 1; y > 0; y--) {

				try {
					if (MainFile.currentWorld.getBlock(x, y - 1) != null && MainFile.currentWorld.getBlock(x, y - 2) != null && MainFile.currentWorld.getBlock(x, y - 3) != null && MainFile.currentWorld.getBlock(x, y - 4) != null && MainFile.currentWorld.getBlock(x, y - 5) != null) {

						int ySize = (MainFile.currentWorld.worldSize.ySize) / 4;

						float current = (float) (ySize - y) / ySize;
						if (current < h)
							MainFile.currentWorld.setBlock(new Random().nextInt(10) == 0 ? new BlockCrackedStone() : new BlockStone(), x, y);
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
