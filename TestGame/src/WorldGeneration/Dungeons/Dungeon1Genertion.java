package WorldGeneration.Dungeons;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Blocks;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Structures.ChunkStructure;
import WorldGeneration.Util.DungeonLootGenerator;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;
import org.newdawn.slick.Image;

import java.io.Serializable;

public class Dungeon1Genertion extends StructureGeneration implements Serializable {
	@Override
	public boolean canGenerate( World world, Chunk chunk ) {
		boolean height = ((chunk.chunkY - 2) * Chunk.chunkSize) > (chunk.world.getHeight(chunk.chunkX * Chunk.chunkSize));
		boolean rand = MainFile.random.nextInt(20) == 0;
		return rand && height;
	}

	//TODO Make higher level dungeons either alot more rare or add some kind of danger to them!
	@Override
	public void generate( World world, Chunk chunk ) {
		int length = 6;
		int height = 5;

		int x = MainFile.random.nextInt(Chunk.chunkSize - length);
		int y = MainFile.random.nextInt(Chunk.chunkSize - height);

		ChunkStructure dungeonStructure = new ChunkStructure(chunk, "Dungeon level 1"){
			@Override
			public boolean shouldRemoveBlock( int x, int y ) {
				return false;
			}

			@Override
			public Image getBackgroundImage() {
				return Blocks.blockCrackedStone.getBlockTextureFromSide(EnumBlockSide.FRONT.FRONT, MainFile.game.getServer().getWorld(), 0, 0);
			}
		};

		//TODO Make special mobs spawn inside dungeons! (Maybe something similar like Minecraft mob spawners?)
		for(int xx = x; xx <= (x + length); xx++){
			for(int yy = y; yy <= (y + height); yy++){
				if(xx == x || yy == y || xx == (x + length) || yy == (y + height)){
					dungeonStructure.setBlock(Blocks.blockCrackedStone, xx, yy);
				}else {
					dungeonStructure.setBlock(Blocks.blockAir, xx, yy);
				}
			}
		}

		DungeonLootGenerator.generateDungeonChestInStructure(chunk.world, chunk,  ((x + 1) + ((length - 2) / 2)), (y + (height - 1)), 1, dungeonStructure);
		chunk.setStructure(dungeonStructure);

	}

	//Generate dungeons as one of the last world gens to prevent things like ores generating inside!
	@Override
	public WorldGenPriority generationPriority() {
		return WorldGenPriority.LOWEST_PRIORITY;
	}
}
