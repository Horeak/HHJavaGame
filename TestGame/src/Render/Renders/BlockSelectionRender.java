package Render.Renders;

import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.BlockSelection;
import Utils.ConfigValues;
import Utils.RenderUtil;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class BlockSelectionRender extends AbstractWindowRender {
	public static Image textureValid = RenderUtil.getImage("textures", "blockSelectionValid");
	public static Image textureInvalid = RenderUtil.getImage("textures", "blockSelectionInvalid");

	@Override
	public void render( Graphics g2 ) {
		org.newdawn.slick.Color temp = g2.getColor();
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);

		float mouseBlockX = (float) (BlockSelection.selectedX - plPos.x) + ConfigValues.renderRange;
		float mouseBlockY = (float) (BlockSelection.selectedY - plPos.y) + ConfigValues.renderRange;

		boolean valid = BlockSelection.selectedX >= 0 && BlockSelection.selectedY >= 0 && BlockSelection.selectedX < MainFile.currentWorld.worldSize.xSize && BlockSelection.selectedY < MainFile.currentWorld.worldSize.ySize;

		if (valid) {
			textureValid.draw(BlockRendering.START_X_POS + (int) ((mouseBlockX) * ConfigValues.size), BlockRendering.START_Y_POS + (int) ((mouseBlockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
		} else {
			textureInvalid.draw(BlockRendering.START_X_POS + (int) ((mouseBlockX) * ConfigValues.size), BlockRendering.START_Y_POS + (int) ((mouseBlockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
		}

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_HOTBAR;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}
}
