package Utils;
/*
* Project: Random Java Creations
* Package: Utils
* Created: 26.07.2015
*/

import BlockFiles.Util.Block;
import Main.MainFile;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class BlockUtils {
	public static void renderDefaultBlockDebug( Graphics g2, Block block, int x, int y ) {
		renderDefaultBlockDebug(g2, block, x, y, ConfigValues.size, ConfigValues.size);
	}

	public static void renderDefaultBlockDebug( Graphics g2, Block block, int x, int y, int sizeW, int sizeH ) {
		g2.setColor(block.getDefaultBlockColor());
		g2.fill(new Rectangle(x + 1, y + 1, sizeW - 1, sizeH - 1));

		g2.setColor(Color.black.black);
		g2.drawRect(x, y, sizeW, sizeH);
	}

	public static boolean canPlaceBlockAt( Block block, int x, int y ) {
		if (x != -1 && y != -1 && x < MainFile.game.getServer().getWorld().worldSize.xSize && y < MainFile.game.getServer().getWorld().worldSize.ySize) {
			boolean t = (int) MainFile.game.getClient().getPlayer().getEntityPostion().x != x || (int) MainFile.game.getClient().getPlayer().getEntityPostion().y != y;

			if (!MainFile.game.getClient().getPlayer().getPlayerBounds().contains(x, y) && t || block == null) {
				return MainFile.game.getServer().getWorld().getBlock(x, y) == null;
			}
		}

		return false;
	}

}
