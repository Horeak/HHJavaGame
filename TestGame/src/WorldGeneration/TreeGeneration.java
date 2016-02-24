package WorldGeneration;

import BlockFiles.BlockGrass;
import BlockFiles.BlockLeaves;
import BlockFiles.BlockWood;
import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import Main.MainFile;
import WorldFiles.World;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.WorldGenPriority;

import java.awt.*;
import java.util.Random;

public class TreeGeneration extends GenerationBase {

	public boolean useRandom = true;

	@Override
	public boolean canGenerate( World world, int x, int y ) {
		if (world.getBlock(x, y) instanceof BlockGrass) {
			Block b = world.getBlock(x, y);

			if (b.canBlockSeeSky(world, x, y)) {
				for (int xx = -1; xx < 2; xx++) {
					for (int yy = -6; yy < 0; yy++) {
						Block bg = world.getBlock(x + xx, y + yy);
						if (bg != null && bg.isBlockSolid() || bg != null && !bg.canPassThrough()) {
							return false;
						}
					}
				}

				return useRandom ? new Random().nextInt(10) == 1 : true;
			}
		}

		return false;
	}

	@Override
	public void generate( World world, int x, int y ) {
		int height = 3 + MainFile.random.nextInt(4);

		for (int i = 0; i < height; i++) {
			world.setBlock(Blocks.blockWood, x, y - (i + 1));
		}

		Point p = new Point(x, y - height);

		for (int xx = -5; xx < 5; xx++) {
			for (int yy = -(height + 2); yy < -(height - 2); yy++) {

				if (xx == -2 && yy == -(height + 2)) {
					continue;
				}
				if (xx == 2 && yy == -(height + 2)) {
					continue;
				}

				int xPos = x + xx;
				int yPos = y + yy;

				if (world.getBlock(xPos, yPos) == null) {

					if (p.distance(xPos, yPos) <= 3) {
						world.setBlock(Blocks.blockLeaves, xPos, yPos);
					}
				}
			}
		}

	}

	@Override
	public String getGenerationName() {
		return "Tree generation";
	}

	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.LOW_PRIORITY;
	}
}
