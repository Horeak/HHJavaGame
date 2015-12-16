package Render.Renders;

import Blocks.BlockDirt;
import Blocks.BlockGrass;
import Blocks.BlockRender.BlockRenderer;
import Blocks.BlockStone;
import Blocks.Util.Block;
import Interface.Gui;
import Interface.Objects.GuiButton;
import Items.Item;
import Render.AbstractWindowRender;
import Render.EnumRenderMode;
import Utils.ConfigValues;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;
import java.util.ArrayList;

public class HotbarRender extends AbstractWindowRender {

	public static int slotSelected = 1;
	public static Item[] blocks = new Item[]{ new BlockGrass(), new BlockDirt(), new BlockStone() };

	public static ArrayList<hotbarButton> hotbarButtons = new ArrayList<>();

	int startX = 10;
	int startY = BlockRendering.START_Y_POS + (ConfigValues.renderYSize * ConfigValues.size) + (BlockRendering.START_Y_POS / 2);

	int size = 75;

	public HotbarRender() {
		for (int i = 0; i < 10; i++) {
			hotbarButtons.add(new hotbarButton(startX + ((size + 7) * i) + 2, startY + 2, null, i, i < blocks.length ? blocks[ i ] : null));
		}
	}

	@Override
	public void render( org.newdawn.slick.Graphics g2 ) {
		Color temp = g2.getColor();
		Rectangle c = g2.getClip();

		//TODO Remove debug feature
		Rectangle rect = new Rectangle(startX, startY, startX + (size * 10) + (size / 1.3F), ConfigValues.hotbarRenderSize + 4);
		g2.setClip(rect);

		g2.setColor(Color.gray);
		g2.fill(rect);

		g2.setColor(Color.black);
		g2.draw(new Rectangle(startX + 1, startY + 1, startX + (size * 10) + (size / 1.3F) - 2, ConfigValues.hotbarRenderSize + 2));

		for (hotbarButton bt : hotbarButtons) {
			bt.renderObject(g2, null);
		}

		g2.setColor(temp);
		g2.setClip(c);
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_HOTBAR;
	}
	@Override
	public boolean canRenderWithGui() {
		return true;
	}

	public void mouseClick( int button, int x, int y ) {
		for (hotbarButton bt : hotbarButtons) {
			if (bt.isMouseOver()) {
				bt.onClicked(button, x, y, null);
			}
		}

	}


	class hotbarButton extends GuiButton {
		int num;
		Item item;

		public hotbarButton( int x, int y, Gui gui, int num, Item item ) {
			super(x, y, size, size, "", gui);

			this.num = num;
			this.item = item;
		}

		@Override
		public void onClicked( int button, int x, int y, Gui gui ) {
			slotSelected = num + 1;
		}

		@Override
		public void renderObject( org.newdawn.slick.Graphics g2, Gui gui ) {
			boolean selected = (num + 1) == slotSelected;

			g2.setColor(Color.darkGray);
			if (selected) g2.setColor(Color.gray);
			if (isMouseOver()) g2.setColor(Color.lightGray);

			Rectangle rectt = new Rectangle(x, y, width, height);
			g2.fill(rectt);

			g2.setColor(Color.orange);
			if (isMouseOver()) g2.setColor(Color.red);
			g2.draw(rectt);

			g2.setColor(Color.white);
			RenderUtil.resizeFont(g2, 14);

			if (selected) RenderUtil.changeFontStyle(g2, Font.BOLD);

			g2.drawString((num + 1) + "", rectt.getX() + 5, rectt.getY() + 4);
			RenderUtil.resetFont(g2);

			g2.setColor(Color.white);
			RenderUtil.resizeFont(g2, 10);

			if (item != null) {
				g2.drawString(item.getItemName(), x + 3, y + 60);
			}

			RenderUtil.resetFont(g2);

			if (item != null && item.getRender() != null) {
				if (item instanceof Block) {
					((BlockRenderer) (item.getRender())).renderBlock(g2, x + 15, y + 25, EnumRenderMode.render2_5D, (Block) item, true, true);
				} else {
					item.getRender().renderItem(g2, x + 15, y + 25, EnumRenderMode.render2_5D, item);
				}
			}
		}

	}
}
