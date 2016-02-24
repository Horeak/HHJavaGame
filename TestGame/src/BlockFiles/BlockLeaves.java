package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import Items.Utils.ItemStack;
import Main.MainFile;
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
		return Color.green.darker();
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		if (texture == null) {
			texture =  MainFile.game.imageLoader.getImage("blocks", "leaves");
		}

		return texture;
	}

	//TODO Add saplings
	@Override
	public ItemStack getItemDropped(World world, int x, int y) {
		return rand.nextInt(10) == 1 ? new ItemStack(Blocks.blockSapling) : null;
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
			world.breakBlock(curX, curY);
		}
	}
}
