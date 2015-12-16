package Blocks.BlockRender;

import Blocks.Util.Block;
import Render.EnumRenderMode;
import Utils.BlockUtils;
import Utils.ConfigValues;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Rectangle;

public class DefaultBlockRendering implements BlockRenderer {

	//To make it possible to access without making a new instance for example for the grass block which will use both this and a custom one
	public static DefaultBlockRendering staticReference = new DefaultBlockRendering();

	private static void drawFront( Graphics g, int xStart, int yStart, Color c ) {
		g.setColor(c);
		RenderUtil.darkenColorBasedOnTime(g);
		g.fill(new Rectangle(xStart, yStart, ConfigValues.size, ConfigValues.size));
	}

	private static void drawSide( Graphics g, int xStart, int yStart, Color c ) {
		xStart += ConfigValues.size;

		Path path = new Path(xStart, yStart);

		path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + (ConfigValues.size / 2), yStart + (ConfigValues.size / 2));
		path.lineTo(xStart, yStart + ConfigValues.size);

		path.close();
		g.setColor(c);
		RenderUtil.darkenColorBasedOnTime(g);
		g.fill(path);
	}

	private static void drawTop( Graphics g, int xStart, int yStart, Color c ) {
		Path path = new Path(xStart, yStart);
		path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + ((ConfigValues.size * 1.5F)), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + ConfigValues.size, yStart);
		path.close();
		g.setColor(c);
		RenderUtil.darkenColorBasedOnTime(g);
		g.fill(path);
	}

	//TODO Fix Right render with texutre
	@Override
	public void renderBlock( Graphics g, int rX, int rY, EnumRenderMode renderMode, Block block, boolean right, boolean top ) {
		if (ConfigValues.simpleBlockRender) {
			BlockUtils.renderDefaultBlockDebug(g, block, rX, rY);
			return;
		}

		if (!block.useBlockTexture()) {
			if (renderMode == EnumRenderMode.render2_5D) {
				if (right) {
					int xStart = rX, yStart = rY;
					drawSide(g, xStart, yStart, block.getDefaultBlockColor().darker());
				}

				if (top) {
					int xStart = rX, yStart = rY;
					drawTop(g, xStart, yStart, block.getDefaultBlockColor().brighter());
				}
				drawFront(g, rX, rY, block.getDefaultBlockColor());
			} else {
				drawFront(g, rX, rY, block.getDefaultBlockColor());
			}
		} else {
			int xStart = rX, yStart = rY;

			if (renderMode == EnumRenderMode.render2_5D) {
				if (right) {
					if (block.getBlockTextureFromSide(EnumBlockSide.SIDE) != null) {
						int xx = xStart + (int) (ConfigValues.size * 1.5F);
						int yy = yStart - (ConfigValues.size / 2);

						Image image = block.getBlockTextureFromSide(EnumBlockSide.SIDE).getFlippedCopy(true, false);
						image.setRotation(90);
						image.drawWarped(xx - (ConfigValues.size / 2), yy, xx + (ConfigValues.size / 2), yy, xx + (ConfigValues.size), yy + (ConfigValues.size / 2), xx, yy + (ConfigValues.size / 2));

						image.setRotation(0);

						drawSide(g, xStart, yStart, new Color(0, 0, 0, 0.2F));
					} else {
						drawSide(g, xStart, yStart, block.getDefaultBlockColor().darker());
					}
				}

				if (top) {
					if (block.getBlockTextureFromSide(EnumBlockSide.TOP) != null) {
						Image image = block.getBlockTextureFromSide(EnumBlockSide.TOP);
						image.drawWarped(rX, rY, rX + (ConfigValues.size / 2), rY - (ConfigValues.size / 2), rX + ((ConfigValues.size) * 1.5F), rY - (ConfigValues.size / 2), rX + ConfigValues.size, rY);

						drawTop(g, xStart, yStart, new Color(1, 1, 1, 0.2F));
					} else {
						drawTop(g, xStart, yStart, block.getDefaultBlockColor().brighter());
					}
				}

				if (block.getBlockTextureFromSide(EnumBlockSide.FRONT) != null) {
					Image image = block.getBlockTextureFromSide(EnumBlockSide.FRONT);
					image.draw(xStart, yStart, ConfigValues.size, ConfigValues.size);
				} else {
					drawFront(g, xStart, yStart, block.getDefaultBlockColor());
				}

			} else {
				if (block.getBlockTextureFromSide(EnumBlockSide.FRONT) != null) {
					Image image = block.getBlockTextureFromSide(EnumBlockSide.FRONT);
					image.draw(xStart, yStart, ConfigValues.size, ConfigValues.size);
				} else {
					drawFront(g, xStart, yStart, block.getDefaultBlockColor());
				}
			}
		}
	}
}
