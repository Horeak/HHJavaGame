package Blocks;


import Blocks.Util.Block;
import Blocks.Util.BlockUpdate;
import Main.MainFile;
import Render.EnumRenderMode;
import Utils.ConfigValues;
import Utils.RenderUtil;

import java.awt.*;
import java.awt.geom.Path2D;

public class BlockGrass extends Block implements BlockUpdate {

	//TODO Improve grass rendering. Instead of one random height try to use smooth noise on grass depth
	int randHeight = MainFile.random.nextInt(5) + 1;

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

	@Override
	public String getBlockDisplayName() {
		return "Grass Block";
	}

	@Override
	public void renderBlock( Graphics2D g2, int renderX, int renderY ) {
		if (randHeight != -1) {
			Color c = g2.getColor();
			Color g = getDefaultBlockColor();

			//Render dirt underlay
			BlockDirt dirt = new BlockDirt();
			if (ConfigValues.renderMod == EnumRenderMode.render2_5D && MainFile.currentWorld != null) {
				RenderUtil.renderDefault2_5DBlock(g2, dirt, renderX, renderY, MainFile.currentWorld.getBlock(x, y - 1) == null, MainFile.currentWorld.getBlock(x + 1, y) == null);
			}
			g2.setColor(dirt.getDefaultBlockColor());
			RenderUtil.darkenColorBasedOnTime(g2);

			g2.fill(new Rectangle(renderX, renderY, ConfigValues.size, ConfigValues.size));


			//Render 2.5D grass
			if (MainFile.currentWorld != null) {
				boolean right = MainFile.currentWorld.getBlock(x + 1, y) == null, top = MainFile.currentWorld.getBlock(x, y - 1) == null;

				if (ConfigValues.renderMod == EnumRenderMode.render2_5D) {
					if (right) {
						int xStart = renderX + ConfigValues.size, yStart = renderY;

						Path2D.Double path = new Path2D.Double();

						path.moveTo(xStart, yStart);

						int height = 12 - randHeight;

						path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
						path.lineTo(xStart + (ConfigValues.size / 2), yStart + (ConfigValues.size / 2) - height);
						path.lineTo(xStart, yStart + ConfigValues.size - height);

						path.closePath();

						g2.setColor(getDefaultBlockColor().darker());
						RenderUtil.darkenColorBasedOnTime(g2);

						g2.fill(path);
					}

					if (top) {
						int xStart = renderX, yStart = renderY;

						Path2D.Double path = new Path2D.Double();

						path.moveTo(xStart, yStart);
						path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
						path.lineTo(xStart + (ConfigValues.size + (ConfigValues.size / 2)), yStart - (ConfigValues.size / 2));
						path.lineTo(xStart + ConfigValues.size, yStart);

						path.closePath();

						g2.setColor(getDefaultBlockColor().brighter());
						RenderUtil.darkenColorBasedOnTime(g2);

						g2.fill(path);
					}
				}

			}

			g2.setColor(g);
			g2.fill(new Rectangle(renderX, renderY, ConfigValues.size, 20 + randHeight));
			g2.setColor(c);


		}
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(0, 127, 14);
	}

	@Override
	public boolean isBlockSolid() {
		return true;
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

	public void addInfo() {
		blockInfoList.add("Grass depth: " + randHeight);
	}
}
