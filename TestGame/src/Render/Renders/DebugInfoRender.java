package Render.Renders;

import Main.MainFile;
import Rendering.AbstractWindowRender;
import Utils.BlockSelection;
import Utils.ConfigValues;
import Utils.FontHandler;
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

		FontHandler.changeFontName(g2, "Times New Roman");
		FontHandler.resizeFont(g2, 16);
		FontHandler.changeFontStyle(g2, Font.BOLD);

		g2.drawString("Debug info", textStartX, linePos += 2);
		FontHandler.resetFont(g2);

		FontHandler.changeFontName(g2, "Arial");
		FontHandler.resizeFont(g2, 13);
		FontHandler.changeFontStyle(g2, Font.BOLD);

		g2.drawString("FPS: " + MainFile.game.gameContainer.getFPS(), textStartX, linePos += (lineLength * 2));

		if (MainFile.game.getServer().getWorld() != null) {
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString("World Size: ", textStartX, linePos += (lineLength * 2));

			g2.drawString(" - " + (MainFile.game.getServer().getWorld().worldSize.xSize) + " blocks wide.", textStartX, linePos += lineLength);
			g2.drawString(" - " + (MainFile.game.getServer().getWorld().worldSize.ySize) + " blocks high.", textStartX, linePos += lineLength);


			g2.drawString("World info:", textStartX, linePos += (lineLength * 2));

			g2.drawString(" - World time: " + MainFile.game.getServer().getWorld().WorldTime + " / " + MainFile.game.getServer().getWorld().WorldTimeDayEnd, textStartX, linePos += (lineLength));
			g2.drawString(" - Time to next phase (" + MainFile.game.getServer().getWorld().getNextWorldTime().name + "): " + ((MainFile.game.getServer().getWorld().getNextWorldTime() == EnumWorldTime.MORNING ? EnumWorldTime.NIGHT.timeEnd : MainFile.game.getServer().getWorld().getNextWorldTime().timeBegin) - MainFile.game.getServer().getWorld().WorldTime), textStartX, linePos += (lineLength));
			g2.drawString(" - Time of day: " + MainFile.game.getServer().getWorld().worldTimeOfDay.name, textStartX, linePos += (lineLength));
			g2.drawString(" - Day number: " + MainFile.game.getServer().getWorld().WorldDay, textStartX, linePos += (lineLength));


			if(MainFile.game.getClient().getPlayer() != null && !MainFile.game.getServer().getWorld().generating) {
				g2.drawString("Player info:", textStartX, linePos += (lineLength * 2));

				g2.drawString(" - Player pos: " + MainFile.game.getClient().getPlayer().getEntityPostion(), textStartX, linePos += (lineLength));
				g2.drawString(" - Block below: " + (MainFile.game.getClient().getPlayer().getBlockBelow() != null ? MainFile.game.getClient().getPlayer().getBlockBelow().getBlockDisplayName() : null), textStartX, linePos += (lineLength));
				g2.drawString(" - Is on Ground: " + (MainFile.game.getClient().getPlayer().isOnGround), textStartX, linePos += (lineLength));
				g2.drawString(" - Blocks fallen: " + (MainFile.game.getClient().getPlayer().blocksFallen), textStartX, linePos += lineLength);
			}

		}

		g2.drawString("Block render size: " + ConfigValues.size, textStartX, linePos += (lineLength * 2));

		if (MainFile.game.getServer().getWorld() != null) {
			FontHandler.changeFontStyle(g2, Font.BOLD);
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
				BlockSelection.selectedBlock.addInfo(MainFile.game.getServer().getWorld(), BlockSelection.selectedX, BlockSelection.selectedY);
			}
		}

		FontHandler.resetFont(g2);

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
