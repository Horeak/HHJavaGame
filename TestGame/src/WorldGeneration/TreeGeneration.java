package WorldGeneration;

import BlockFiles.BlockGrass;
import BlockFiles.BlockSapling;
import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.WorldGenPriority;

import java.awt.*;
import java.util.Random;

public class TreeGeneration extends GenerationBase {

	//TODO Make TreeGeneration chunk independent
	public boolean useRandom = true;
	public boolean sapling = false;



	//TODO FIX!

	@Override
	public boolean canGenerate( World world, Chunk chunk, int x, int y ) {
		if(chunk == null || chunk.world == null) return false;
		//TODO Return false if biome doesnt support trees!

		if (chunk.getBlock(x, y, true) instanceof BlockGrass) {
			Block b = chunk.getBlock(x, y);

			for (int xx = -1; xx < 2; xx++) {
				for (int yy = -6; yy < 0; yy++) {
					Block bg = chunk.getBlock(x + xx, y + yy);
					if (bg != null && !(bg instanceof BlockSapling)) {
						return false;
					}
				}

				boolean height = useRandom ? y >= chunk.world.getHeight(x + (chunk.chunkX * Chunk.chunkSize)) : true;
				return height && (useRandom ? new Random().nextInt(10) == 1 : true);
			}
		}

		return false;
	}

	//TODO FIX
	@Override
	public void generate( World world, Chunk chunk, int x, int y ) {
		if(chunk == null || chunk.world == null) return;

		int height = 3 + MainFile.random.nextInt(4);

		for (int i = 0; i < height; i++) {
			chunk.setBlock(Blocks.blockWood, x, (y - (i + 1)));
		}

		Point p = new Point(x, y - height);

		for (int xx = -5; xx < 5; xx++) {
			for (int yy = -(height + 2); yy <= -(height - 1); yy++) {

				if (xx == -2 && yy == -(height + 2)) {
					continue;
				}
				if (xx == 2 && yy == -(height + 2)) {
					continue;
				}

				int xPos = (x + xx);
				int yPos = (y + yy);

				if (chunk.getBlock(xPos, yPos) == null) {
					if (p.distance(xPos, yPos) <= 3) {
						//TODO Leaves not being set when xPos == x
						chunk.setBlock(Blocks.blockLeaves, xPos, yPos);
					}
				}
			}
		}


	}
	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.LOW_PRIORITY;
	}
}
