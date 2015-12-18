package Blocks;


import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Blocks.Util.ITickBlock;
import Main.MainFile;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;


public class BlockGrass extends Block implements ITickBlock {

	public static Image topTexture = RenderUtil.getBlockImage("grassTop");
	public static Image sideTexture = RenderUtil.getBlockImage("grassSide");

	public BlockGrass( int x, int y ) {
		super(x, y);
	}

	public BlockGrass() {
		super();
	}

	public static boolean canGrassGrow( Block block ) {
		Block temp = MainFile.currentWorld.getBlock(block.x, block.y - 1);
		return temp == null || block != null && !temp.isBlockSolid();
	}

	public Image getBlockTextureFromSide( EnumBlockSide side ) {
		return side == EnumBlockSide.TOP ? topTexture : sideTexture;
	}

	@Override
	public String getBlockDisplayName() {
		return "Grass Block";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(0, 127, 14);
	}

	@Override
	public boolean shouldupdate() {
		return true;
	}

	@Override
	public void updateBlock() {
		if (!canGrassGrow(this)) {
			MainFile.currentWorld.setBlock(new BlockDirt(), x, y);
		}

		if (canGrassGrow(this)) {
			if (MainFile.random.nextInt(32) == 1) {
				int tempX = 0, tempY = 0;
				for (int x = this.x - 1; x < this.x + 2; x++) {
					for (int y = this.y - 1; y < this.y + 2; y++) {
						Block block = MainFile.currentWorld.getBlock(x, y);

						if (tempX == 1 && tempY == 0 || tempX == 1 && tempY == 2) continue;


						if (block != null) {
							if (block instanceof BlockDirt) {
								if (canGrassGrow(block)) {
									if (MainFile.random.nextInt(10) == 3) {
										MainFile.currentWorld.setBlock(new BlockGrass(), x, y);
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
}
