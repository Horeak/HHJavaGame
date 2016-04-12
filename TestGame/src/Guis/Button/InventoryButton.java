package Guis.Button;

import Guis.GuiGame;
import Guis.Objects.IInventoryGui;
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

	//TODO Make InventoryButton use a texture
	//TODO Add function to render button overlay similar to armor buttons
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

	@Override
	public void onClicked( int button, int x, int y, UIMenu menu ) {
		try {
			if(MainFile.game.gameContainer.getInput().isKeyDown(Input.KEY_LSHIFT)){
				if (button == Input.MOUSE_LEFT_BUTTON) {
					if (gui instanceof IInventoryGui) {
						if(inv.getItem(num) != null) {

							if (inv == MainFile.game.getClient().getPlayer()) {
								IInventory invv = ((IInventoryGui) gui).getInvetory();

								if (invv.addItem(inv.getItem(num))) {
									inv.setItem(num, null);
								}
							} else {
								if (MainFile.game.getClient().getPlayer().addItem(inv.getItem(num))) {
									inv.setItem(num, null);
								}
							}
						}
					}
				}
			}else {


				if (button == Input.MOUSE_LEFT_BUTTON) {
					try {
						if (gui.heldItem != null && inv.validItemForSlot(gui.heldItem, num)) {
							int tt = inv.addItemToSlot(gui.heldItem, num);

							if (tt > 0) {
								gui.heldItem.setStackSize(tt);
							} else {
								gui.heldItem = null;
							}

						} else if (gui.heldItem == null) {
							gui.heldItem = inv.getItem(num);
							inv.setItem(num, null);
						}

					} catch (Exception e) {
						LoggerUtil.exception(e);
					}

					//TODO Perhaps look at a way to improve this? (Messy code)
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
							gui.heldItem.setStackSize(t - 1);
						}

					}

				}

			}
		} catch (Exception e) {
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

			RenderUtil.renderItem(g2, item, (int) tangle.getCenterX(), y);

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

	public void renderObjectPost(Graphics g2, UIMenu menu){
		ItemStack item = inv.getItem(num);

		if (isMouseOver() && item != null) {
			int mouseX = MainFile.game.gameContainer.getInput().getMouseX();
			int mouseY = MainFile.game.gameContainer.getInput().getMouseY();

			int length = item.getTooltips() != null ? item.getTooltips().size() + 1 : 1;

			String[] tt = new String[length];

			tt[0] = item.getStackSize() + "x " + item.getStackName();

			if(item.getTooltips() != null)
				for(int j = 0; j < tt.length-1; j++){
					tt[j+1] = item.getTooltips().get(j);
				}

			gui.renderTooltip(mouseX, mouseY, tt);
		}
	}
}