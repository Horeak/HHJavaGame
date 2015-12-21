package Guis;

import Blocks.BlockRender.IBlockRenderer;
import Blocks.Util.Block;
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
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class GuiInventory extends Gui {

	public int startX = 140, startY = 250;
	public int Swidth = 520, Sheight = 297;

	public IItem heldItem = null;
	boolean init = false;

	public GuiInventory() {
		MainFile.gameContainer.setPaused(true);
	}

	public void init() {
		for (int i = 0; i < 10; i++) {
			guiObjects.add(new inventoryButton(startX + 10 + (i * (50)), startY + 30, this, true, i));
		}

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 4; y++) {
				guiObjects.add(new inventoryButton(startX + 10 + ((x) * (50)), startY + 40 + ((y + 1) * (50)), this, false, 10 + (x + (y * 10))));
			}
		}
	}

	@Override
	public void render( Graphics g2 ) {
		if (!init) {
			init();
			init = true;
		}
		g2.setColor(new Color(0.3F, 0.3F, 0.3F, 0.4F));
		g2.fill(MainFile.blockRenderBounds);

		Rectangle bounder = new Rectangle(startX, startY, Swidth, Sheight);

		g2.setColor(Color.gray);
		g2.fill(bounder);

		g2.setColor(Color.darkGray.darker());
		g2.draw(bounder);


		g2.setColor(Color.white);
		RenderUtil.resizeFont(g2, 12);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString(MainFile.currentWorld.player.getInventoryName(), startX + 5, startY + 5);
		RenderUtil.resetFont(g2);

	}

	public void renderPost( Graphics g2 ) {
		int mouseX = MainFile.gameContainer.getInput().getMouseX();
		int mouseY = MainFile.gameContainer.getInput().getMouseY();

		for (GuiObject ob : guiObjects) {
			if (ob instanceof inventoryButton) {
				inventoryButton button = (inventoryButton) ob;
				IItem item = MainFile.currentWorld.player.getItem(button.num);

				if (button.isMouseOver() && item != null) {
					g2.pushTransform();

					AffineTransform affinetransform = new AffineTransform();
					FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
					Font font = new Font("ARIAL", Font.BOLD, 10);

					String text = item.getItemStackSize() + "x " + item.getItemName();

					int textwidth = (int) (font.getStringBounds(text, frc).getWidth());

					Rectangle rect = new Rectangle(mouseX - 20, mouseY - 20, textwidth + 10, 20);

					g2.setColor(Color.lightGray);
					g2.fill(rect);

					g2.setColor(Color.black);
					g2.draw(rect);

					RenderUtil.resizeFont(g2, 10);
					RenderUtil.changeFontStyle(g2, Font.BOLD);

					g2.drawString(text, mouseX - 15, mouseY - 15);

					RenderUtil.resetFont(g2);

					g2.popTransform();
				}
			}
		}
		g2.pushTransform();

		g2.scale(0.5F, 0.5F);
		g2.translate(mouseX - 16, mouseY - 16);

		if (heldItem != null && heldItem.getRender() != null) {
			if (heldItem instanceof Block) {
				((IBlockRenderer) (heldItem.getRender())).renderBlock(g2, mouseX, mouseY, EnumRenderMode.render2_5D, (Block) heldItem, true, true, false, true);
			} else {
				heldItem.getRender().renderItem(g2, mouseX, mouseY, EnumRenderMode.render2_5D, heldItem);
			}
		}

		g2.scale(2, 2);
		g2.popTransform();
	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void keyPressed( int key, char c ) {
		if (key == Input.KEY_E || key == Input.KEY_ESCAPE) {
			MainFile.gameContainer.setPaused(false);
			MainFile.currentGui = null;
		}
	}


	class inventoryButton extends GuiObject {

		public int num;
		public boolean renderNum;

		public inventoryButton( int x, int y, Menu menu, boolean renderNumber, int number ) {
			super(x, y, 48, 48, menu);

			this.num = number;
			this.renderNum = renderNumber;
		}

		@Override
		public void onClicked( int button, int x, int y, Menu menu ) {

			try {

				//TODO Improve system with .clone() to prevent item duplication glitch where items thinks it is the same instance as another and changes to stacksize happens to both
				//TODO Create a inventoryhandler util so that i dont have to make this code for every inventory
				if (button == Input.MOUSE_LEFT_BUTTON) {
					//TODO Add case if it needs to increase the stack size
					if (heldItem == null && MainFile.currentWorld.player.getItem(num) != null) {
						heldItem = MainFile.currentWorld.player.getItem(num).clone();
						MainFile.currentWorld.player.setItem(num, null);

					} else if (heldItem != null && MainFile.currentWorld.player.getItem(num) == null) {
						MainFile.currentWorld.player.setItem(num, heldItem);
						heldItem = null;

					} else if (heldItem != null && MainFile.currentWorld.player.getItem(num) != null && heldItem.getItemID().equals(MainFile.currentWorld.player.getItem(num).getItemID()) && MainFile.currentWorld.player.getItem(num).getItemStackSize() < MainFile.currentWorld.player.getItem(num).getItemMaxStackSize()) {
						int t = MainFile.currentWorld.player.getItem(num).getItemMaxStackSize() - MainFile.currentWorld.player.getItem(num).getItemStackSize();
						int tt = t - heldItem.getItemStackSize();

						if (tt > 0) {
							if (MainFile.currentWorld.player.getItem(num).getItemMaxStackSize() - tt > MainFile.currentWorld.player.getItem(num).getItemMaxStackSize()) {
								MainFile.currentWorld.player.getItem(num).setStackSize(MainFile.currentWorld.player.getItem(num).getItemMaxStackSize());
							} else {
								MainFile.currentWorld.player.getItem(num).setStackSize(MainFile.currentWorld.player.getItem(num).getItemMaxStackSize() - tt);
							}

							heldItem = null;
						} else {
							heldItem.setStackSize(heldItem.getItemStackSize() - t);
							MainFile.currentWorld.player.getItem(num).setStackSize(MainFile.currentWorld.player.getItem(num).getItemMaxStackSize() - tt);

							if (heldItem.getItemStackSize() == 0) {
								heldItem = null;
							}
						}

					}
				} else if (button == Input.MOUSE_RIGHT_BUTTON) {
					if (heldItem == null && MainFile.currentWorld.player.getItem(num) != null) {
						heldItem = MainFile.currentWorld.player.getItem(num).clone();

						int t = heldItem.getItemStackSize();
						int tj = t / 2;

						if (heldItem.getItemStackSize() == 1) {
							heldItem = MainFile.currentWorld.player.getItem(num).clone();
							MainFile.currentWorld.player.setItem(num, null);
						} else {
							heldItem.decreaseStackSize(tj);
							MainFile.currentWorld.player.setItem(num, heldItem);
						}

					} else if (heldItem != null && MainFile.currentWorld.player.getItem(num) == null) {
						int t = heldItem.getItemStackSize();
						int tj = t / 2;

						if (heldItem.getItemStackSize() == 1) {
							MainFile.currentWorld.player.setItem(num, heldItem);
							heldItem = null;
						} else {
							heldItem.decreaseStackSize(tj);
							MainFile.currentWorld.player.setItem(num, heldItem);
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
				g2.translate(tangle.getX() + 32, tangle.getY() + 32);

				if (item != null && item.getRender() != null) {
					if (item instanceof Block) {
						((IBlockRenderer) (item.getRender())).renderBlock(g2, (int) tangle.getX(), (int) tangle.getY(), EnumRenderMode.render2_5D, (Block) item, true, true, false, true);
					} else {
						item.getRender().renderItem(g2, (int) tangle.getX(), (int) tangle.getY(), EnumRenderMode.render2_5D, item);
					}
				}

				g2.scale(2, 2);
				g2.popTransform();

				g2.setColor(Color.black);
				RenderUtil.resizeFont(g2, 10);

				FontUtils.drawCenter(g2.getFont(), item.getItemStackSize() + "x", x, y + 20, 20, g2.getColor());

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
}
