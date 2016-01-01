package Render.Renders;

import Blocks.BlockRender.IBlockRenderer;
import Blocks.Util.Block;
import Interface.Menu;
import Interface.Objects.GuiButton;
import Items.IItem;
import Main.MainFile;
import Render.AbstractWindowRender;
import Render.EnumRenderMode;
import Utils.ConfigValues;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;
import java.util.ArrayList;

public class HotbarRender extends AbstractWindowRender {

	public static int slotSelected = 1;

	public static ArrayList<hotbarButton> hotbarButtons = new ArrayList<>();
	static float alpha = 0.5F;
	int size = 50;
	int startX = 10;
	int startY = MainFile.yWindowSize - (size + 10);


	public HotbarRender() {
		for (int i = 0; i < 10; i++) {
			hotbarButtons.add(new hotbarButton(startX + ((size + 7) * i) + 2, startY + 2, null, i));
		}
	}

	@Override
	public void render( org.newdawn.slick.Graphics g2 ) {
		Color temp = g2.getColor();

		if (MainFile.currentWorld != null && MainFile.currentWorld.player != null) {
			for (hotbarButton bt : hotbarButtons) {
				bt.item = MainFile.currentWorld.player.getItem(bt.num);
			}
		}

		Rectangle rect = new Rectangle(startX, startY, startX + (size * 10) + (57), ConfigValues.hotbarRenderSize);

		g2.setColor(RenderUtil.getColorWithAlpha(Color.gray, alpha));
		g2.fill(rect);

		g2.setColor(RenderUtil.getColorWithAlpha(Color.black, alpha));
		g2.draw(rect);

		for (hotbarButton bt : hotbarButtons) {
			bt.renderObject(g2, null);
		}

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_HOTBAR && MainFile.currentMenu == null;
	}
	@Override
	public boolean canRenderWithWindow() {
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
		public IItem item;
		int num;

		public hotbarButton( int x, int y, Menu menu, int num ) {
			super(x, y, size, size, "", menu);

			this.num = num;
		}

		@Override
		public void onClicked( int button, int x, int y, Menu menu ) {
			slotSelected = num + 1;
		}

		@Override
		public void renderObject( org.newdawn.slick.Graphics g2, Menu menu ) {
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

			g2.scale(0.5F, 0.5F);

			if (item != null && item.getRender() != null) {
				if (item instanceof Block) {
					((IBlockRenderer) (item.getRender())).renderBlock(g2, (x + 14) * 2, (y + 23) * 2, EnumRenderMode.render2_5D, (Block) item, true, true, false, true);
				} else {
					item.getRender().renderItem(g2, (x + 20) * 2, (y + 25) * 2, EnumRenderMode.render2_5D, item);
				}
			}

			g2.scale(2, 2);

			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 12);

			if (item != null) {
				FontUtils.drawRight(g2.getFont(), item.getItemStackSize() + "x", x, y + 32, 45, g2.getColor());
			}

			RenderUtil.resetFont(g2);

		}

	}
}
