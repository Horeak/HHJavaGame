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
		textureSelection =  MainFile.game.imageLoader.getImage("textures", "blockSelectionValid");
	}

	public static Image textureSelection =  null;

	@Override
	public void render( Graphics g2 ) {
		org.newdawn.slick.Color temp = g2.getColor();
		Vec2d plPos = new Vec2d(MainFile.game.getClient().getPlayer().getEntityPostion().x, MainFile.game.getClient().getPlayer().getEntityPostion().y);

		float mouseBlockX = (float) (BlockSelection.selectedX - plPos.x) + ConfigValues.renderRange;
		float mouseBlockY = (float) (BlockSelection.selectedY - plPos.y) + ConfigValues.renderRange;

		textureSelection.draw((int) ((mouseBlockX) * ConfigValues.size), (int) ((mouseBlockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
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
