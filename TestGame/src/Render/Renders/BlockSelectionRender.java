package Render.Renders;

import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.BlockSelection;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

public class BlockSelectionRender extends AbstractWindowRender {
	@Override
	public void render( Graphics g2 ) {
		org.newdawn.slick.Color temp = g2.getColor();
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);

		float mouseBlockX = (float) (BlockSelection.selectedX - plPos.x) + ConfigValues.renderRange;
		float mouseBlockY = (float) (BlockSelection.selectedY - plPos.y) + ConfigValues.renderRange;

		if (plPos.x < 12 && plPos.x > 1) mouseBlockX -= 1;

		if (BlockSelection.selectedX >= 0 && BlockSelection.selectedY >= 0) {
			Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS + ((mouseBlockX) * ConfigValues.size), BlockRendering.START_Y_POS + ((mouseBlockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
			g2.setColor(new Color(255, 255, 255, 64));
			g2.fill(rectangle);

			g2.setColor(Color.white);
			g2.draw(rectangle);
		} else {
			Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS + ((mouseBlockX) * ConfigValues.size), BlockRendering.START_Y_POS + ((mouseBlockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
			g2.setColor(new Color(255, 0, 0, 64));
			g2.fill(rectangle);

			g2.setColor(Color.red);
			g2.draw(rectangle);

			g2.draw(new Line(rectangle.getX(), rectangle.getY(), rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()));
			g2.draw(new Line(rectangle.getX() + rectangle.getWidth(), rectangle.getY(), rectangle.getX(), rectangle.getY() + rectangle.getHeight()));

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
