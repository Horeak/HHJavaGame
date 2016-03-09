package WorldGeneration;

import BlockFiles.BlockGrass;
import BlockFiles.BlockLeaves;
import BlockFiles.BlockWood;
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

	public boolean useRandom = true;
	//TODO Fix
	@Override
	public boolean canGenerate( Chunk chunk, int x, int y ) {
		if(true)return false;

		if(chunk == null || chunk.world == null) return false;

		if (chunk.world.getBlock(x + (chunk.chunkX * Chunk.chunkSize), y  + (chunk.chunkY * Chunk.chunkSize), true) instanceof BlockGrass) {
			Block b = chunk.world.getBlock(x + (chunk.chunkX * Chunk.chunkSize), y  + (chunk.chunkY * Chunk.chunkSize));

			if (b.canBlockSeeSky(chunk.world, x, y)) {
				for (int xx = -1; xx < 2; xx++) {
					for (int yy = -6; yy < 0; yy++) {
						Block bg = chunk.getBlock(x + xx, y + yy);
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
	public void generate( Chunk chunk, int x, int y ) {
		if(chunk == null || chunk.world == null) return;

		//TODO This will be receaving chunk cordinates so convert!
		int height = 3 + MainFile.random.nextInt(4);

		for (int i = 0; i < height; i++) {
			chunk.world.setBlock(Blocks.blockWood, x + (chunk.chunkX * Chunk.chunkSize), y - (i + 1) + (chunk.chunkY * Chunk.chunkSize));
		}

		Point p = new Point(x + (chunk.chunkX * Chunk.chunkSize), y + (chunk.chunkY * Chunk.chunkSize) - height);

		for (int xx = -5; xx < 5; xx++) {
			for (int yy = -(height + 2); yy <= -(height - 1); yy++) {

				if (xx == -2 && yy == -(height + 2)) {
					continue;
				}
				if (xx == 2 && yy == -(height + 2)) {
					continue;
				}

				int xPos = (x + xx) + (chunk.chunkX * Chunk.chunkSize);
				int yPos = (y + yy) + (chunk.chunkY * Chunk.chunkSize);

				if (chunk.getBlock(xPos, yPos) == null) {

					if (p.distance(xPos, yPos) <= 3) {
						chunk.world.setBlock(Blocks.blockLeaves, xPos, yPos);
					}
				}
			}
		}

		for (int i = 0; i < height; i++) {
			chunk.world.setBlock(Blocks.blockWood, x + (chunk.chunkX * Chunk.chunkSize), y + (chunk.chunkY * Chunk.chunkSize) - (i + 1));
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
