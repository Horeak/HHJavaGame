package Utils;
/*
* Project: Random Java Creations
* Package: Utils
* Created: 26.07.2015
*/

import Blocks.Util.Block;
import Main.MainFile;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class BlockUtils {
	public static void renderDefaultBlockDebug( Graphics g2, Block block, int x, int y ) {
		g2.setColor(block.getDefaultBlockColor());
		g2.fill(new Rectangle(x + 1, y + 1, ConfigValues.size - 1, ConfigValues.size - 1));

		g2.setColor(Color.black.black);
		g2.drawRect(x, y, ConfigValues.size, ConfigValues.size);

	}

	public static boolean canPlaceBlockAt( Block block, int x, int y ) {
		if (x != -1 && y != -1 && x < MainFile.getServer().getWorld().worldSize.xSize && y < MainFile.getServer().getWorld().worldSize.ySize) {
			boolean t = (int) MainFile.getClient().getPlayer().getEntityPostion().x != x || (int) MainFile.getClient().getPlayer().getEntityPostion().y != y;

			if (!MainFile.getClient().getPlayer().getPlayerBounds().contains(x, y) && t || block == null) {
				return MainFile.getServer().getWorld().getBlock(x, y) == null;
			}
		}

		return false;
	}

}
