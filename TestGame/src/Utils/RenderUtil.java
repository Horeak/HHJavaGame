package Utils;
/*
* Project: Random Java Creations
* Package: Utils
* Created: 26.07.2015
*/


import BlockFiles.BlockRender.IBlockRenderer;
import Items.Utils.ItemStack;
import Main.MainFile;
import Render.EnumRenderMode;
import org.newdawn.slick.Graphics;

public class RenderUtil {
	public static org.newdawn.slick.Color getColorWithAlpha( org.newdawn.slick.Color c, float alpha ) {
		return new org.newdawn.slick.Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}

	//Optimise item rendering
	public static void renderItem(Graphics g2, ItemStack item, int x, int y, EnumRenderMode mode ) {
		if (item != null && item.getItem().getRender() != null) {
			if (item.isBlock()) {
				for(int i = (ConfigValues.renderMod == EnumRenderMode.render2D || ConfigValues.simpleBlockRender ? 2 : 0); i < 3; i++)
				((IBlockRenderer)(item.getBlock().getRender())).renderBlock(g2, x, y, mode, item.getBlock(), true, true, false, true, MainFile.game.getServer().getWorld(), 0,0, i);
			} else {
				item.getItem().getRender().renderItem(g2, x, y, mode, item);
			}
		}
	}

}
