package Render.Renders;

import EntityFiles.Entity;
import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;


public class EntityRendering extends AbstractWindowRender {


	@Override
	public void render( org.newdawn.slick.Graphics g2 ) {
		Vec2d plPos = new Vec2d(MainFile.getClient().getPlayer().getEntityPostion().x, MainFile.getClient().getPlayer().getEntityPostion().y);
		for (Entity ent : MainFile.getServer().getWorld().Entities) {

			if (ent.getEntityPostion().distance(MainFile.getClient().getPlayer().getEntityPostion()) <= ConfigValues.renderDistance) {

				float entX = ((float) (ent.getEntityPostion().x - plPos.x) + ConfigValues.renderRange);
				float entY = ((float) (ent.getEntityPostion().y - plPos.y) + ConfigValues.renderRange) + 1;

				ent.renderEntity(g2, BlockRendering.START_X_POS + (int) ((entX) * ConfigValues.size), BlockRendering.START_Y_POS + (int) ((entY) * ConfigValues.size));
			}
		}
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_ENTITIES && !MainFile.getServer().getWorld().generating;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}
}
