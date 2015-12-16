package Render.Renders;

import Blocks.Util.Block;
import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.BlockUtils;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class BlockSelectionRender extends AbstractWindowRender {


	//TODO BlockSelection not rendering with Slick2D

	public static Block selectedBlock = null;
	//TODO Remove debug feature and replace with proper hotbar/inventory
	public static int selectedX = -1, selectedY = -1;

	@Override
	public void render( Graphics g2 ) {
		org.newdawn.slick.Color temp = g2.getColor();

		//TODO Make block selectrion be block locked even when player moves over the block-lock
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);

		if (MainFile.gameContainer != null && MainFile.gameContainer.getInput() != null) {
			Point mousePoint = new Point(MainFile.gameContainer.getInput().getMouseX(), MainFile.gameContainer.getInput().getMouseY());
			if (mousePoint != null)
				if (mousePoint.getX() > BlockRendering.START_X_POS && mousePoint.getX() < (ConfigValues.renderXSize * ConfigValues.size) + BlockRendering.START_X_POS) {
					if (mousePoint.getY() > (BlockRendering.START_Y_POS) && mousePoint.getY() < (ConfigValues.renderYSize * ConfigValues.size) + BlockRendering.START_Y_POS) {

						//TODO Why do i need +24 on y-axis?
						int mouseX = (int) mousePoint.getX() - (BlockRendering.START_X_POS);
						int mouseY = (int) mousePoint.getY() - (BlockRendering.START_Y_POS + 24);

						int mouseBlockX = mouseX / (ConfigValues.size);
						int mouseBlockY = mouseY / (ConfigValues.size);

						float tempX = 0, tempY = 0;
						int renderX = 0, renderY = 0;
						boolean setX = false, setY = false;

						int xStart = (int) (MainFile.currentWorld.player.getEntityPostion().x - ConfigValues.renderRange), xEnd = (int) (MainFile.currentWorld.player.getEntityPostion().x + ConfigValues.renderRange + 1);
						int yStart = (int) (MainFile.currentWorld.player.getEntityPostion().y - ConfigValues.renderRange), yEnd = (int) (MainFile.currentWorld.player.getEntityPostion().y + ConfigValues.renderRange + 1);

						for (int x = xStart; x < xEnd; x++) {
							for (int y = yStart; y < yEnd; y++) {
								if (renderX == mouseBlockX) {
									tempX = x;
									setX = true;
								}

								if (renderY == mouseBlockY) {
									tempY = y;
									setY = true;
								}

								if (setX && setY) break;

								renderY += 1;
							}
							renderY = 0;
							renderX += 1;
						}

						float tX = (float) (plPos.x - (int) plPos.x);
						float tY = (float) (plPos.y - (int) plPos.y);

						selectedX = Math.round(tempX + tX);
						selectedY = Math.round(tempY + tY) + 1;

						Block b = MainFile.currentWorld.getBlock(selectedX, selectedY);
						selectedBlock = b;

						if (selectedX >= 0 && selectedY >= 0) {
							Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS + ((selectedX) * ConfigValues.size), BlockRendering.START_Y_POS + ((selectedY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
							g2.setColor(new Color(255, 255, 255, 64));
							g2.fill(rectangle);

							g2.setColor(Color.white);
							g2.draw(rectangle);
						} else {
							Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS + ((selectedX) * ConfigValues.size), BlockRendering.START_Y_POS + ((selectedY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
							g2.setColor(new Color(255, 0, 0, 64));
							g2.fill(rectangle);

							g2.setColor(Color.red);
							g2.draw(rectangle);

							g2.draw(new Line(rectangle.getX(), rectangle.getY(), rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()));
							g2.draw(new Line(rectangle.getX() + rectangle.getWidth(), rectangle.getY(), rectangle.getX(), rectangle.getY() + rectangle.getHeight()));

						}

					} else {
						selectedY = -1;
						selectedX = -1;
					}
				} else {
					selectedY = -1;
					selectedX = -1;
				}

		} else {
			selectedY = -1;
			selectedX = -1;
		}

		g2.setColor(temp);
	}

	public void mouseClick( int button, int x, int y ) {
		try {

			int selected = (HotbarRender.slotSelected - 1);

			if (selected < HotbarRender.blocks.length) {
				if (HotbarRender.blocks[ selected ] instanceof Block) {
					Block block = (Block) HotbarRender.blocks[ selected ].getClass().newInstance();

					if (block != null) {
						if (BlockUtils.canPlaceBlockAt(block, selectedX, selectedY)) {
							MainFile.currentWorld.setBlock(block, selectedX, selectedY);
						}
					}
				}
			} else {
				if (BlockUtils.canPlaceBlockAt(null, selectedX, selectedY)) {
				MainFile.currentWorld.setBlock(null, selectedX, selectedY);
				}
		}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_HOTBAR;
	}

	@Override
	public boolean canRenderWithGui() {
		return false;
	}
}
