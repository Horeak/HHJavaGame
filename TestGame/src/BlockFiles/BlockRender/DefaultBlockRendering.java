package BlockFiles.BlockRender;

import BlockFiles.Util.Block;
import Utils.BlockUtils;
import Utils.ConfigValues;
import Utils.RenderUtil;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Graphics;

public class DefaultBlockRendering implements IBlockRenderer {

	public static DefaultBlockRendering staticReference = new DefaultBlockRendering();

	@Override
	public void renderBlock( Graphics g, int xStart, int yStart, Block block, boolean right, boolean top, boolean isItem, World world, int x, int y, int face ) {
		if (ConfigValues.simpleBlockRender) {
			BlockUtils.renderDefaultBlockDebug(g, block, xStart, yStart);
			return;
		}

		if (!block.useBlockTexture()) {
				if (right && face == 0) {
					IBlockRenderer.drawSide(g, xStart, yStart, block.getDefaultBlockColor());
				}

				if (top && face == 1) {
					IBlockRenderer.drawTop(g, xStart, yStart, block.getDefaultBlockColor());
				}

				if(face == 2) {
					IBlockRenderer.drawFront(g, xStart, yStart, block.getDefaultBlockColor());
				}
		} else {
			if (right && face == 0) {
				IBlockRenderer.drawRightImage(g, xStart, yStart, block.getBlockTextureFromSide(EnumBlockSide.SIDE, world, x, y) != null ? block.getBlockTextureFromSide(EnumBlockSide.SIDE, world, x, y).getFlippedCopy(true, false) : TextureLoader.unknownBlock.getFlippedCopy(true, false));

				if(block.getBlockTextureFromSide(EnumBlockSide.SIDE, world, x, y) == null){
					IBlockRenderer.drawSide(g, xStart, yStart, RenderUtil.getColorWithAlpha(block.getDefaultBlockColor(), 0.5F));
				}

			}

			if (top && face == 0) {
				IBlockRenderer.drawTopImage(g, xStart, yStart, block.getBlockTextureFromSide(EnumBlockSide.TOP, world, x, y) != null ? block.getBlockTextureFromSide(EnumBlockSide.TOP, world, x, y).getFlippedCopy(true, false) : TextureLoader.unknownBlock.getFlippedCopy(true, false));

				if(block.getBlockTextureFromSide(EnumBlockSide.TOP, world, x, y) == null){
					IBlockRenderer.drawTop(g, xStart, yStart, RenderUtil.getColorWithAlpha(block.getDefaultBlockColor(), 0.5F));
				}

				}

			if(face == 2) {
				IBlockRenderer.drawFrontImage(g, xStart, yStart, block.getBlockTextureFromSide(EnumBlockSide.FRONT, world, x, y) != null ? block.getBlockTextureFromSide(EnumBlockSide.FRONT, world, x, y).getFlippedCopy(true, false) : TextureLoader.unknownBlock.getFlippedCopy(true, false));

				if(block.getBlockTextureFromSide(EnumBlockSide.FRONT, world, x, y) == null){
					IBlockRenderer.drawFront(g, xStart, yStart, RenderUtil.getColorWithAlpha(block.getDefaultBlockColor(), 0.5F));
				}
			}

		}

	}
}
