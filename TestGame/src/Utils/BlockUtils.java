package Utils;
/*
* Project: Random Java Creations
* Package: Utils
* Created: 26.07.2015
*/

import Blocks.Util.Block;
import Main.MainFile;

import java.awt.*;

public class BlockUtils
{
	public static void renderDefaultBlockDebug(Graphics2D g2, Block block, int x, int y){
		g2.setColor(block.getDefaultBlockColor());
		g2.fill(new Rectangle(x + 1, y + 1, ConfigValues.size - 1, ConfigValues.size - 1));

		g2.setColor(Color.black);
		g2.drawRect(x, y, ConfigValues.size, ConfigValues.size);

	}

	public static boolean canPlaceBlockAt(Block block, int x, int y){
		if (x != -1 && y != -1 && x < MainFile.currentWorld.worldSize.xSize && y < MainFile.currentWorld.worldSize.ySize) {
			if(!MainFile.currentWorld.player.getPlayerBounds().contains(x, y) || block == null) {

				return true;
			}
		}

		return false;
	}

}
