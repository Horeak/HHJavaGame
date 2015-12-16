package Items.Rendering;

import Items.Item;
import Render.EnumRenderMode;
import org.newdawn.slick.Graphics;

public interface ItemRenderer {
	void renderItem( Graphics g, int rX, int rY, EnumRenderMode renderMode, Item item );
}
