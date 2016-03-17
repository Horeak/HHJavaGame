package Guis.Button;

import EntityFiles.Entities.EntityPlayer;
import Guis.GuiGame;
import Interface.GuiObject;
import Interface.UIMenu;
import Items.Utils.IInventory;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.FontHandler;
import Utils.LoggerUtil;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class InventoryButton extends GuiObject {

	public int num;
	public boolean renderNum;
	public GuiGame gui;
	public IInventory inv;

	public int xSize, ySize;

	public InventoryButton( GuiGame gui, int x, int y, boolean renderNumber, int number, IInventory inventory ) {
		this(gui, x, y, 48, 48, renderNumber, number, inventory);
	}

	public InventoryButton( GuiGame gui, int x, int y, int xSize, int ySize, boolean renderNumber, int number, IInventory inventory ) {
		super(MainFile.game,x, y, xSize, ySize, gui);

		this.num = number;
		this.renderNum = renderNumber;
		this.gui = gui;
		this.inv = inventory;

		this.xSize = xSize;
		this.ySize = ySize;
	}

	public boolean validItem(ItemStack stack){
		return true;
	}

	public static int addItem( GuiGame gui, int slot, IInventory inv ) throws Exception {
		if (gui.heldItem == null && inv.getItem(slot) != null) {
			gui.heldItem = new ItemStack(inv.getItem(slot));
			inv.setItem(slot, null);

		} else if (gui.heldItem != null && inv.getItem(slot) == null) {
			inv.setItem(slot, gui.heldItem);
			return 0;

		} else if (gui.heldItem != null && inv.getItem(slot) != null && gui.heldItem.equals(inv.getItem(slot)) && inv.getItem(slot).getStackSize() < inv.getItem(slot).getMaxStackSize()) {
			int t = inv.getItem(slot).getMaxStackSize() - inv.getItem(slot).getStackSize();
			int tt = t - gui.heldItem.getStackSize();

			if (tt > 0) {
				if (inv.getItem(slot).getMaxStackSize() - tt > inv.getItem(slot).getMaxStackSize()) {
					inv.getItem(slot).setStackSize(inv.getItem(slot).getMaxStackSize());
				} else {
					inv.getItem(slot).setStackSize(inv.getItem(slot).getMaxStackSize() - tt);
				}

				return 0;
			} else {
				gui.heldItem.setStackSize(gui.heldItem.getStackSize() - t);
				inv.getItem(slot).setStackSize(inv.getItem(slot).getMaxStackSize() - tt);

				if (gui.heldItem.getStackSize() == 0) {
					return 0;
				}
			}

		}else if(gui.heldItem != null && inv.getItem(slot) != null && !gui.heldItem.equals(inv.getItem(slot))){
			ItemStack stack = inv.getItem(slot);
			inv.setItem(slot, gui.heldItem);
			gui.heldItem = new ItemStack(stack);
		}

		return gui.heldItem != null ? gui.heldItem.getStackSize() : 0;
	}

	@Override
	public void onClicked( int button, int x, int y, UIMenu menu ) {
		try {
			//TODO Create a inventoryhandler util so that i dont have to make this code for every inventory
			if (button == Input.MOUSE_LEFT_BUTTON) {
				try {
					if(validItem(gui.heldItem) || gui.heldItem == null) {
						int tt = addItem(gui, num, inv);

						if (tt > 0) {
							gui.heldItem.setStackSize(tt);
						} else {
							gui.heldItem = null;
						}
					}

				} catch (Exception e) {
					LoggerUtil.exception(e);
				}

			} else if (button == Input.MOUSE_RIGHT_BUTTON) {
				if (gui.heldItem == null && inv.getItem(num) != null) {
					gui.heldItem = new ItemStack(inv.getItem(num));

					int t = gui.heldItem.getStackSize();
					int tj = t / 2;

					if (gui.heldItem.getStackSize() == 1) {
						gui.heldItem = new ItemStack(inv.getItem(num));
						inv.setItem(num, null);
					} else {
						gui.heldItem.decreaseStackSize(tj);
						inv.getItem(num).setStackSize(tj);
					}

				} else if (gui.heldItem != null && inv.getItem(num) == null) {
					int t = gui.heldItem.getStackSize();
					int tj = 1;

					if (gui.heldItem.getStackSize() == 1) {
						inv.setItem(num, gui.heldItem);
						gui.heldItem = null;
					} else {
						gui.heldItem.setStackSize(tj);
						inv.setItem(num, new ItemStack(gui.heldItem));
						gui.heldItem.setStackSize(t-1);
					}

				}

			}

		} catch (CloneNotSupportedException e) {
			LoggerUtil.exception(e);
		}
	}

	@Override
	public void renderObject( Graphics g2, UIMenu menu ) {
		ItemStack item = inv.getItem(num);
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

		tangle.setSize(width - 4, height - 4);
		tangle.setLocation(tangle.getX() + 2, tangle.getY() + 2);

		g2.setColor(Color.lightGray.brighter());
		g2.fill(tangle);

		g2.setColor(Color.black);
		g2.draw(tangle);

		if (item != null && item.getItem() != null) {
			g2.pushTransform();

			//TODO Need to scale to fit button size
			g2.scale(0.5F, 0.5F);
			g2.translate(tangle.getMinX(), tangle.getMaxY());

			RenderUtil.renderItem(g2, item, (int) tangle.getCenterX(), y, item.getItem().getRenderMode());

			g2.scale(2, 2);
			g2.popTransform();

			g2.setColor(Color.black);
			FontHandler.resizeFont(g2, 12);

			org.newdawn.slick.util.FontUtils.drawRight(g2.getFont(), item.getStackSize() + "x", x, (int)tangle.getMaxY() - 14, (int)(tangle.getWidth() - 2), g2.getColor());

			FontHandler.resetFont(g2);

		}

		if (renderNum) {
			g2.setColor(Color.black);

			FontHandler.resizeFont(g2, 12);
			FontHandler.changeFontStyle(g2, Font.BOLD);

			g2.drawString((num + 1) + "", x + 5, y + 5);

			FontHandler.resetFont(g2);
		}

	}
}