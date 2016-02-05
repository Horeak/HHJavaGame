package Guis;

import Guis.Button.InventoryButton;
import Main.MainFile;
import Utils.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class GuiInventory extends GuiGame {

	public int startX = 140, startY = 250;
	public int Swidth = 520, Sheight = 297;

	boolean init = false;

	public GuiInventory( GameContainer container, boolean b ) {
		super(container, b);
	}

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
		FontHandler.resizeFont(g2, 12);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString(MainFile.game.getClient().getPlayer().getInventoryName(), startX + 5, startY + 5);
		FontHandler.resetFont(g2);

		renderInventoryButtons();
	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void keyPressed( int key, char c ) {
		if (key == MainFile.game.getConfig().getKeybindFromID("inventory").getKey() || key == MainFile.game.getConfig().getKeybindFromID("exit").getKey()) {
			closeGui();
		}
	}

}
