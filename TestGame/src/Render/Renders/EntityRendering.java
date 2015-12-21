package Render.Renders;

import EntityFiles.Entity;
import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;


public class EntityRendering extends AbstractWindowRender {


	@Override
	public void render( org.newdawn.slick.Graphics g2 ) {
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);
		for (Entity ent : MainFile.currentWorld.Entities) {
			if (ent.getEntityPostion().distance(MainFile.currentWorld.player.getEntityPostion()) <= ConfigValues.renderDistance) {

				int mouseBlockX = (int) ((float) (ent.getEntityPostion().x - plPos.x) + ConfigValues.renderRange);
				int mouseBlockY = (int) ((float) (ent.getEntityPostion().y - plPos.y) + ConfigValues.renderRange) + 1;

				ent.renderEntity(g2, (mouseBlockX - (int) (ent.getEntityPostion().x - (int) ent.getEntityPostion().x)) * ConfigValues.size, (mouseBlockY - (int) (ent.getEntityPostion().y - (int) ent.getEntityPostion().y)) * ConfigValues.size);
			}
		}
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_ENTITIES && !MainFile.currentWorld.generating;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}
}
