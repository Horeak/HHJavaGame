package Render.Renders;

import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import Utils.RenderUtil;
import WorldFiles.EnumWorldTime;

import javax.swing.*;
import java.awt.*;


public class DebugInfoRender extends AbstractWindowRender {

	public static int fps = 0;

	@Override
	public void render( JFrame frame, Graphics2D g2 ) {
		Color temp = g2.getColor();
		Shape c = g2.getClip();

		int xCord = (ConfigValues.renderXSize * ConfigValues.size) + (BlockRendering.START_X_POS * 2);
		int ySize = (ConfigValues.renderYSize * ConfigValues.size) + (BlockRendering.START_Y_POS * 2);

		Rectangle tangle = new Rectangle(xCord, BlockRendering.START_Y_POS, 300 - (BlockRendering.START_X_POS), ySize - (BlockRendering.START_Y_POS * 2));
		g2.setClip(tangle);

		g2.setColor(Color.DARK_GRAY);
		g2.fill(tangle);
		g2.setColor(Color.black);
		g2.draw(tangle);
		tangle.setLocation(tangle.x + 1, tangle.y + 1);
		tangle.setSize(tangle.width - 2, tangle.height - 2);
		g2.draw(tangle);
		tangle.setLocation(tangle.x + 1, tangle.y + 1);
		tangle.setSize(tangle.width - 2, tangle.height - 2);
		g2.draw(tangle);

		int textStartX = xCord + 5;
		int textStartY = BlockRendering.START_Y_POS + 15;

		int linePos = textStartY;
		int lineLength = 13;

		g2.setColor(Color.BLACK);

		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Debug info", textStartX, linePos += 2);
		RenderUtil.resetFont(g2);

		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("FPS: " + fps, textStartX, linePos += (lineLength * 2));
		RenderUtil.resetFont(g2);

		if (MainFile.currentWorld != null) {
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("World Size: ", textStartX, linePos += (lineLength * 2));
			RenderUtil.resetFont(g2);

			g2.drawString(" - " + (MainFile.currentWorld.worldSize.xSize) + " blocks wide.", textStartX, linePos += lineLength);
			g2.drawString(" - " + (MainFile.currentWorld.worldSize.ySize) + " blocks high.", textStartX, linePos += lineLength);

			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("World info:", textStartX, linePos += (lineLength * 2));
			RenderUtil.resetFont(g2);

			g2.drawString(" - World time: " + MainFile.currentWorld.WorldTime + " / " + MainFile.currentWorld.WorldTimeDayEnd, textStartX, linePos += (lineLength));
			g2.drawString(" - Time until next day phase (" + MainFile.currentWorld.getNextWorldTime().name + "): " + ((MainFile.currentWorld.getNextWorldTime() == EnumWorldTime.MORNING ? 1800 : MainFile.currentWorld.getNextWorldTime().timeBegin) - MainFile.currentWorld.WorldTime), textStartX, linePos += (lineLength));
			g2.drawString(" - Time of day: " + MainFile.currentWorld.worldTimeOfDay.name, textStartX, linePos += (lineLength));
			g2.drawString(" - Day number: " + MainFile.currentWorld.WorldDay, textStartX, linePos += (lineLength));


			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Player info:", textStartX, linePos += (lineLength * 2));
			RenderUtil.resetFont(g2);

			g2.drawString(" - Player pos: " + MainFile.currentWorld.player.getEntityPostion(), textStartX, linePos += (lineLength));
			g2.drawString(" - Block below: " + (MainFile.currentWorld.player.getBlockBelow() != null ? MainFile.currentWorld.player.getBlockBelow().getBlockDisplayName() : null), textStartX, linePos += (lineLength));

		}

		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Block render size: " + ConfigValues.size, textStartX, linePos += (lineLength * 2));
		RenderUtil.resetFont(g2);

		if (MainFile.currentWorld != null) {
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Currently selected block: " + (BlockSelectionRender.selectedBlock != null ? BlockSelectionRender.selectedBlock.getBlockDisplayName() : "None"), textStartX, linePos += (lineLength * 2));
			RenderUtil.resetFont(g2);

			if (BlockSelectionRender.selectedBlock != null) {
				g2.setColor(Color.BLACK);
				g2.drawString("Block cords: " + "[" + BlockSelectionRender.selectedBlock.x + ", " + BlockSelectionRender.selectedBlock.y + "]", textStartX, linePos += (lineLength));
				if (BlockSelectionRender.selectedBlock.blockInfoList.size() > 0) {
					g2.drawString("Blockinfo: ", textStartX, linePos += (lineLength));
					for (String t : BlockSelectionRender.selectedBlock.blockInfoList) {
						g2.drawString(" - " + t, textStartX, linePos += (lineLength));
					}
				}

				BlockSelectionRender.selectedBlock.blockInfoList.clear();
				BlockSelectionRender.selectedBlock.addInfo();
			}
		}

		g2.setColor(temp);
		g2.setClip(c);
	}


	@Override
	public boolean canRender( JFrame frame ) {
		return ConfigValues.debug;
	}

	@Override
	public boolean canRenderWithGui() {
		return true;
	}
}
