package Items.Rendering;

import Items.Utils.ItemStack;
import Render.EnumRenderMode;
import org.newdawn.slick.Graphics;

public interface IItemRenderer {
	void renderItem( Graphics g, int rX, int rY, EnumRenderMode renderMode, ItemStack item );
}
