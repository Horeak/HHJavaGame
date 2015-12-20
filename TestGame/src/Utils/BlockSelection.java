package Utils;

import Blocks.Util.Block;
import Main.MainFile;
import Render.Renders.BlockRendering;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;

public class BlockSelection {

	public static Block selectedBlock = null;
	public static int selectedX = -1, selectedY = -1;


	public static void update( GameContainer container ) {
		Vec2d plPos = new Vec2d(MainFile.currentWorld.player.getEntityPostion().x, MainFile.currentWorld.player.getEntityPostion().y);

		if (container != null && container.getInput() != null) {
			Point mousePoint = new Point(container.getInput().getMouseX(), container.getInput().getMouseY());
			if (mousePoint != null)
				if (mousePoint.getX() > BlockRendering.START_X_POS && mousePoint.getX() < (ConfigValues.renderXSize * ConfigValues.size) + BlockRendering.START_X_POS) {
					if (mousePoint.getY() > (BlockRendering.START_Y_POS) && mousePoint.getY() < (ConfigValues.renderYSize * ConfigValues.size) + BlockRendering.START_Y_POS) {

						int mouseX = (int) mousePoint.getX() - (BlockRendering.START_X_POS);
						int mouseY = (int) mousePoint.getY() - (BlockRendering.START_Y_POS + 24);

						float mouseBlockX = mouseX / (ConfigValues.size);
						float mouseBlockY = mouseY / (ConfigValues.size) + 1;

						float tempX = 0, tempY = 0;
						int renderX = 0, renderY = 0;
						boolean setX = false, setY = false;

						int xStart = (int) (MainFile.currentWorld.player.getEntityPostion().x - ConfigValues.renderRange), xEnd = (int) (MainFile.currentWorld.player.getEntityPostion().x + ConfigValues.renderRange + 1);
						int yStart = (int) (MainFile.currentWorld.player.getEntityPostion().y - ConfigValues.renderRange), yEnd = (int) (MainFile.currentWorld.player.getEntityPostion().y + ConfigValues.renderRange + 1);

						for (int x = xStart; x < xEnd; x++) {
							for (int y = yStart; y < yEnd; y++) {
								if (renderX == mouseBlockX) {
									tempX = x;
									setX = true;
								}

								if (renderY == mouseBlockY) {
									tempY = y;
									setY = true;
								}

								if (setX && setY) break;

								renderY += 1;
							}
							renderY = 0;
							renderX += 1;
						}

						float tX = (float) (plPos.x - (int) plPos.x);
						float tY = (float) (plPos.y - (int) plPos.y);

						selectedX = Math.round(tempX + tX);
						selectedY = Math.round(tempY + tY);

						Block b = MainFile.currentWorld.getBlock(selectedX, selectedY);
						selectedBlock = b;

					} else {
						selectedY = -1;
						selectedX = -1;
					}
				} else {
					selectedY = -1;
					selectedX = -1;
				}

		} else {
			selectedY = -1;
			selectedX = -1;
		}

	}
}
