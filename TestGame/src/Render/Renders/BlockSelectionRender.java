package Render.Renders;

import Main.MainFile;
import Rendering.AbstractWindowRender;
import Utils.BlockSelection;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class BlockSelectionRender extends AbstractWindowRender {

	public void loadTextures(){
		textureValid =  MainFile.game.imageLoader.getImage("textures", "blockSelectionValid");
		textureInvalid =  MainFile.game.imageLoader.getImage("textures", "blockSelectionInvalid");
	}

	public static Image textureValid =  null;
	public static Image textureInvalid =  null;

	@Override
	public void render( Graphics g2 ) {
		org.newdawn.slick.Color temp = g2.getColor();
		Vec2d plPos = new Vec2d(MainFile.game.getClient().getPlayer().getEntityPostion().x, MainFile.game.getClient().getPlayer().getEntityPostion().y);

		float mouseBlockX = (float) (BlockSelection.selectedX - plPos.x) + ConfigValues.renderRange;
		float mouseBlockY = (float) (BlockSelection.selectedY - plPos.y) + ConfigValues.renderRange;

		//TODO Is this needed?
//		boolean valid = BlockSelection.selectedX >= 0 && BlockSelection.selectedY >= 0 && BlockSelection.selectedX < MainFile.game.getServer().getWorld().worldSize.xSize && BlockSelection.selectedY < MainFile.game.getServer().getWorld().worldSize.ySize;

//		if (valid) {
			textureValid.draw(BlockRendering.START_X_POS + (int) ((mouseBlockX) * ConfigValues.size), BlockRendering.START_Y_POS + (int) ((mouseBlockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
//		} else {
//			textureInvalid.draw(BlockRendering.START_X_POS + (int) ((mouseBlockX) * ConfigValues.size), BlockRendering.START_Y_POS + (int) ((mouseBlockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
//		}

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_HOTBAR && MainFile.game.getServer().getWorld() != null && !MainFile.game.getServer().getWorld().generating;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}
}
