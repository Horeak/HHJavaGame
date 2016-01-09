package Render.Renders;

import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.BlockSelection;
import Utils.ConfigValues;
import Utils.RenderUtil;
import WorldFiles.EnumWorldTime;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;


public class DebugInfoRender extends AbstractWindowRender {
	@Override
	public void render( Graphics g2 ) {
		Color temp = g2.getColor();

		int xCord = (ConfigValues.renderXSize * ConfigValues.size) - 25;
		int ySize = (ConfigValues.renderYSize * ConfigValues.size);

		Rectangle tangle = new Rectangle(xCord, BlockRendering.START_Y_POS, 266, ySize);
		g2.setClip(tangle);

		g2.setColor(Color.darkGray);
		g2.fill(tangle);
		g2.setColor(Color.black);

		tangle.setLocation(tangle.getX() + 1, tangle.getY());
		tangle.setSize(tangle.getWidth() - 2, tangle.getHeight() - 1);

		g2.draw(tangle);

		int textStartX = xCord + 5;
		int textStartY = BlockRendering.START_Y_POS + 15;

		int linePos = textStartY;
		int lineLength = 13;

		g2.setColor(Color.black);

		RenderUtil.changeFontName(g2, "Times New Roman");
		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);

		g2.drawString("Debug info", textStartX, linePos += 2);
		RenderUtil.resetFont(g2);

		RenderUtil.changeFontName(g2, "Arial");
		RenderUtil.resizeFont(g2, 13);
		RenderUtil.changeFontStyle(g2, Font.BOLD);

		g2.drawString("FPS: " + MainFile.gameContainer.getFPS(), textStartX, linePos += (lineLength * 2));

		if (MainFile.getServer().getWorld() != null) {
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("World Size: ", textStartX, linePos += (lineLength * 2));

			g2.drawString(" - " + (MainFile.getServer().getWorld().worldSize.xSize) + " blocks wide.", textStartX, linePos += lineLength);
			g2.drawString(" - " + (MainFile.getServer().getWorld().worldSize.ySize) + " blocks high.", textStartX, linePos += lineLength);


			g2.drawString("World info:", textStartX, linePos += (lineLength * 2));

			g2.drawString(" - World time: " + MainFile.getServer().getWorld().WorldTime + " / " + MainFile.getServer().getWorld().WorldTimeDayEnd, textStartX, linePos += (lineLength));
			g2.drawString(" - Time to next phase (" + MainFile.getServer().getWorld().getNextWorldTime().name + "): " + ((MainFile.getServer().getWorld().getNextWorldTime() == EnumWorldTime.MORNING ? EnumWorldTime.NIGHT.timeEnd : MainFile.getServer().getWorld().getNextWorldTime().timeBegin) - MainFile.getServer().getWorld().WorldTime), textStartX, linePos += (lineLength));
			g2.drawString(" - Time of day: " + MainFile.getServer().getWorld().worldTimeOfDay.name, textStartX, linePos += (lineLength));
			g2.drawString(" - Day number: " + MainFile.getServer().getWorld().WorldDay, textStartX, linePos += (lineLength));


			if(MainFile.getClient().getPlayer() != null && !MainFile.getServer().getWorld().generating) {
				g2.drawString("Player info:", textStartX, linePos += (lineLength * 2));

				g2.drawString(" - Player pos: " + MainFile.getClient().getPlayer().getEntityPostion(), textStartX, linePos += (lineLength));
				g2.drawString(" - Block below: " + (MainFile.getClient().getPlayer().getBlockBelow() != null ? MainFile.getClient().getPlayer().getBlockBelow().getBlockDisplayName() : null), textStartX, linePos += (lineLength));
				g2.drawString(" - Is on Ground: " + (MainFile.getClient().getPlayer().isOnGround), textStartX, linePos += (lineLength));
				g2.drawString(" - Blocks fallen: " + (MainFile.getClient().getPlayer().blocksFallen), textStartX, linePos += lineLength);
			}

		}

		g2.drawString("Block render size: " + ConfigValues.size, textStartX, linePos += (lineLength * 2));

		if (MainFile.getServer().getWorld() != null) {
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Currently selected block: " + (BlockSelection.selectedBlock != null ? BlockSelection.selectedBlock.getItemName() : "None"), textStartX, linePos += (lineLength * 2));

			if (BlockSelection.selectedBlock != null) {
				g2.setColor(Color.black);
				g2.drawString("Block cords: " + "[" + BlockSelection.selectedX + ", " + BlockSelection.selectedY + "]", textStartX, linePos += (lineLength));
				if (BlockSelection.selectedBlock.blockInfoList.size() > 0) {
					g2.drawString("Blockinfo: ", textStartX, linePos += (lineLength));
					for (String t : BlockSelection.selectedBlock.blockInfoList) {
						g2.drawString((!t.isEmpty() ? " - " : "") + t, textStartX, linePos += (lineLength));
					}
				}

				BlockSelection.selectedBlock.blockInfoList.clear();
				BlockSelection.selectedBlock.addInfo(MainFile.getServer().getWorld(), BlockSelection.selectedX, BlockSelection.selectedY);
			}
		}

		RenderUtil.resetFont(g2);

		g2.setColor(temp);
		g2.setClip(MainFile.blockRenderBounds);
	}


	@Override
	public boolean canRender() {
		return ConfigValues.debug;
	}

	@Override
	public boolean canRenderWithWindow() {
		return true;
	}
}
