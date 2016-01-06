package Render.Renders;

import Blocks.Util.Block;
import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;

import java.util.ArrayList;


public class BlockRendering extends AbstractWindowRender {

	public static final int START_X_POS = 0; //23
	public static final int START_Y_POS = 0; //23

	@Override
	public void render( org.newdawn.slick.Graphics g2 ) {
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);

		org.newdawn.slick.geom.Rectangle c = g2.getClip();
		g2.setClip(MainFile.blockRenderBounds);

		ArrayList<Block> b = new ArrayList<>();


		for (int x = -(ConfigValues.renderRange + 2); x < (ConfigValues.renderRange + 2); x++) {
			for (int y = -(ConfigValues.renderRange + 2); y < (ConfigValues.renderRange + 2); y++) {

				int xx = (int) (x + plPos.x);
				int yy = (int) (y + plPos.y);

				float blockX = (float) (((xx) - plPos.x) + ConfigValues.renderRange);
				float blockY = (float) (((yy) - plPos.y) + ConfigValues.renderRange);

				if (MainFile.currentWorld.getBlock(xx, yy) != null) {
					Block block = MainFile.currentWorld.getBlock(xx, yy);

					if (block.isBlockSolid()) {
						block.getRender().renderItem(g2, START_X_POS + (int) ((blockX) * ConfigValues.size), START_Y_POS + (int) ((blockY) * ConfigValues.size), block.getRenderMode(), block);
					} else {
						b.add(block);
					}
				}

			}
		}

		//TODO Fix overlay when a non-solid block is diagonally down to the left of a solid one
		//Non-solid block rendering is delayed to prevent overlay issues
		for (Block block : b) {
			float blockX = (float) (((block.x) - plPos.x) + ConfigValues.renderRange);
			float blockY = (float) (((block.y) - plPos.y) + ConfigValues.renderRange);

			block.getRender().renderItem(g2, START_X_POS + (int) ((blockX) * ConfigValues.size), START_Y_POS + (int) ((blockY) * ConfigValues.size), block.getRenderMode(), block);
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
