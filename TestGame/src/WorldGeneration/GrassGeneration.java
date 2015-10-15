package WorldGeneration;
/*
* Project: Random Java Creations
* Package: WorldGeneration.Util
* Created: 26.07.2015
*/

import Blocks.BlockDirt;
import Blocks.BlockGrass;
import Blocks.Util.Block;
import Main.MainFile;
import Render.SimplexNoise;
import WorldFiles.EnumWorldSize;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

public class GrassGeneration extends StructureGeneration {
	@Override
	public boolean canGenerate() {
		return true;
	}
	@Override
	public void generate() {
		SimplexNoise noise = new SimplexNoise();
		int frequency = MainFile.currentWorld.worldSize == EnumWorldSize.LARGE ? 230 : MainFile.currentWorld.worldSize == EnumWorldSize.MEDIUM ? 90 : 30;

		for(int x = 0; x < MainFile.currentWorld.worldSize.xSize; x++) {
			float h = (noise.noise((float) x / frequency, 0) + 1) / 2; // make noise 0 to 1
			for (int y = 0; y < (MainFile.currentWorld.worldSize.ySize); y++) {

				int ySize = (MainFile.currentWorld.worldSize.ySize) / 4;
				float current = (float) (ySize - y) / ySize;

				Block b = null;
				Block temp = MainFile.currentWorld.getBlock(x, y + 1);

				if(temp == null || !temp.isBlockSolid())
					b = new BlockGrass();
				else if(temp != null && temp.isBlockSolid())
					b = new BlockDirt();

				if(current < h) MainFile.currentWorld.setBlock(b, x, y);
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
