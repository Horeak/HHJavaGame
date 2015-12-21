package Render.Renders;

import Blocks.Util.Block;
import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;


public class BlockRendering extends AbstractWindowRender {

	public static final int START_X_POS = 0; //23
	public static final int START_Y_POS = 0; //23

	@Override
	public void render( org.newdawn.slick.Graphics g2 ) {
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);

		org.newdawn.slick.geom.Rectangle c = g2.getClip();
		g2.setClip(MainFile.blockRenderBounds);

		for (int x = -(ConfigValues.renderRange + 2); x < (ConfigValues.renderRange + 2); x++) {
			for (int y = -(ConfigValues.renderRange + 2); y < (ConfigValues.renderRange + 2); y++) {

				int xx = (int) (x + plPos.x);
				int yy = (int) (y + plPos.y);

				float blockX = (float) (((xx) - plPos.x) + ConfigValues.renderRange);
				float blockY = (float) (((yy) - plPos.y) + ConfigValues.renderRange);

				if (MainFile.currentWorld.getBlock(xx, yy) != null) {
					Block block = MainFile.currentWorld.getBlock(xx, yy);
					block.getRender().renderItem(g2, START_X_POS + (int) ((blockX) * ConfigValues.size), START_Y_POS + (int) ((blockY) * ConfigValues.size), ConfigValues.renderMod, block);
				}

			}
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
