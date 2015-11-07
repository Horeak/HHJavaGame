package Render.Renders;

import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import Utils.RenderUtil;

import javax.swing.*;
import java.awt.*;

public class WorldGenerationScreen extends AbstractWindowRender {

	String text = "Generating world.";

	@Override
	public void render( JFrame frame, Graphics2D g2 ) {
		Color c = g2.getColor();

		Rectangle rect = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

		g2.setColor(Color.black);
		g2.fill(rect);

		g2.setColor(Color.WHITE);
		RenderUtil.resizeFont(g2, 24);
		RenderUtil.changeFontStyle(g2, Font.BOLD);

		g2.drawString(text, rect.x + (rect.width / 2) - (110), rect.y + (rect.height / 2));
		RenderUtil.resetFont(g2);

		g2.drawString("Currently generating: " + MainFile.currentWorld.generationStatus.replace("-|-", " - "), rect.x + (rect.width / 2) - (170), rect.y + (rect.height / 2) + 18);


		if (text.contains("....")) {
			text = "Generating world.";
		} else {
			text = text + ".";
		}

		g2.setColor(c);
	}

	@Override
	public boolean canRender( JFrame frame ) {
		return MainFile.currentWorld.generating;
	}

	@Override
	public boolean canRenderWithGui() {
		return false;
	}
}
