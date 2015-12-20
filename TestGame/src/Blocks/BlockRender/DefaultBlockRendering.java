package Blocks.BlockRender;

import Blocks.Util.Block;
import Blocks.Util.ILightSource;
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

	public static Image[] breakImages = new Image[]{ RenderUtil.getImage("textures/breakBlock", "break1"), RenderUtil.getImage("textures/breakBlock", "break2"), RenderUtil.getImage("textures/breakBlock", "break3"), RenderUtil.getImage("textures/breakBlock", "break4"), RenderUtil.getImage("textures/breakBlock", "break5") };

	private static void drawFront( Graphics g, int xStart, int yStart, Color c) {
		g.setColor(c);
		g.fill(new Rectangle(xStart, yStart, ConfigValues.size, ConfigValues.size));
	}

	private static void drawSide( Graphics g, int xStart, int yStart, Color c) {
		xStart += ConfigValues.size;

		Path path = new Path(xStart, yStart);

		path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + (ConfigValues.size / 2), yStart + (ConfigValues.size / 2));
		path.lineTo(xStart, yStart + ConfigValues.size);

		path.close();
		g.setColor(c);
		g.fill(path);
	}

	private static void drawTop( Graphics g, int xStart, int yStart, Color c) {
		Path path = new Path(xStart, yStart);
		path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + ((ConfigValues.size * 1.5F)), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + ConfigValues.size, yStart);
		path.close();
		g.setColor(c);
		g.fill(path);
	}


	private static void drawShadowFront( Graphics g, int xStart, int yStart, Block block) {
		float t = (float)block.getLightValue() / (float)ILightSource.MAX_LIGHT_STRENGTH;

		Color temp = block.getLightUnit().getLightColor();
		Color c = new Color(0, 0, 0, 1.02F - t);

		Rectangle tangle = new Rectangle(xStart, yStart, ConfigValues.size, ConfigValues.size);

		g.setColor(c);
		g.fill(tangle);

		if(temp != ILightSource.DEFAULT_LIGHT_COLOR){
			g.setColor(new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), t));
			g.fill(tangle);
		}


	}

	private static void drawShadowSide( Graphics g, int xStart, int yStart, Block block) {
		float t = (float)block.getLightValue() / (float)ILightSource.MAX_LIGHT_STRENGTH;

		Color temp = block.getLightUnit().getLightColor();
		Color c = new Color(0, 0, 0, 1.02F - t);

		xStart += ConfigValues.size;
		Path path = new Path(xStart, yStart);

		path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + (ConfigValues.size / 2), yStart + (ConfigValues.size / 2));
		path.lineTo(xStart, yStart + ConfigValues.size);

		path.close();
		g.setColor(c);
		g.fill(path);

		if(temp != ILightSource.DEFAULT_LIGHT_COLOR){
			g.setColor(new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), t));
			g.fill(path);
		}

	}

	private static void drawShadowTop( Graphics g, int xStart, int yStart, Block block) {
		float t = ((float)block.getLightValue() / (float)ILightSource.MAX_LIGHT_STRENGTH);

		Color temp = block.getLightUnit().getLightColor();
		Color c = new Color(0, 0, 0, 1.02F - t);

		Path path = new Path(xStart, yStart);
		path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + ((ConfigValues.size * 1.5F)), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + ConfigValues.size, yStart);
		path.close();
		g.setColor(c);
		g.fill(path);

		if(temp != ILightSource.DEFAULT_LIGHT_COLOR){
			g.setColor(new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), t));
			g.fill(path);
		}

	}

	@Override
	public void renderBlock( Graphics g, int rX, int rY, EnumRenderMode renderMode, Block block, boolean right, boolean top, boolean renderLighting, boolean isItem ) {
		if (ConfigValues.simpleBlockRender) {
			BlockUtils.renderDefaultBlockDebug(g, block, rX, rY);
			return;
		}

		if (!block.useBlockTexture()) {
			if (renderMode == EnumRenderMode.render2_5D) {

				if (right) {
					int xStart = rX, yStart = rY;
					drawSide(g, xStart, yStart, block.getDefaultBlockColor().darker());

					if (!isItem) {
						if (getBreakImageForBlock(block) != null) {
							getBreakImageForBlock(block).drawWarped(xStart, yStart, xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2), xStart + (ConfigValues.size / 2), yStart + (ConfigValues.size / 2), xStart, yStart + ConfigValues.size);
						}
					}

					if (renderLighting) {
						drawShadowSide(g, xStart, yStart, block);
					} else {
						drawSide(g, xStart, yStart, block.getDefaultBlockColor().darker());
					}
				}

				if (top) {
					int xStart = rX, yStart = rY;

					drawTop(g, xStart, yStart, block.getDefaultBlockColor().brighter());

					if (!isItem) {
						if (getBreakImageForBlock(block) != null) {
							getBreakImageForBlock(block).drawWarped(rX, rY, rX + (ConfigValues.size / 2), rY - (ConfigValues.size / 2), rX + ((ConfigValues.size) * 1.5F), rY - (ConfigValues.size / 2), rX + ConfigValues.size, rY);
						}
					}

					if (renderLighting) {
						drawShadowTop(g, xStart, yStart, block);
					} else {
						drawTop(g, xStart, yStart, block.getDefaultBlockColor().brighter());
					}
				}

				drawFront(g, rX, rY, block.getDefaultBlockColor());

				if(renderLighting)
					drawShadowFront(g, rX, rY, block);

				if (!isItem) {
					if (getBreakImageForBlock(block) != null) {
						getBreakImageForBlock(block).draw(rX, rY, ConfigValues.size, ConfigValues.size);
					}
				}

			} else {
				drawFront(g, rX, rY, block.getDefaultBlockColor());

				if(renderLighting)
					drawShadowFront(g, rX, rY, block);

				if (!isItem) {
					if (getBreakImageForBlock(block) != null) {
						getBreakImageForBlock(block).draw(rX, rY, ConfigValues.size, ConfigValues.size);
					}
				}
			}
		} else {
			int xStart = rX, yStart = rY;
			if (renderMode == EnumRenderMode.render2_5D) {

				if (right) {
					if (block.getBlockTextureFromSide(EnumBlockSide.SIDE) != null) {
						g.pushTransform();

						g.rotate(xStart + (ConfigValues.size / 2), yStart + ConfigValues.size / 2, 90);
						g.translate(0, -ConfigValues.size);

						Image image = block.getBlockTextureFromSide(EnumBlockSide.SIDE).getFlippedCopy(true, false);
						image.drawWarped(xStart - (ConfigValues.size / 2), yStart + (ConfigValues.size / 2), xStart + (ConfigValues.size / 2), yStart + (ConfigValues.size / 2), xStart + (ConfigValues.size), yStart + (ConfigValues.size), xStart, yStart + (ConfigValues.size));


						if (!isItem) {
							if (getBreakImageForBlock(block) != null) {
								getBreakImageForBlock(block).getFlippedCopy(true, false).drawWarped(xStart - (ConfigValues.size / 2), yStart + (ConfigValues.size / 2), xStart + (ConfigValues.size / 2), yStart + (ConfigValues.size / 2), xStart + (ConfigValues.size), yStart + (ConfigValues.size), xStart, yStart + (ConfigValues.size));
							}
						}

						g.popTransform();

						if (renderLighting) {
							drawShadowSide(g, xStart, yStart, block);
						} else {
							drawSide(g, xStart, yStart, new Color(0, 0, 0, 0.26F));
						}

					} else {
						drawSide(g, xStart, yStart, block.getDefaultBlockColor().darker());
					}
				}

				if (top) {
					if (block.getBlockTextureFromSide(EnumBlockSide.TOP) != null) {
						Image image = block.getBlockTextureFromSide(EnumBlockSide.TOP).getFlippedCopy(true, false);

						g.pushTransform();

						g.rotate(rX + (ConfigValues.size / 2), rY + ConfigValues.size / 2, 180);
						g.translate(-(ConfigValues.size / 2), ConfigValues.size * 1.5F);

						image.drawWarped(rX, rY, rX + (ConfigValues.size / 2), rY - (ConfigValues.size / 2), rX + ((ConfigValues.size) * 1.5F), rY - (ConfigValues.size / 2), rX + ConfigValues.size, rY);


						if (!isItem) {
							if (getBreakImageForBlock(block) != null) {
								getBreakImageForBlock(block).getFlippedCopy(true, false).drawWarped(rX, rY, rX + (ConfigValues.size / 2), rY - (ConfigValues.size / 2), rX + ((ConfigValues.size) * 1.5F), rY - (ConfigValues.size / 2), rX + ConfigValues.size, rY);
							}
						}

						g.popTransform();

						if(renderLighting) {
							drawShadowTop(g, xStart, yStart, block);
							drawTop(g, xStart, yStart, new Color(1, 1, 1, 0.05F));
						}else {
							drawTop(g, xStart, yStart, new Color(0.6F, 0.6F, 0.6F, 0.3F));
						}

					} else {
						drawTop(g, xStart, yStart, block.getDefaultBlockColor().brighter());
					}
				}

				if (block.getBlockTextureFromSide(EnumBlockSide.FRONT) != null) {
					Image image = block.getBlockTextureFromSide(EnumBlockSide.FRONT);
					image.draw(xStart, yStart, ConfigValues.size, ConfigValues.size);

					if (!isItem) {
						if (getBreakImageForBlock(block) != null) {
							getBreakImageForBlock(block).draw(xStart, yStart, ConfigValues.size, ConfigValues.size);
						}
					}

					if(renderLighting) {
						drawShadowFront(g, xStart, yStart, block);
						drawFront(g, xStart, yStart, new Color(1,1,1, 0.02F));
					}
				} else {
					drawFront(g, xStart, yStart, block.getDefaultBlockColor());
				}

			} else {
				if (block.getBlockTextureFromSide(EnumBlockSide.FRONT) != null) {
					Image image = block.getBlockTextureFromSide(EnumBlockSide.FRONT);
					image.draw(xStart, yStart, ConfigValues.size, ConfigValues.size);

					if (!isItem) {
						if (getBreakImageForBlock(block) != null) {
							getBreakImageForBlock(block).draw(xStart, yStart, ConfigValues.size, ConfigValues.size);
						}
					}


					if (renderLighting) {
						drawShadowFront(g, xStart, yStart, block);
					}

					drawFront(g, xStart, yStart, new Color(1,1,1, 0.02F));

				} else {
					drawFront(g, xStart, yStart, block.getDefaultBlockColor());
				}
			}
		}


	}

	public Image getBreakImageForBlock( Block block ) {
		float tt = ((float) block.getBlockDamage() / (float) block.getMaxBlockDamage()) * 5;

		if ((int) tt != 0 && ((int) tt) >= 0 && ((int) tt - 1) < 5) {
			return breakImages[ (int) tt - 1 ];
		}

		return null;
	}
}
