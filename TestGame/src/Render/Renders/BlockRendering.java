package Render.Renders;

import Blocks.Util.Block;
import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;

import java.text.DecimalFormat;


public class BlockRendering extends AbstractWindowRender {

	public static final int START_X_POS = 0; //23
	public static final int START_Y_POS = 0; //23

	@Override
	public void render( org.newdawn.slick.Graphics g2 ) {
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);

		org.newdawn.slick.geom.Rectangle c = g2.getClip();
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

							block.getRender().renderItem(g2, START_X_POS + (int) ((renderX - (plPos.x - (int) plPos.x)) * ConfigValues.size), START_Y_POS + (int) ((renderY - (plPos.y - (int) plPos.y)) * ConfigValues.size), ConfigValues.renderMod, block);
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
	public boolean canRender() {
		return ConfigValues.RENDER_BLOCKS && !MainFile.currentWorld.generating;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}
}
