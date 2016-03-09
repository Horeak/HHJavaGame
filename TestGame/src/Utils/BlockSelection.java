package Utils;

import BlockFiles.Util.Block;
import Main.MainFile;
import Render.Renders.BlockRendering;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;

public class BlockSelection {

	public static Block selectedBlock = null;
	public static int selectedX = -1, selectedY = -1;


	public static void update( GameContainer container ) {
		Vec2d plPos = new Vec2d(MainFile.game.getClient().getPlayer().getEntityPostion().x, MainFile.game.getClient().getPlayer().getEntityPostion().y);

		if (container != null && container.getInput() != null) {
			Point mousePoint = new Point(container.getInput().getMouseX(), container.getInput().getMouseY());

			if (mousePoint != null)
				if (mousePoint.getX() > BlockRendering.START_X_POS && mousePoint.getX() < (ConfigValues.renderXSize * ConfigValues.size) + BlockRendering.START_X_POS) {
					if (mousePoint.getY() > (BlockRendering.START_Y_POS) && mousePoint.getY() < (ConfigValues.renderYSize * ConfigValues.size) + BlockRendering.START_Y_POS) {

						//TODO This is still offset for some reason under certain circumstances
						int mouseX = (int) ((mousePoint.getX()) - (BlockRendering.START_X_POS)) / (ConfigValues.size);
						int mouseY = (int) ((mousePoint.getY()) - (BlockRendering.START_Y_POS)) / (ConfigValues.size);

						int blockX = (mouseX + (int) plPos.x) - ConfigValues.renderRange;
						int blockY = (mouseY + (int) plPos.y) - ConfigValues.renderRange;

						selectedX = blockX;
						selectedY = blockY;

						Block b = MainFile.game.getServer().getWorld().getBlock(selectedX, selectedY);
						selectedBlock = b;

					} else {
						selectedY = -1;
						selectedX = -1;
						selectedBlock = null;
					}
				} else {
					selectedY = -1;
					selectedX = -1;
					selectedBlock = null;
				}

		} else {
			selectedY = -1;
			selectedX = -1;
			selectedBlock = null;
		}

	}
}
