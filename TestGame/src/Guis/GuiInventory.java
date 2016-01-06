package Guis;

import Guis.Button.InventoryButton;
import Interface.GuiObject;
import Items.IItem;
import Main.MainFile;
import Settings.Config;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class GuiInventory extends Gui {

	public int startX = 140, startY = 250;
	public int Swidth = 520, Sheight = 297;

	boolean init = false;

	public void init() {
		for (int i = 0; i < 10; i++) {
			guiObjects.add(new InventoryButton(this, startX + 10 + (i * (50)), startY + 30, true, i));
		}

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 4; y++) {
				guiObjects.add(new InventoryButton(this, startX + 10 + ((x) * (50)), startY + 40 + ((y + 1) * (50)), false, 10 + (x + (y * 10))));
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
			if (ob instanceof InventoryButton) {
				InventoryButton button = (InventoryButton) ob;
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

		if(heldItem != null)
		RenderUtil.renderItem(g2, heldItem, mouseX, mouseY, heldItem.getRenderMode());

		g2.scale(2, 2);
		g2.popTransform();
	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void keyPressed( int key, char c ) {
		if (key == Config.getKeybindFromID("inventory").getKey() || key == Config.getKeybindFromID("exit").getKey()) {
			closeGui();
		}
	}

}
