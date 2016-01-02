package Guis.Button;

import EntityFiles.Entities.EntityPlayer;
import Guis.Gui;
import Interface.GuiObject;
import Interface.Menu;
import Items.IItem;
import Main.MainFile;
import Render.EnumRenderMode;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;

public class InventoryButton extends GuiObject {

	public int num;
	public boolean renderNum;
	public Gui gui;

	public InventoryButton( Gui gui, int x, int y, boolean renderNumber, int number ) {
		super(x, y, 48, 48, gui);

		this.num = number;
		this.renderNum = renderNumber;
		this.gui = gui;
	}

	public static int addItem( Gui gui, int slot, EntityPlayer player ) throws Exception {
		if (gui.heldItem == null && player.getItem(slot) != null) {
			gui.heldItem = player.getItem(slot).clone();
			player.setItem(slot, null);

		} else if (gui.heldItem != null && player.getItem(slot) == null) {
			player.setItem(slot, gui.heldItem);
			return 0;

		} else if (gui.heldItem != null && player.getItem(slot) != null && gui.heldItem.getItemID().equals(player.getItem(slot).getItemID()) && player.getItem(slot).getItemStackSize() < player.getItem(slot).getItemMaxStackSize()) {
			int t = player.getItem(slot).getItemMaxStackSize() - MainFile.currentWorld.player.getItem(slot).getItemStackSize();
			int tt = t - gui.heldItem.getItemStackSize();

			if (tt > 0) {
				if (player.getItem(slot).getItemMaxStackSize() - tt > player.getItem(slot).getItemMaxStackSize()) {
					player.getItem(slot).setStackSize(player.getItem(slot).getItemMaxStackSize());
				} else {
					player.getItem(slot).setStackSize(player.getItem(slot).getItemMaxStackSize() - tt);
				}

				return 0;
			} else {
				gui.heldItem.setStackSize(gui.heldItem.getItemStackSize() - t);
				player.getItem(slot).setStackSize(player.getItem(slot).getItemMaxStackSize() - tt);

				if (gui.heldItem.getItemStackSize() == 0) {
					return 0;
				}
			}

		}

		return gui.heldItem != null ? gui.heldItem.getItemStackSize() : 0;
	}

	@Override
	public void onClicked( int button, int x, int y, Menu menu ) {

		try {

			//TODO Create a inventoryhandler util so that i dont have to make this code for every inventory
			if (button == Input.MOUSE_LEFT_BUTTON) {
				try {
					int tt = addItem(gui, num, MainFile.currentWorld.player);

					if (tt > 0) {
						gui.heldItem.setStackSize(tt);
					} else {
						gui.heldItem = null;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (button == Input.MOUSE_RIGHT_BUTTON) {
				if (gui.heldItem == null && MainFile.currentWorld.player.getItem(num) != null) {
					gui.heldItem = MainFile.currentWorld.player.getItem(num).clone();

					int t = gui.heldItem.getItemStackSize();
					int tj = t / 2;

					if (gui.heldItem.getItemStackSize() == 1) {
						gui.heldItem = MainFile.currentWorld.player.getItem(num).clone();
						MainFile.currentWorld.player.setItem(num, null);
					} else {
						gui.heldItem.decreaseStackSize(tj);
						MainFile.currentWorld.player.getItem(num).setStackSize(tj);
					}

				} else if (gui.heldItem != null && MainFile.currentWorld.player.getItem(num) == null) {
					int t = gui.heldItem.getItemStackSize();
					int tj = t / 2;

					if (gui.heldItem.getItemStackSize() == 1) {
						MainFile.currentWorld.player.setItem(num, gui.heldItem);
						gui.heldItem = null;
					} else {
						gui.heldItem.decreaseStackSize(tj);
						MainFile.currentWorld.player.setItem(num, gui.heldItem.clone());
						gui.heldItem.setStackSize(tj);
					}

				}

			}

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void renderObject( Graphics g2, Menu menu ) {
		IItem item = MainFile.currentWorld.player.getItem(num);
		Rectangle tangle = new Rectangle(x, y, width, height);

		tangle.setLocation(x - 2, y - 1);
		tangle.setSize(width + 3, height + 3);

		g2.setColor(Color.black);
		g2.fill(tangle);

		tangle = new Rectangle(x, y, width, height);

		g2.setColor(Color.darkGray);
		g2.fill(tangle);

		g2.setColor(Color.black);
		g2.draw(tangle);

		tangle.setSize(44, 44);
		tangle.setLocation(tangle.getX() + 2, tangle.getY() + 2);

		g2.setColor(Color.lightGray);
		g2.fill(tangle);

		g2.setColor(Color.black);
		g2.draw(tangle);

		if (item != null) {
			g2.pushTransform();

			g2.scale(0.5F, 0.5F);
			g2.translate(tangle.getX() + 20, tangle.getY() + 40);

			RenderUtil.renderItem(g2, item, (int) tangle.getX(), (int) tangle.getY(), EnumRenderMode.render2_5D);

			g2.scale(2, 2);
			g2.popTransform();

			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 12);

			FontUtils.drawRight(g2.getFont(), item.getItemStackSize() + "x", x, y + 30, 40, g2.getColor());

			RenderUtil.resetFont(g2);

		}

		if (renderNum) {
			g2.setColor(Color.black);

			RenderUtil.resizeFont(g2, 12);
			RenderUtil.changeFontStyle(g2, Font.BOLD);

			g2.drawString((num + 1) + "", x + 5, y + 5);

			RenderUtil.resetFont(g2);
		}

	}
}