package Guis;

import BlockFiles.BlockAir;
import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import Guis.Button.InventoryButton;
import Guis.Objects.IInventoryGui;
import Guis.Objects.MainMenuButton;
import Interface.GuiObject;
import Interface.UIMenu;
import Items.Items;
import Items.Tools.ITool;
import Items.Utils.IInventory;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.FontHandler;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GuiCreativeMenu extends GuiGame implements IInventoryGui {
	@Override
	public IInventory getInvetory() {
		return inv;
	}

	public category currentCategory = categories[0];
	public boolean reset = false;


	public static creativeInventory inv = new creativeInventory();

	public static class creativeInventory implements IInventory {

		public ArrayList<ItemStack> itemStacks = new ArrayList<>();

		@Override
		public ItemStack[] getItems() {
			return (ItemStack[])itemStacks.toArray();
		}

		@Override
		public ItemStack getItem( int i ) {
			if(i >= itemStacks.size()) return null;

			return new ItemStack(itemStacks.get(i));
		}

		@Override
		public void setItem( int i, ItemStack item ) {
		}

		@Override
		public int getInventorySize() {
			return itemStacks.size();
		}

		@Override
		public String getInventoryName() {
			return "Creative Inventory";
		}

		@Override
		public boolean validItemForSlot( ItemStack stack, int slot ) {
			return true;
		}

		@Override
		public boolean canConsumeFromSlot( int slot ) {
			return true;
		}
	}


	public static int page = 40;
	public int pageStart = 0, pageEnd = page;

	public void applyCategory(category cat){
		inv.itemStacks.clear();
		guiObjects.removeIf(b -> (b instanceof InventoryButton && ((InventoryButton)b).inv == inv));


		for(Block b : Blocks.blockRegistry.keySet()){
			if(b instanceof BlockAir)continue;

			ItemStack stack = new ItemStack(b, b.getItemMaxStackSize());

			if(cat.isItemValid(stack)) {
				inv.itemStacks.add(stack);
			}
		}


		inv.itemStacks.sort(new Comparator<ItemStack>() {
			@Override
			public int compare( ItemStack o1, ItemStack o2 ) {
				return o1.isBlock() && o2.isBlock() ? Integer.compare(o1.getBlock().registryValue, o2.getBlock().registryValue) : 0;
			}
		});

		for(Item b :Items.ItemRegistry.keySet()){
			ItemStack stack = new ItemStack(b, b.getItemMaxStackSize());


			if(cat.isItemValid(stack)) {
				inv.itemStacks.add(stack);
			}
		}

		inv.itemStacks.sort(new Comparator<ItemStack>() {
			@Override
			public int compare( ItemStack o1, ItemStack o2 ) {
				return !o1.isBlock() && !o2.isBlock() ? Integer.compare(((Item)o1.getItem()).registryValue, ((Item)o2.getItem()).registryValue) : 0;
			}
		});


		for(int i = pageStart; i < pageEnd; i++) {
			guiObjects.add(new invButton(this, startX + 10 + ((i - ((i / 10) * 10)) * 50), startY - 160 + (((i - (pageStart)) / 10) * 50), false, i, inv));
		}
	}

	public int startX = 140, startY = 300;
	public int Swidth = 520, Sheight = 497;


	//TODO Add search bar?
	public GuiCreativeMenu(GameContainer container, boolean b) {
		super(container, b);

		applyCategory(currentCategory);

		for (int i = 0; i < 10; i++) {
			guiObjects.add(new InventoryButton(this, startX + 10 + (i * (50)), startY + 110, true, i, MainFile.game.getClient().getPlayer()));
		}

		for (int xx = 0; xx < 10; xx++) {
			for (int yy = 0; yy < 4; yy++) {
				guiObjects.add(new InventoryButton(this, startX + 10 + ((xx) * (50)), startY + 120 + ((yy + 1) * (50)), false, 10 + (xx + (yy * 10)), MainFile.game.getClient().getPlayer()));
			}
		}

		guiObjects.add(new MainMenuButton(MainFile.game, startX + 10, startY + 60, 60, 30, "<-", this) {
			@Override
			public void renderObject( Graphics g2, UIMenu menu ) {
				super.renderObject(g2, menu);
			}

			@Override
			public void onClicked( int button, int x, int y, UIMenu menu ) {
				super.onClicked(button, x, y, menu);

				if(pageStart >= page){
					pageStart -= page;
					pageEnd = pageStart + page;
					reset = true;
				}
			}
		});

		guiObjects.add(new MainMenuButton(MainFile.game, startX + 140, startY + 60, 60, 30, "->", this){
			@Override
			public void renderObject( Graphics g2, UIMenu menu ) {
				super.renderObject(g2, menu);
			}

			@Override
			public void onClicked( int button, int x, int y, UIMenu menu ) {
				super.onClicked(button, x, y, menu);

				if(inv.getInventorySize() >= pageEnd){
					pageStart += page;
					pageEnd = pageStart + page;
					reset = true;
				}
			}
		});

		int i = 0;
		for(category catt : categories){
			guiObjects.add(new categoryButton(startX - 50, (startY - 160) + (i * 55), 50, 50, this, catt));

			i += 1;
		}
	}

	@Override
	public void render( Graphics g2 ) {
		if(reset){
			applyCategory(currentCategory);
			reset = false;
		}

		g2.setColor(new Color(0.3F, 0.3F, 0.3F, 0.4F));
		g2.fill(MainFile.blockRenderBounds);

		Rectangle bounder = new Rectangle(startX - 60, 100, Swidth + 60, Sheight + 85);

		g2.setColor(Color.gray);
		g2.fill(bounder);

		g2.setColor(Color.darkGray.darker());
		g2.draw(bounder);

		g2.setColor(Color.lightGray);
		g2.fill(new Rectangle(startX + 5, startY + 55, 200, 40));


		g2.setColor(Color.white);
		FontHandler.resizeFont(g2, 12);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString(inv.getInventoryName(), startX + 5, 105);

		FontHandler.resizeFont(g2, 14);
		FontUtils.drawCenter(g2.getFont(), (((pageStart / page) + 1) + "/" + ((inv.getInventorySize() / page) + 1)), startX + 70, startY + 66, 70, g2.getColor());
		FontHandler.resetFont(g2);

	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void keyPressed( int key, char c ) {
		if (key == MainFile.game.getConfig().getKeybindFromID("inventory").getKey() || key == MainFile.game.getConfig().getKeybindFromID("creative").getKey() || key == MainFile.game.getConfig().getKeybindFromID("exit").getKey()) {
			closeGui();
		}
	}

	public static category[] categories = new category[]{
			new category() {

				ItemStack stack = new ItemStack(Blocks.blockFurnace);
				@Override
				public boolean isItemValid( ItemStack stack ) {
					return true;
				}

				@Override
				public ItemStack getRender() {
					return stack;
				}

				@Override
				public String getName() {
					return "All";
				}

			}, new category() {
			ItemStack stack = new ItemStack(Blocks.blockPlanks);

			@Override
			public boolean isItemValid( ItemStack stack ) {
				return stack.isBlock();
			}

			@Override
			public ItemStack getRender() {
				return stack;
			}

			@Override
			public String getName() {
				return "Blocks";
			}

			},  new category() {
			ItemStack stack = new ItemStack(Blocks.blockChest);

			@Override
			public boolean isItemValid( ItemStack stack ) {
				return stack.isBlock() && stack.getBlock().getInventory() != null;
			}

			@Override
			public ItemStack getRender() {
				return stack;
			}

			@Override
			public String getName() {
				return "Inventories";
			}

			},new category() {
			ItemStack stack = new ItemStack(Blocks.blockItemMover);

			@Override
			public boolean isItemValid( ItemStack stack ) {
				return stack.isBlock() && stack.getBlock().getTickBlock() != null;
			}

			@Override
			public ItemStack getRender() {
				return stack;
			}

			@Override
			public String getName() {
				return "Tickable blocks";
			}

			},new category() {
			ItemStack stack = new ItemStack(Items.itemStick);

			@Override
			public boolean isItemValid( ItemStack stack ) {
				return !stack.isBlock();
			}

			@Override
			public ItemStack getRender() {
				return stack;
			}

			@Override
			public String getName() {
				return "Items";
			}

			},new category() {
				ItemStack stack = new ItemStack(Items.itemStonePickaxe);
				@Override
				public boolean isItemValid( ItemStack stack ) {
					return stack.getItem() instanceof ITool;
				}

				@Override
				public ItemStack getRender() {
					return stack;
				}

				@Override
				public String getName() {
					return "Tools";
				}
			}

	};


	static abstract class category{
		public abstract boolean isItemValid(ItemStack stack);
		public abstract ItemStack getRender();
		public abstract String getName();
	}

	class categoryButton extends GuiObject {

		category gg;
		public categoryButton( int x, int y, int width, int height, UIMenu menu, category g ) {
			super(MainFile.game,x, y, width, height, menu);
			this.gg = g;
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			currentCategory = gg;
			reset = true;

			pageStart = 0;
			pageEnd = page;
		}

		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {
			Rectangle rectangle = new Rectangle(x, y, width, height);

			g2.setColor(enabled ? isMouseOver() ? Color.lightGray : Color.darkGray : Color.darkGray.darker());

			if(currentCategory == gg){
				g2.setColor(Color.gray);
			}

			g2.fill(rectangle);

			g2.setColor(currentCategory == gg ? Color.yellow : Color.white);
			g2.draw(rectangle);

			g2.pushTransform();
			g2.scale(0.5F, 0.5F);
			g2.translate(rectangle.getX() + 30, rectangle.getY() + 45);
			RenderUtil.renderItem(g2, gg.getRender(), (int) rectangle.getX(), (int) rectangle.getY());
			g2.scale(2, 2);
			g2.popTransform();

			if(isMouseOver()){
				int mouseX = MainFile.game.gameContainer.getInput().getMouseX();
				int mouseY = MainFile.game.gameContainer.getInput().getMouseY();

				renderTooltip(mouseX, mouseY, new String[]{gg.getName()});
			}

		}
	}


	class invButton extends InventoryButton{

		public invButton( GuiGame gui, int x, int y, boolean renderNumber, int number, IInventory inventory ) {
			super(gui, x, y, renderNumber, number, inventory);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			if (gui.heldItem != null) {
				if (inv != MainFile.game.getClient().getPlayer()) {
					gui.heldItem = null;
					return;
				}
			}

		super.onClicked(button, x, y, menu);
		}

	}

}

