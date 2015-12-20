package Render.Renders;

import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;


public class WorldGenerationScreen extends AbstractWindowRender {

	String text = "Generating world.";

	@Override
	public void render( Graphics g2 ) {
		Color c = g2.getColor();

		Rectangle rect = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

		g2.setColor(Color.black);
		g2.fill(rect);

		g2.setColor(Color.white);
		RenderUtil.resizeFont(g2, 24);
		RenderUtil.changeFontStyle(g2, Font.BOLD);

		g2.drawString(text, rect.getX() + (rect.getWidth() / 2) - (110), rect.getY() + (rect.getHeight() / 2));
		RenderUtil.resetFont(g2);

		g2.drawString("Currently generating: " + MainFile.currentWorld.generationStatus.replace("-|-", " - "), rect.getX() + (rect.getWidth() / 2) - (170), rect.getY() + (rect.getHeight() / 2) + 18);


		if (text.contains("....")) {
			text = "Generating world.";
		} else {
			text = text + ".";
		}

		g2.setColor(c);
	}

	@Override
	public boolean canRender() {
		return MainFile.currentWorld.generating;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}
}
