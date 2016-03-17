package WorldGeneration.Structures;

import BlockFiles.Blocks;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;
import org.newdawn.slick.geom.Line;

import java.awt.*;

public class CaveGeneration extends StructureGeneration {

	//TODO Somehow make it work with a "region" instead of one chunk (for example generate it each 4 chunks.)
	@Override
	public boolean canGenerate( Chunk chunk) {
		boolean biome = chunk.world.getBiome((chunk.chunkX * Chunk.chunkSize)) != null;
		boolean height = biome ? ((chunk.chunkY + 1) * Chunk.chunkSize) > chunk.world.getBiome((chunk.chunkX * Chunk.chunkSize)).getHeight((chunk.chunkX * Chunk.chunkSize)) : false;
		boolean rand = MainFile.random.nextInt(100) == 0 || true;
		//TODO Is caves too rare or not working at all?w

		return  biome && height && rand;
	}

	//TODO Generate more then once
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

					//TODO Make corners round!
					if (y > (chunk.world.getBiome(x + (chunk.chunkX * Chunk.chunkSize))).getHeight(x + (chunk.chunkX * Chunk.chunkSize)) && Point.distance(x, y, xS, yS) < ((xRange + yRange) / 2)) {
						//TODO Try and make this work with setting blocks through the world and not the chunk
						chunk.world.setBlock(Blocks.blockAir, x + (chunk.chunkX * Chunk.chunkSize), y + (chunk.chunkY * Chunk.chunkSize));
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
