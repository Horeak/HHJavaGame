package Render.Renders;

import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;

import javax.swing.*;
import java.awt.*;


public class BackgroundRender extends AbstractWindowRender {

	//TODO Somehow add lighting. For example make Night time darken all rendering calls unless light nearby

	public Color skyColor;

	@Override
	public void render(JFrame frame, Graphics2D g2) {
		Color temp = g2.getColor();
		skyColor = MainFile.currentWorld.worldTimeOfDay.SkyColor;

		//TODO Make it where the last and new SkyColor merges when the time of day changes instead of instant change
		g2.setPaint(new GradientPaint(new Point(BlockRendering.START_X_POS, BlockRendering.START_Y_POS),skyColor , new Point(ConfigValues.size * ConfigValues.renderXSize, ConfigValues.size * ConfigValues.renderYSize), skyColor.brighter()));
		g2.fill(new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, ConfigValues.size * ConfigValues.renderXSize, ConfigValues.size * ConfigValues.renderYSize));

		g2.setColor(temp);
	}

	@Override
	public boolean canRender(JFrame frame) {
		return ConfigValues.RENDER_BACKGROUND && !MainFile.currentWorld.generating;
	}

	@Override
	public boolean canRenderWithGui() {
		return false;
	}
}
