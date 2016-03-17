package Render.Renders;

import Guis.Objects.GuiButton;
import Interface.UIMenu;
import Items.Utils.ItemStack;
import Main.MainFile;
import Rendering.AbstractWindowRender;
import Utils.ConfigValues;
import Utils.FontHandler;
import Utils.RenderUtil;
import com.sun.deploy.config.Config;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;
import java.awt.Font;
import java.util.ArrayList;

public class HotbarRender extends AbstractWindowRender {


	//TODO Make hotbar an texture
	public static int slotSelected = 1;

	public static ArrayList<hotbarButton> hotbarButtons = new ArrayList<>();
	static float alpha = 0.5F;
	int size = 50;
	int startX = 10;
	int startY = 0;

	public static org.newdawn.slick.Image hotbarImage;

	@Override
	public void loadTextures() {
		hotbarImage = MainFile.game.imageLoader.getImage("ui", "hotbar");
	}

	@Override
	public void render( org.newdawn.slick.Graphics g2 ) {
		Color temp = g2.getColor();
		hotbarButtons.clear();

		for (int i = 0; i < 10; i++) {
			hotbarButtons.add(new hotbarButton(startX + ((size + 7) * i) + 2, startY + 2, null, i));
		}

		startY = (int)MainFile.blockRenderBounds.getHeight() - (size + 10);

		if (MainFile.game.getServer().getWorld() != null && MainFile.game.getClient().getPlayer() != null) {
			for (hotbarButton bt : hotbarButtons) {
				bt.item = MainFile.game.getClient().getPlayer().getItem(bt.num);
			}
		}

		hotbarImage.draw(startX - 2, startY - 1, startX + (size * 10) + (68), startY + ConfigValues.hotbarRenderSize + 2, 0, 0, hotbarImage.getWidth(), 55);

		for (hotbarButton bt : hotbarButtons) {
			bt.renderObject(g2, null);
		}

		ItemStack item = MainFile.game.getClient().getPlayer().getItem(slotSelected-1);

		if(item != null) {
			g2.setColor(Color.white);
			FontHandler.resizeFont(g2, 12);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString(item.getStackName(), startX, startY - 15);
			FontHandler.resetFont(g2);
		}

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_HOTBAR && MainFile.game.getCurrentMenu() == null && MainFile.game.getServer().getWorld() != null && !MainFile.game.getServer().getWorld().generating;
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
		public ItemStack item;
		int num;

		public hotbarButton( int x, int y, UIMenu menu, int num ) {
			super(MainFile.game, x, y, size, size, "", menu);

			this.num = num;
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			slotSelected = num + 1;
		}

		@Override
		public void renderObject( org.newdawn.slick.Graphics g2, UIMenu menu ) {
			boolean selected = (num + 1) == slotSelected;
			Rectangle rectt = new Rectangle(x, y, width, height);

			if(selected){
				hotbarImage.draw(x, y, x + 50, y + 50, 102, 56, 152, 106);

			}else if(isMouseOver()){
				hotbarImage.draw(x, y, x + 50, y + 50, 51, 56, 101, 106);

			}else{
				hotbarImage.draw(x, y, x + 50, y + 50, 0, 56, 50, 106);

			}

			g2.setColor(Color.white);
			FontHandler.resizeFont(g2, 14);

			if (selected) FontHandler.changeFontStyle(g2, Font.BOLD);

			g2.drawString((num + 1) + "", rectt.getX() + 5, rectt.getY() + 4);
			FontHandler.resetFont(g2);

			g2.scale(0.5F, 0.5F);

			if(item != null && item.getItem() != null)
			RenderUtil.renderItem(g2, item, (x + 14) * 2, (y + 23) * 2, item.getItem().getRenderMode());

			g2.scale(2, 2);

			g2.setColor(Color.black);
			FontHandler.resizeFont(g2, 12);

			if (item != null) {
				org.newdawn.slick.util.FontUtils.drawRight(g2.getFont(), item.getStackSize() + "x", x, y + 32, 45, g2.getColor());
			}

			FontHandler.resetFont(g2);

		}

	}
}
