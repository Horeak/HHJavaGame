package Guis;

import Guis.Button.ArmorInventoryButton;
import Guis.Button.InventoryButton;
import Main.MainFile;
import Utils.ConfigValues;
import Utils.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class GuiInventory extends GuiGame{

	public int startX = 140, startY = 160;
	public int Swidth = 520, Sheight = 497;

	boolean init = false;

	public GuiInventory( GameContainer container, boolean b ) {
		super(container, b);
	}

	public void init() {
		for (int i = 0; i < 10; i++) {
			guiObjects.add(new InventoryButton(this, startX + 10 + (i * (50)), startY + 30, true, i, MainFile.game.getClient().getPlayer()));
		}

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 4; y++) {
				guiObjects.add(new InventoryButton(this, startX + 10 + ((x) * (50)), startY + 40 + ((y + 1) * (50)), false, 10 + (x + (y * 10)), MainFile.game.getClient().getPlayer()));
			}
		}

		for (int i = 0; i < 4; i++) {
			guiObjects.add(new ArmorInventoryButton(this, (int)rectangle.getMaxX() + 10, (int)rectangle.getY() + (i * 50), i));
		}
	}

	final Rectangle rectangle = new Rectangle(startX + 10, startY + (50 * 6), 120, 190);

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

		g2.setColor(Color.gray.darker(0.25F));
		g2.fill(rectangle);

		g2.setColor(Color.black);
		g2.draw(rectangle);

		MainFile.game.getClient().getPlayer().renderEntity(g2, (int)rectangle.getCenterX() - (int)((float)MainFile.game.getClient().getPlayer().getEntityBounds().getWidth() * (ConfigValues.size / 2)), (int)rectangle.getCenterY() + (int)((float)MainFile.game.getClient().getPlayer().getEntityBounds().getHeight() * (ConfigValues.size / 2)));

		Rectangle tg = new Rectangle(rectangle.getMaxX() + 55, rectangle.getY(), 325, rectangle.getHeight());

		g2.setColor(Color.lightGray);
		g2.fill(tg);

		g2.setColor(Color.darkGray);
		g2.draw(tg);

		g2.setClip(tg);

		g2.setColor(Color.black);
		FontHandler.resizeFont(g2, 16);

		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Player: '" + MainFile.game.getClient().getPlayer().getEntityDisplayName() + "'", rectangle.getMaxX() + 60, rectangle.getY() + 5);

		FontHandler.changeFontStyle(g2, Font.PLAIN);
		g2.drawString("Health: " + MainFile.game.getClient().getPlayer().getEntityHealth() + "/" + MainFile.game.getClient().getPlayer().getEntityMaxHealth() + "(" + MainFile.game.getClient().getPlayer().getHealingAmount() + "+)"
				+ "\n" + "Armor: (0/0)(TODO)"
				+ "\n" + "Deaths: " + MainFile.game.getClient().getPlayer().deaths, rectangle.getMaxX() + 60, rectangle.getY() + 32);

		FontHandler.resetFont(g2);

		g2.setClip(null);

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
