package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Blocks.Util.ITickBlock;
import Utils.RenderUtil;
import WorldGeneration.TreeGeneration;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.util.Random;

public class BlockSapling extends Block implements ITickBlock{

	private static TreeGeneration treeGeneration = new TreeGeneration();
	private static Random rand = new Random();
	public static Image texture = RenderUtil.getBlockImage("sapling");

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
	public Image getBlockTextureFromSide( EnumBlockSide side ) {
		return texture;
	}

	@Override
	public boolean shouldupdate() {
		return treeGeneration.canGenerate(world, x, y+1);
	}

	public int blockupdateDelay() {
		return 60;
	}

	int time = 0;
	@Override
	public int getTimeSinceUpdate() {
		return time;
	}

	@Override
	public void setTimeSinceUpdate( int i ) {
		time = i;
	}

	@Override
	public void updateBlock() {
		if(rand.nextInt(20) == 2){
			if(treeGeneration.canGenerate(world, x, y+1)){
				treeGeneration.generate(world, x, y+1);
			}
		}
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
}
