package Render.Renders;

import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;


public class WorldGenerationScreen extends AbstractWindowRender {

	public static String generationStatus = "";
	String text = ".";
	int tt = 0;

	@Override
	public void render( Graphics g2 ) {
		Color c = g2.getColor();

		g2.setColor(Color.black);
		g2.fill(MainFile.blockRenderBounds);

		g2.setColor(Color.white);
		RenderUtil.resizeFont(g2, 24);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		FontUtils.drawCenter(g2.getFont(), "Generating world", 0, (MainFile.yWindowSize / 2) - 10, MainFile.xWindowSize, g2.getColor());
		FontUtils.drawLeft(g2.getFont(), text, (MainFile.xWindowSize / 2) + 100, (MainFile.yWindowSize / 2) - 10);
		RenderUtil.resetFont(g2);


		g2.setColor(Color.white);
		RenderUtil.resizeFont(g2, 16);
		FontUtils.drawCenter(g2.getFont(), "Currently generating: " + generationStatus.replace("-|-", " - "), 0, (MainFile.yWindowSize / 2) + 25, MainFile.xWindowSize, g2.getColor());
		RenderUtil.resetFont(g2);

		if (tt >= 10) {
			tt = 0;

			if (text.contains(".....")) {
				text = ".";
			} else {
				text = text + ".";
			}
		} else {
			tt += 1;
		}

		g2.setColor(c);
	}

	@Override
	public boolean canRender() {
		return MainFile.getServer().getWorld().generating;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}
}
