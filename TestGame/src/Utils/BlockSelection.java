package Utils;

import BlockFiles.Util.Block;
import Main.MainFile;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;

public class BlockSelection {

	public static Block selectedBlock = null;
	public static int selectedX = -1, selectedY = -1;

	public static int maxRange = 8;


	public static void update( GameContainer container ) {
		Vec2d plPos = new Vec2d(Math.round(MainFile.game.getClient().getPlayer().getEntityPostion().x), Math.round(MainFile.game.getClient().getPlayer().getEntityPostion().y));

		if (container != null && container.getInput() != null) {
			Point mousePoint = new Point(Math.round(container.getInput().getMouseX()), Math.round(container.getInput().getMouseY()));

			if (mousePoint != null)
				if (mousePoint.getX() < (ConfigValues.renderXSize * ConfigValues.size)) {
					if (mousePoint.getY() < (ConfigValues.renderYSize * ConfigValues.size)) {

						//TODO This is still offset for some reason under certain circumstances
						int mouseX = ((int)mousePoint.getX()) / (ConfigValues.size);
						int mouseY = ((int)mousePoint.getY()) / (ConfigValues.size);

						int posX = Math.round((float)plPos.x);
						int posY = Math.round((float)plPos.y);

						int blockX = (mouseX + posX) - ConfigValues.renderRange;
						int blockY = (mouseY + posY) - ConfigValues.renderRange;

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
