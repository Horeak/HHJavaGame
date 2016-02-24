package BlockFiles;


import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.ITickBlock;
import Items.Utils.ItemStack;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;


public class BlockGrass extends Block implements ITickBlock {

	public static Image topTexture =  MainFile.game.imageLoader.getImage("blocks","grassTop");
	public static Image sideTexture =  MainFile.game.imageLoader.getImage("blocks","grassSide");

	public static boolean canGrassGrow( World world, int x, int y ) {
		Block block = world.getBlock(x, y);
		Block temp = world.getBlock(x, y - 1);

		boolean light = block.getLightValue(world, x, y) >= (3 * world.worldTimeOfDay.lightMultiplier);
		boolean above = temp == null || temp != null && !temp.isBlockSolid();

		return light && above;
	}

	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return side == EnumBlockSide.TOP ? topTexture : sideTexture;
	}

	@Override
	public String getBlockDisplayName() {
		return "Grass";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.green.darker().darker();
	}

	@Override
	public boolean shouldupdate(World world, int x, int y) {
		return true;
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
	public void updateBlock(World world, int xx, int yy) {
		if (!canGrassGrow(world, xx, yy)) {
			world.setBlock(Blocks.blockDirt, xx, yy);
		}

		if (canGrassGrow(world, xx, yy)) {
			if (MainFile.random.nextInt(32) == 1) {
				int tempX = 0, tempY = 0;
				for (int x = xx - 1; x < xx + 2; x++) {
					for (int y = yy - 1; y < yy + 2; y++) {
						Block block = world.getBlock(x, y);

						if (tempX == 1 && tempY == 0 || tempX == 1 && tempY == 2) continue;


						if (block != null) {
							if (block instanceof BlockDirt) {
								if (canGrassGrow(world, x, y)) {
									if (MainFile.random.nextInt(10) == 3) {
										world.setBlock(Blocks.blockGrass, x, y);
									}
								}
							}
						}

						tempY += 1;
					}
					tempY = 0;
					tempX += 1;
				}
			}
		}
	}

	public int blockupdateDelay() {
		return 5;
	}


	public ItemStack getItemDropped(World world, int x, int y) {
		return new ItemStack(Blocks.blockDirt);
	}

	public int getMaxBlockDamage() {
		return 5;
	}
}
