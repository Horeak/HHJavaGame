package WorldGeneration.Structures;

import BlockFiles.Blocks;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

import java.awt.*;

public class CaveGeneration extends StructureGeneration {

	@Override
	public boolean canGenerate( Chunk chunk) {
		boolean biome = chunk.world.getBiome((chunk.chunkX)) != null;
		boolean height = biome ? ((chunk.chunkY + 1) * Chunk.chunkSize) > chunk.world.getHeight((chunk.chunkX * Chunk.chunkSize)) : false;
		boolean rand = MainFile.random.nextInt(100) == 0 || true;

		return  biome && height && rand;
	}

	@Override
	public void generate( Chunk chunk) {

		for(int i = 0; i < MainFile.random.nextInt(3); i++) {
			int xS = MainFile.random.nextInt(Chunk.chunkSize);
			int yS = MainFile.random.nextInt(Chunk.chunkSize);

			int xRange = MainFile.random.nextInt(Chunk.chunkSize);
			int yRange = MainFile.random.nextInt(Chunk.chunkSize);

//			System.out.println("Generated cave at: " + (xS + (chunk.chunkX * Chunk.chunkSize)) + ", " + (yS + (chunk.chunkY * Chunk.chunkSize)) + " Range: " + (xRange + ", " + yRange) + " Chunk: " + chunk.chunkX + ", " + chunk.chunkY);

			for (int x = xS - xRange; x < xS + xRange; x++) {
				for (int y = yS - yRange; y < yS + yRange; y++) {
//					Line lineX = new Line(xS, x);
//					Line lineY = new Line(yS, y);

					//TODO Fix this making holes in the ground level
					if (chunk.world.getBiome(chunk.chunkX) != null && y > (chunk.world.getHeight(x + (chunk.chunkX * Chunk.chunkSize)) + 4)&& Point.distance(x, y, xS, yS) < ((xRange + yRange) / 2)) {
						chunk.setBlock(Blocks.blockAir, x, y);
					}

				}
			}
		}

	}


	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.NORMAL_PRIORITY;
	}
}
