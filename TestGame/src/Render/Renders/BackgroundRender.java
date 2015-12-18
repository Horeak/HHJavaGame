package Render.Renders;


import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

public class BackgroundRender extends AbstractWindowRender {

	public Color skyColor;

	@Override
	public void render( Graphics g2 ) {
		Color temp = g2.getColor();
		skyColor = MainFile.currentWorld.worldTimeOfDay.SkyColor;

		//TODO Make it where the last and new SkyColor merges when the time of day changes instead of instant change

		GradientFill fill = new GradientFill(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, skyColor, ConfigValues.size * ConfigValues.renderXSize, ConfigValues.size * ConfigValues.renderYSize, skyColor.brighter());
		g2.fill(new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, ConfigValues.size * ConfigValues.renderXSize, ConfigValues.size * ConfigValues.renderYSize), fill);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_BACKGROUND && !MainFile.currentWorld.generating;
	}

	@Override
	public boolean canRenderWithGui() {
		return false;
	}
}
