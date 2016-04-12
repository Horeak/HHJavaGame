package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.Material;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockLeaves extends Block {
	public static Image texture;

	//TODO Make leaves placed by player stay

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
		return texture;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		texture =  imageLoader.getImage("blocks", "leaves");
	}

	@Override
	public ItemStack getItemDropped(World world, int x, int y) {
		return MainFile.random.nextInt(10) == 1 ? new ItemStack(Blocks.blockSapling) : null;
	}

	@Override
	public Material getBlockMaterial() {
		return Material.PLANT;
	}

	public boolean isBlockSolid() {
		return false;
	}

	public boolean canPassThrough() {
		return true;
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

		if (!canStay(world, curX, curY) && world.getBlock(curX, curY) instanceof BlockLeaves) {
			world.breakBlock(curX, curY);
		}
	}
}
