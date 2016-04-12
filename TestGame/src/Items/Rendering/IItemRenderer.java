package Items.Rendering;

import Items.Utils.ItemStack;
import org.newdawn.slick.Graphics;

public interface IItemRenderer {
	void renderItem( Graphics g, int rX, int rY, ItemStack item );
}
