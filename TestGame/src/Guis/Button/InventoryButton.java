package Guis.Button;

import EntityFiles.Entities.EntityPlayer;
import Guis.Gui;
import Interface.GuiObject;
import Interface.Menu;
import Items.Utils.ItemStack;
import Main.MainFile;
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
			gui.heldItem = new ItemStack(player.getItem(slot));
			player.setItem(slot, null);

		} else if (gui.heldItem != null && player.getItem(slot) == null) {

			player.setItem(slot, gui.heldItem);
			return 0;

		} else if (gui.heldItem != null && player.getItem(slot) != null && gui.heldItem.equals(player.getItem(slot)) && player.getItem(slot).getStackSize() < player.getItem(slot).getMaxStackSize()) {
			int t = player.getItem(slot).getMaxStackSize() - MainFile.getClient().getPlayer().getItem(slot).getStackSize();
			int tt = t - gui.heldItem.getStackSize();

			if (tt > 0) {
				if (player.getItem(slot).getMaxStackSize() - tt > player.getItem(slot).getMaxStackSize()) {
					player.getItem(slot).setStackSize(player.getItem(slot).getMaxStackSize());
				} else {
					player.getItem(slot).setStackSize(player.getItem(slot).getMaxStackSize() - tt);
				}

				return 0;
			} else {
				gui.heldItem.setStackSize(gui.heldItem.getStackSize() - t);
				player.getItem(slot).setStackSize(player.getItem(slot).getMaxStackSize() - tt);

				if (gui.heldItem.getStackSize() == 0) {
					return 0;
				}
			}

		}else if(gui.heldItem != null && player.getItem(slot) != null && !gui.heldItem.equals(player.getItem(slot))){
			ItemStack stack = player.getItem(slot);
			player.setItem(slot, gui.heldItem);
			gui.heldItem = new ItemStack(stack);
		}

		return gui.heldItem != null ? gui.heldItem.getStackSize() : 0;
	}

	@Override
	public void onClicked( int button, int x, int y, Menu menu ) {

		try {

			//TODO Create a inventoryhandler util so that i dont have to make this code for every inventory
			if (button == Input.MOUSE_LEFT_BUTTON) {
				try {
					int tt = addItem(gui, num, MainFile.getClient().getPlayer());

					if (tt > 0) {
						gui.heldItem.setStackSize(tt);
					} else {
						gui.heldItem = null;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

//TODO Make right click with held item put one item from heldItem to empty slot

			} else if (button == Input.MOUSE_RIGHT_BUTTON) {
				if (gui.heldItem == null && MainFile.getClient().getPlayer().getItem(num) != null) {
					gui.heldItem = new ItemStack(MainFile.getClient().getPlayer().getItem(num));

					int t = gui.heldItem.getStackSize();
					int tj = t / 2;

					if (gui.heldItem.getStackSize() == 1) {
						gui.heldItem = new ItemStack(MainFile.getClient().getPlayer().getItem(num));
						MainFile.getClient().getPlayer().setItem(num, null);
					} else {
						gui.heldItem.decreaseStackSize(tj);
						MainFile.getClient().getPlayer().getItem(num).setStackSize(tj);
					}

				} else if (gui.heldItem != null && MainFile.getClient().getPlayer().getItem(num) == null) {
					int t = gui.heldItem.getStackSize();
					int tj = 1;

					if (gui.heldItem.getStackSize() == 1) {
						MainFile.getClient().getPlayer().setItem(num, gui.heldItem);
						gui.heldItem = null;
					} else {
						gui.heldItem.setStackSize(tj);
						MainFile.getClient().getPlayer().setItem(num, new ItemStack(gui.heldItem));
						gui.heldItem.setStackSize(t-1);
					}

				}

			}

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void renderObject( Graphics g2, Menu menu ) {
		ItemStack item = MainFile.getClient().getPlayer().getItem(num);
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

			RenderUtil.renderItem(g2, item, (int) tangle.getX(), (int) tangle.getY(), item.getItem().getRenderMode());

			g2.scale(2, 2);
			g2.popTransform();

			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 12);

			FontUtils.drawRight(g2.getFont(), item.getStackSize() + "x", x, y + 30, 40, g2.getColor());

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