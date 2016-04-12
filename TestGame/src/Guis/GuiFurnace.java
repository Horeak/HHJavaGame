package Guis;

import BlockFiles.Inventory.FurnaceInventory;
import Crafting.CraftingRegister;
import Guis.Button.InventoryButton;
import Guis.Objects.IInventoryGui;
import Items.Utils.IInventory;
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

public class GuiFurnace extends GuiGame implements IInventoryGui{
	public World world;
	public int x, y;

	public int startX = 140, startY = 300;
	public int Swidth = 520, Sheight = 497;

	public GuiFurnace( GameContainer container, boolean b, World world, int x, int y ) {
		super(container, b);

		this.world = world;
		this.x = x;
		this.y = y;

		for (int i = 0; i < 10; i++) {
			guiObjects.add(new InventoryButton(this, startX + 10 + (i * (50)), startY + 30, true, i, MainFile.game.getClient().getPlayer()));
		}

		for (int xx = 0; xx < 10; xx++) {
			for (int yy = 0; yy < 4; yy++) {
				guiObjects.add(new InventoryButton(this, startX + 10 + ((xx) * (50)), startY + 40 + ((yy + 1) * (50)), false, 10 + (xx + (yy * 10)), MainFile.game.getClient().getPlayer()));
			}
		}

		//TODO Make FuranceInventoryButton class or add a function to allow rendering icon overlays on inventory buttons.
		//TODO Add a "fire" icon to the fuel slot, a block "border" to the input and output
		guiObjects.add(new InventoryButton(this, startX + 150, 150, false, 0, world.getInventory(x, y)));
		guiObjects.add(new InventoryButton(this, startX + 150, 250, false, 1, world.getInventory(x, y)));
		guiObjects.add(new InventoryButton(this, startX + 300, 200, false, 2, world.getInventory(x, y)){
			public boolean validItem(ItemStack stack){
				return false;
			}
		});
	}

	@Override
	public void render( Graphics g2 ) {
		FurnaceInventory inv = (FurnaceInventory)world.getInventory(x, y);

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
		g2.drawString("Furance", startX + 5, 105);
		FontHandler.resetFont(g2);

		Rectangle rectangle = new Rectangle(startX + 125, 125, 250, 200);

		g2.setColor(Color.lightGray);
		g2.fill(rectangle);

		g2.setColor(Color.darkGray);
		g2.draw(rectangle);

		Rectangle bk = new Rectangle(startX + 210, 213, 75, 20);

		g2.setColor(Color.darkGray.darker());
		g2.fill(bk);


		if(inv.smeltTime > 0) {
			Rectangle progress = new Rectangle(startX + 210, 213, 75 * ((float)inv.smeltTime / (float)(CraftingRegister.getFurnaceSmeltTimeFromInput(inv.getItem(0)))), 20);
			GradientFill fill = new GradientFill(startX + 210, 213, Color.yellow, startX + 210 + (int)(75 * ((float)inv.smeltTime / (float)(CraftingRegister.getFurnaceSmeltTimeFromInput(inv.getItem(0))))), 233, Color.red);

			g2.setColor(Color.red);
			g2.fill(progress, fill);
		}

		g2.setColor(Color.darkGray);
		g2.draw(bk);

		Rectangle fuelRect = new Rectangle(startX + 210, 270, 100, 25);

		g2.setColor(Color.gray);
		g2.fill(fuelRect);

		g2.setColor(Color.darkGray);
		g2.draw(fuelRect);

		g2.setClip(fuelRect);

		g2.setColor(Color.black);
		FontHandler.resizeFont(g2, 12);
		g2.drawString("Fuel: " + inv.fuel, fuelRect.getX() + 4, fuelRect.getY() + 6);
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

	@Override
	public IInventory getInvetory() {
		return world.getInventory(x, y);
	}
}
