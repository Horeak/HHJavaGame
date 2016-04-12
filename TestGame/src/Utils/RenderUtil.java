package Utils;


import BlockFiles.BlockRender.IBlockRenderer;
import Items.Utils.ItemStack;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class RenderUtil {
	//It creates a new color with alpha (Has to create a new instance in order to work with static colors)
	public static org.newdawn.slick.Color getColorWithAlpha( org.newdawn.slick.Color c, float alpha ) {
		org.newdawn.slick.Color cc = new Color(c.r, c.g, c.b, alpha);
		return cc;
	}

	//Optimise item rendering
	public static void renderItem(Graphics g2, ItemStack item, int x, int y ) {
		if (item != null && item.getItem().getRender() != null) {
			if (item.isBlock()) {
				for(int i = 0; i < 3; i++)
				((IBlockRenderer)(item.getBlock().getRender())).renderBlock(g2, x, y, item, null, 0,0, i);
			} else {
				item.getItem().getRender().renderItem(g2, x, y, item);
			}
		}
	}

}
