package Render.Renders;

import EntityFiles.Entity;
import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.Color;


public class EntityRendering extends AbstractWindowRender {


	@Override
	public void render( org.newdawn.slick.Graphics g2 ) {
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);

		//TODO Fix entity rendering postion (only working with intergers) or simply rewrite the system with something proper...
		//TODO Make entity rendering work with floats

		int xStart = (int) (plPos.x - ConfigValues.renderRange), xEnd = (int) (plPos.x + ConfigValues.renderRange + 1);
		int yStart = (int) (plPos.y - ConfigValues.renderRange), yEnd = (int) (plPos.y + ConfigValues.renderRange + 1);

		int renderX = 1, renderY = 1;
		for (int x = xStart; x < xEnd; x += 1) {
			for (int y = yStart; y < yEnd; y += 1) {

				for (Entity ent : MainFile.currentWorld.Entities) {

					Color c = g2.getColor();

					if ((int) ent.getEntityPostion().x == x && (int) ent.getEntityPostion().y == y) {
						ent.renderEntity(g2, (renderX - (int) (ent.getEntityPostion().x - (int) ent.getEntityPostion().x)) * ConfigValues.size, (renderY - (int) (ent.getEntityPostion().y - (int) ent.getEntityPostion().y)) * ConfigValues.size);
					}

					g2.setColor(c);
				}

				renderY += 1;
			}
			renderY = 1;
			renderX += 1;
		}
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_ENTITIES && !MainFile.currentWorld.generating;
	}

	@Override
	public boolean canRenderWithGui() {
		return false;
	}
}
