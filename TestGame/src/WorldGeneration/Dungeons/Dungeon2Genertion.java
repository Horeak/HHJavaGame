package WorldGeneration.Dungeons;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Blocks;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldGeneration.Structures.ChunkStructure;
import WorldGeneration.Util.DungeonLootGenerator;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;
import org.newdawn.slick.Image;

import java.io.Serializable;

public class Dungeon2Genertion extends StructureGeneration  implements Serializable{
	@Override
	public boolean canGenerate(Chunk chunk) {
		boolean height = ((chunk.chunkY - 4) * Chunk.chunkSize) > (chunk.world.getHeight(chunk.chunkX * Chunk.chunkSize));
		boolean rand = MainFile.random.nextInt(40) == 0;
		return rand && height;
	}

	@Override
	public void generate(Chunk chunk) {
		int length = 8;
		int height = 5;

		int x = MainFile.random.nextInt(Chunk.chunkSize - length);
		int y = MainFile.random.nextInt(Chunk.chunkSize - height);

		ChunkStructure dungeonStructure = new ChunkStructure(chunk, "Dungeon level 2"){
			@Override
			public boolean shouldRemoveBlock( int x, int y ) {
				return false;
			}

			@Override
			public Image getBackgroundImage() {
				return Blocks.blockBlueDungeonBricks.getBlockTextureFromSide(EnumBlockSide.FRONT.FRONT, MainFile.game.getServer().getWorld(), 0, 0);
			}
		};

		//TODO Make special mobs spawn inside dungeons! (Maybe something similar like Minecraft mob spawners?)
		for(int xx = x; xx <= (x + length); xx++){
			for(int yy = y; yy <= (y + height); yy++){
				if(xx == x || yy == y || xx == (x + length) || yy == (y + height)){
					dungeonStructure.setBlock(Blocks.blockBlueDungeonBricks, xx, yy);
				}else {
					dungeonStructure.setBlock(Blocks.blockAir, xx, yy);
				}
			}
		}

		DungeonLootGenerator.generateDungeonChestInStructure(chunk.world, chunk,  (x + 3), (y + (height - 1)), 2, dungeonStructure);
		DungeonLootGenerator.generateDungeonChestInStructure(chunk.world, chunk,  (x + 5), (y + (height - 1)), 2, dungeonStructure);

		chunk.setStucture(dungeonStructure);

	}

	//Generate dungeons as one of the last world gens to prevent things like ores generating inside!
	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.LOWEST_PRIORITY;
	}
}
