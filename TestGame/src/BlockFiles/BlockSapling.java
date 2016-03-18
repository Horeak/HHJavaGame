package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.ITickBlock;
import Main.MainFile;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.TreeGeneration;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.util.Random;

public class BlockSapling extends Block{

	private static TreeGeneration treeGeneration = new TreeGeneration();
	private static Random rand = new Random();
	public static Image texture;

	public BlockSapling(){
		treeGeneration.useRandom = false;
	}

	@Override
	public String getBlockDisplayName() {
		return "Sapling";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(63, 25, 0);
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return texture;
	}

	@Override
	public void loadTextures() {
		texture  =  MainFile.game.imageLoader.getImage("blocks","sapling");
	}

	@Override
	public boolean isBlockSolid() {
		return false;
	}

	@Override
	public boolean canPassThrough() {
		return true;
	}

	@Override
	public int getMaxBlockDamage() {
		return 1;
	}

	public ITickBlock getTickBlock(){
		return new saplingTickBlock();
	}


	class saplingTickBlock implements ITickBlock{
		@Override
		public boolean shouldUpdate( World world, int x, int y) {
			Chunk ch = world.getChunk(x, y+1);
			return treeGeneration.canGenerate(ch, x - (ch.chunkX * Chunk.chunkSize), (y - (ch.chunkY * Chunk.chunkSize))+1);
		}

		public int blockUpdateDelay() {
			return 60;
		}

		int time = 0;
		@Override
		public int getTimeSinceUpdate(World world, int x, int y) {
			return time;
		}

		@Override
		public void setTimeSinceUpdate( World world, int x, int y,int i ) {
			time = i;
		}

		@Override
		public void tickBlock( World world, int x, int y) {
			if(rand.nextInt(20) == 2){
				Chunk ch = world.getChunk(x, y+1);

				if(treeGeneration.canGenerate(ch, x - (ch.chunkX * Chunk.chunkSize), (y - (ch.chunkY * Chunk.chunkSize))+1)){
					treeGeneration.generate(ch, x - (ch.chunkX * Chunk.chunkSize), (y - (ch.chunkY * Chunk.chunkSize))+1);
				}
			}
		}
	}

}
