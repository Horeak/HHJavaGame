package Render.Renders;

import Blocks.Util.Block;
import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import Utils.RenderUtil;
import com.sun.javafx.geom.Vec2d;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;


public class BlockRendering extends AbstractWindowRender {

	public static final int START_X_POS = 23;
	public static final int START_Y_POS = 23;

	//TODO Darken rendering depending on time of day/light
	//TODO Investigate the possibility of complete 3D render like MC

	//TODO Translate block location when inbetween 0.1F and 0.9F? and then use the current system for full numbers

	@Override
	public void render( JFrame frame, Graphics2D g2 ) {
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);

		Shape c = g2.getClip();
		g2.setClip(MainFile.blockRenderBounds);

		int renderDistance = ConfigValues.renderDistance + 2;

		float xStart = (int) (plPos.x - ConfigValues.renderRange), xEnd = (int) (plPos.x + ConfigValues.renderRange + 1) + 1;
		float yStart = (int) (plPos.y - ConfigValues.renderRange), yEnd = (int) (plPos.y + ConfigValues.renderRange + 1) + 2;

		int renderX = 0, renderY = 0;
		for (float x = xStart; x < xEnd; x++) {
			for (float y = yStart; y < yEnd; y++) {

				Vec2d blPos = new Vec2d(x, y);
				double distance = plPos.distance(blPos);

				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(0);

				int xx = Integer.parseInt(df.format(x).replace(",", "."));
				int yy = Integer.parseInt(df.format(y).replace(",", "."));

				if ((int) distance <= renderDistance) {
					if (renderX < (ConfigValues.renderXSize + 1) && renderY < (ConfigValues.renderYSize + 2)) {
						if (MainFile.currentWorld.getBlock(xx, yy) != null) {
							Block block = MainFile.currentWorld.getBlock(xx, yy);

							RenderUtil.renderBlock(g2, block, START_X_POS + (int) ((renderX - (plPos.x - (int) plPos.x)) * ConfigValues.size), START_Y_POS + (int) ((renderY - (plPos.y - (int) plPos.y)) * ConfigValues.size));
						}
					}
				}

				renderY += 1;
			}
			renderY = 0;
			renderX += 1;
		}

		g2.setClip(c);
	}

	@Override
	public boolean canRender( JFrame frame ) {
		return ConfigValues.RENDER_BLOCKS && !MainFile.currentWorld.generating;
	}

	@Override
	public boolean canRenderWithGui() {
		return false;
	}
}
