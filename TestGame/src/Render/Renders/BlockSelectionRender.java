package Render.Renders;

import Blocks.BlockDirt;
import Blocks.BlockGrass;
import Blocks.BlockStone;
import Blocks.Util.Block;
import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.BlockUtils;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

public class BlockSelectionRender extends AbstractWindowRender {

	public static Block selectedBlock = null;

	@Override
	public void render(JFrame frame, Graphics2D g2) {
		Color temp = g2.getColor();

		//TODO Make block selectrion be block locked even when player moves over the block-lock
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);

		if(frame != null && frame.getMousePosition() != null){
			Point mousePoint = frame.getMousePosition();
			if(mousePoint != null)
			if(mousePoint.x > BlockRendering.START_X_POS && mousePoint.x < (ConfigValues.renderXSize * ConfigValues.size) + BlockRendering.START_X_POS) {
				if (mousePoint.y > (BlockRendering.START_Y_POS) && mousePoint.y < (ConfigValues.renderYSize * ConfigValues.size) + BlockRendering.START_Y_POS ) {

					//TODO Why do i need +24 on y-axis?
					int mouseX = mousePoint.x - (BlockRendering.START_X_POS);
					int mouseY = mousePoint.y - (BlockRendering.START_Y_POS + 24);

					int mouseBlockX = mouseX / (ConfigValues.size);
					int mouseBlockY = mouseY / (ConfigValues.size);

					float tempX = 0, tempY = 0;
					int renderX = 0, renderY = 0;
					boolean setX = false, setY = false;

					int xStart = (int) (MainFile.currentWorld.player.getEntityPostion().x - ConfigValues.renderRange), xEnd = (int)(MainFile.currentWorld.player.getEntityPostion().x + ConfigValues.renderRange + 1);
					int yStart = (int) (MainFile.currentWorld.player.getEntityPostion().y - ConfigValues.renderRange), yEnd = (int)(MainFile.currentWorld.player.getEntityPostion().y + ConfigValues.renderRange + 1);

					for (int x = xStart; x < xEnd; x++) {
						for (int y = yStart; y < yEnd; y++) {
							if(renderX == mouseBlockX){
								tempX = x;
								setX = true;
							}

							if(renderY == mouseBlockY){
								tempY = y;
								setY = true;
							}

							if(setX && setY)
								break;

							renderY += 1;
						}
						renderY = 0;
						renderX += 1;
					}

					float tX = (float)(plPos.x - (int) plPos.x);
					float tY = (float)(plPos.y - (int) plPos.y);

					selectedX = Math.round(tempX + tX);
					selectedY = Math.round(tempY + tY);

					Block b = MainFile.currentWorld.getBlock(selectedX, selectedY);
					selectedBlock = b;

					if(selectedX >= 0 && selectedY >= 0) {
						Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS + (int)((mouseBlockX) * ConfigValues.size), BlockRendering.START_Y_POS + (int)((mouseBlockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
						g2.setColor(new Color(255, 255, 255, 64));
						g2.fill(rectangle);

						g2.setColor(Color.WHITE);
						g2.draw(rectangle);
					}else{
						Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS + (int)((mouseBlockX) * ConfigValues.size), BlockRendering.START_Y_POS + (int)((mouseBlockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
						g2.setColor(new Color(255, 0, 0, 64));
						g2.fill(rectangle);

						g2.setColor(Color.RED);
						g2.draw(rectangle);

						g2.draw(new Line2D.Double(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height));
						g2.draw(new Line2D.Double(rectangle.x + rectangle.width, rectangle.y, rectangle.x, rectangle.y + rectangle.height));

					}


				}else{
					selectedY = -1;
					selectedX = -1;
				}
			}else{
				selectedY = -1;
				selectedX = -1;
			}

		}else{
			selectedY = -1;
			selectedX = -1;
		}

		g2.setColor(temp);
	}


	//TODO Remove debug feature and replace with proper hotbar/inventory
	public static int selectedX = -1, selectedY = -1;
	public void mouseClick(MouseEvent e, JFrame frame){
			if (HotbarRender.slotSelected == 1) {

				if(BlockUtils.canPlaceBlockAt(new BlockGrass(), selectedX, selectedY))
				MainFile.currentWorld.setBlock(new BlockGrass(), selectedX, selectedY);

			} else if (HotbarRender.slotSelected == 2) {

				if(BlockUtils.canPlaceBlockAt(new BlockDirt(), selectedX, selectedY))
				MainFile.currentWorld.setBlock(new BlockDirt(), selectedX, selectedY);

			} else if (HotbarRender.slotSelected == 3) {

				if(BlockUtils.canPlaceBlockAt(new BlockStone(), selectedX, selectedY))
				MainFile.currentWorld.setBlock(new BlockStone(), selectedX, selectedY);

			} else if (HotbarRender.slotSelected > 3) {

				if(BlockUtils.canPlaceBlockAt(null, selectedX, selectedY))
				MainFile.currentWorld.setBlock(null, selectedX, selectedY);
			}
	}

	@Override
	public boolean canRender(JFrame frame) {
		return ConfigValues.RENDER_HOTBAR;
	}

	@Override
	public boolean canRenderWithGui() {
		return false;
	}
}
