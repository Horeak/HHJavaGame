package Guis;

import BlockFiles.Inventory.FurnaceInventory;
import Crafting.CraftingRegister;
import Guis.Button.InventoryButton;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.FontHandler;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class GuiChest extends GuiGame {
	public World world;
	public int x, y;

	public int startX = 140, startY = 300;
	public int Swidth = 520, Sheight = 507;

	public GuiChest( GameContainer container, boolean b, World world, int x, int y ) {
		super(container, b);

		this.world = world;
		this.x = x;
		this.y = y;

		for (int i = 0; i < 10; i++) {
			guiObjects.add(new InventoryButton(this, startX + 10 + (i * (50)), startY + 40, true, i, MainFile.game.getClient().getPlayer()));
		}

		for (int xx = 0; xx < 10; xx++) {
			for (int yy = 0; yy < 4; yy++) {
				guiObjects.add(new InventoryButton(this, startX + 10 + ((xx) * (50)), startY + 50 + ((yy + 1) * (50)), false, 10 + (xx + (yy * 10)), MainFile.game.getClient().getPlayer()));
			}
		}

		for (int yy = 0; yy < 4; yy++) {
			for (int xx = 0; xx < 5; xx++) {
				guiObjects.add(new InventoryButton(this, startX + ((Swidth / 4)) + 10 + ((xx) * (50)), startY - (230) + ((yy + 1) * (50)), false, (xx + (yy * 5)), world.getInventory(x, y)));
			}
		}

	}

	@Override
	public void render( Graphics g2 ) {
		g2.setColor(new Color(0.3F, 0.3F, 0.3F, 0.4F));
		g2.fill(MainFile.blockRenderBounds);

		Rectangle bounder = new Rectangle(startX, 100, Swidth, Sheight);

		g2.setColor(Color.gray);
		g2.fill(bounder);

		g2.setColor(Color.darkGray.darker());
		g2.draw(bounder);


		g2.setColor(Color.white);
		FontHandler.resizeFont(g2, 12);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Chest", startX + 5, 105);
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
