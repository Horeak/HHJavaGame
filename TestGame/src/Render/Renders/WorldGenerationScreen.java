package Render.Renders;

import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.*;


public class WorldGenerationScreen extends AbstractWindowRender {

	String text = "Generating world.";

	@Override
	public void render( Graphics g2 ) {
		Color c = g2.getColor();

		g2.setColor(Color.black);
		g2.fill(MainFile.blockRenderBounds);

		g2.setColor(Color.white);
		RenderUtil.resizeFont(g2, 24);
		RenderUtil.changeFontStyle(g2, Font.BOLD);

		g2.drawString(text, MainFile.blockRenderBounds.getX() + (MainFile.blockRenderBounds.getWidth() / 2) - (110), MainFile.blockRenderBounds.getY() + (MainFile.blockRenderBounds.getHeight() / 2));
		RenderUtil.resetFont(g2);

		g2.drawString("Currently generating: " + MainFile.currentWorld.generationStatus.replace("-|-", " - "), MainFile.blockRenderBounds.getX() + (MainFile.blockRenderBounds.getWidth() / 2), MainFile.blockRenderBounds.getY() + (MainFile.blockRenderBounds.getHeight() / 2) + 18);


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
