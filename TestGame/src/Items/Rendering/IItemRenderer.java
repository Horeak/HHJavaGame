package Items.Rendering;

import Items.IItem;
import Render.EnumRenderMode;
import org.newdawn.slick.Graphics;

public interface IItemRenderer {
	void renderItem( Graphics g, int rX, int rY, EnumRenderMode renderMode, IItem item );
}
