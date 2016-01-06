package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Items.Utils.ItemStack;
import Utils.RenderUtil;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.util.Random;

public class BlockLeaves extends Block {
	public static Image texture;
	private static Random rand = new Random();

	@Override
	public String getBlockDisplayName() {
		return "Leaves";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.green;
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		if (texture == null) {
			texture = RenderUtil.getBlockImage("leaves");
		}

		return texture;
	}

	//TODO Add saplings
	@Override
	public ItemStack getItemDropped(World world, int x, int y) {
		return rand.nextInt(10) == 1 ? new ItemStack(new BlockSapling()) : null;
	}

	public boolean isBlockSolid() {
		return false;
	}

	public boolean canPassThrough() {
		return false;
	}

	public boolean canStay(World world, int x, int y) {
		for (int xx = -3; xx <= 3; xx++) {
			for (int yy = -3; yy <= 3; yy++) {
				int xPos = xx + x;
				int yPos = yy + y;

				if (world.getBlock(xPos, yPos) instanceof BlockWood) {
					return true;
				}
			}
		}

		return false;
	}

	public int getMaxBlockDamage() {
		return 2;
	}

	@Override
	public void updateBlock( World world, int fromX, int fromY, int curX, int curY ) {
		super.updateBlock(world, fromX, fromY, curX, curY);

		if (!canStay(world, curX, curY)) {
			if(world.player != null){
				ItemStack stack = getItemDropped(world, curX, curY);

				if(stack != null)
				world.player.addItem(stack);
			}
			world.setBlock(null, curX, curY);
		}
	}
}
