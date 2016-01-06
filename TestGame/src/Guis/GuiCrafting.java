package Guis;

import Crafting.CraftingRecipe;
import Crafting.CraftingRegister;
import Guis.Button.InventoryButton;
import Interface.GuiObject;
import Interface.Menu;
import Items.Utils.ItemStack;
import Main.MainFile;
import Settings.Config;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;

public class GuiCrafting extends Gui {

	public int startX = 140, startY = 150;
	public int Swidth = 520, Sheight = 297;

	Rectangle rectangle1 = new Rectangle(startX + 9, startY + 25, 216, Sheight - 49);

	CraftingRecipe selectedRes = null;
	boolean init = false;
	float translate = 0; //18 = 1 down


	public void init() {
		int i = 0;
		for (CraftingRecipe res : CraftingRegister.recipes) {
			guiObjects.add(new CraftingButton(this, startX + 10, startY + 25 + (i * (54)), res));
			i += 1;
		}

		guiObjects.add(new craftButton(startX + (Swidth / 2) + 8, startY + (Sheight - 35), 243, 25, this));
		guiObjects.add(new scrollBar(startX + (Swidth / 2) - 25, startY + 25, 20, Sheight - 50, this));


		for (int g = 0; g < 10; g++) {
			guiObjects.add(new InventoryButton(this, (startX) + 10 + (g * (50)), (startY + Sheight) + 5, true, g));
		}

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 4; y++) {
				guiObjects.add(new InventoryButton(this, (startX) + 10 + ((x) * (50)), (startY + Sheight) + 10 + ((y + 1) * (50)), false, 10 + (x + (y * 10))));
			}
		}

	}

	public void renderObject( Graphics g2 ) {
		for (GuiObject object : guiObjects) {
			Rectangle temp = g2.getClip();

			if (object instanceof CraftingButton) {
				g2.pushTransform();

				for (int i = 0; i < translate / 18; i++) {
					g2.translate(0, -translate);
				}

				g2.setClip(rectangle1);

			} else {
				g2.setClip(null);
			}

			object.renderObject(g2, this);

			if (object instanceof CraftingButton) {

				for (int i = 0; i < translate / 18; i++) {
					g2.translate(0, translate);
				}

				g2.popTransform();
			}

			g2.setClip(temp);

		}
	}

	@Override
	public void render( Graphics g2 ) {
		if (!init) {
			init();
			init = true;
		}

		renderInventoryButtons();

		g2.setColor(new Color(0.3F, 0.3F, 0.3F, 0.4F));
		g2.fill(MainFile.blockRenderBounds);

		Rectangle bounder = new Rectangle(startX, startY, Swidth, Sheight);

		g2.setColor(Color.gray);
		g2.fill(bounder);

		g2.setColor(Color.black);
		g2.draw(bounder);


		g2.setColor(Color.white);
		RenderUtil.resizeFont(g2, 12);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Crafting", startX + 5, startY + 5);
		RenderUtil.resetFont(g2);

		Rectangle rightArea = new Rectangle(startX + (Swidth / 2), startY, (Swidth / 2), Sheight);

		g2.setColor(Color.lightGray);
		g2.fill(rightArea);

		g2.setColor(Color.black);
		g2.draw(rightArea);


		for (GuiObject object : guiObjects) {
			if (object instanceof craftButton) {
				((craftButton) object).enabled = selectedRes != null && CraftingRegister.hasMaterialFor(selectedRes);
			}
		}


		if (selectedRes != null) {
			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 12);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Current recipe: " + selectedRes.output.getItem().getItemName(), rightArea.getX() + 5, rightArea.getY() + 5);
			RenderUtil.resetFont(g2);

			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 12);
			g2.drawString("Requires: ", rightArea.getX() + 5, rightArea.getY() + 35);
			RenderUtil.resetFont(g2);

			for (int i = 0; i < selectedRes.input.length; i++) {
				ItemStack req = selectedRes.input[ i ];
				boolean hasItem = CraftingRegister.hasMaterial(req);

				Rectangle rect = new Rectangle((int) rightArea.getX() + 8, (int) rightArea.getY() + 55 + (i * 35), 243, 32);

				g2.setColor(hasItem ? Color.darkGray : Color.darkGray.darker());
				g2.fill(rect);

				g2.setColor(Color.white.darker());
				g2.draw(rect);

				g2.pushTransform();
				g2.scale(0.5F, 0.5F);
				g2.translate(rect.getX(), rect.getY());

				RenderUtil.renderItem(g2, req, (int) rect.getX() + 10, (int) rect.getY() + 25, req.getItem().getRenderMode());

				g2.scale(2, 2);
				g2.popTransform();

				g2.setColor(hasItem ? Color.white : Color.red);
				RenderUtil.resizeFont(g2, 12);
				g2.drawString(req.getStackSize() + "x " + req.getItem().getItemName(), rect.getX() + 35, rect.getY() + 8);

				FontUtils.drawRight(g2.getFont(), "(" + CraftingRegister.getAmount(req) + "/" + req.getStackSize() + ")", (int)rect.getX(), (int)rect.getY() + 8, (int)rect.getWidth() - 8, g2.getColor());

				RenderUtil.resetFont(g2);

			}

			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 12);
			g2.drawString("Creates: ", rightArea.getX() + 5, rightArea.getY() + 200);
			RenderUtil.resetFont(g2);


			Rectangle rect = new Rectangle(rightArea.getX() + 8, rightArea.getY() + 215, 243, 32);
			g2.setColor(Color.darkGray);
			g2.fill(rect);

			g2.setColor(Color.white.darker());
			g2.draw(rect);

			g2.pushTransform();
			g2.scale(0.5F, 0.5F);
			g2.translate(rect.getX(), rect.getY());

			RenderUtil.renderItem(g2, selectedRes.output, (int) rect.getX() + 10, (int) rect.getY() + 25, selectedRes.output.getItem().getRenderMode());

			g2.scale(2, 2);
			g2.popTransform();
			g2.setColor(Color.white);
			RenderUtil.resizeFont(g2, 12);
			g2.drawString(selectedRes.output.getStackSize() + "x " + selectedRes.output.getItem().getItemName(), rect.getX() + 35, rect.getY() + 8);
			RenderUtil.resetFont(g2);

		}

		Rectangle boundere = new Rectangle(startX, startY + Sheight, Swidth, Sheight - 25);
		g2.setColor(Color.gray);
		g2.fill(boundere);

		g2.setColor(Color.darkGray.darker());
		g2.draw(boundere);
	}

	@Override
	public boolean canRender() {
		return true;
	}


	public void keyPressed( int key, char c ) {
		if (key == Config.getKeybindFromID("crafting").getKey() || key == Config.getKeybindFromID("exit").getKey()) {
			closeGui();
		}
	}

	public void onMouseWheelMoved( int change ) {
		for (GuiObject ob : guiObjects) {
			if (ob instanceof scrollBar) {
				ob.onMouseWheelMoved(change);
			}
		}
	}

	class CraftingButton extends GuiObject {

		public CraftingRecipe res;
		public boolean selected = false;
		private boolean over;
		private Rectangle area;

		public CraftingButton( Gui gui, int x, int y, CraftingRecipe res ) {
			super(x, y, 215, 48, gui);
			area = new Rectangle(x, y, width, height);

			this.res = res;
		}


		@Override
		public void onClicked( int button, int x, int y, Menu menu ) {
			selected ^= true;
			selectedRes = selected ? res : null;

			for (GuiObject object : guiObjects) {
				if (object instanceof CraftingButton) {
					if (((CraftingButton) object).res != res) {
						((CraftingButton) object).selected = false;
					}
				}
			}
		}

		public boolean isMouseOver() {
			if (!(MainFile.currentMenu instanceof Gui) && menu != null && (menu instanceof Gui)) {
				return false;
			}

			return over;
		}

		@Override
		public void renderObject( Graphics g2, Menu menu ) {
			boolean other = false;

			for (GuiObject ob : guiObjects) {
				if (ob.isMouseOver()) {
					if (!(ob instanceof CraftingButton)) {
						other = true;
						break;
					}
				}
			}

			over = this.area.contains(MainFile.gameContainer.getInput().getMouseX(), MainFile.gameContainer.getInput().getMouseY() + (float) (translate * (translate / 18))) && !other;
			g2.setClip(new Rectangle(x - 1, y - 1, width + 2, height + 2));

			Rectangle rectangle = new Rectangle(x, y, width, height);
			Rectangle sub = new Rectangle(x, y, 48, 48);

			ItemStack item = res.output;

			g2.setColor(selected ? Color.orange : isMouseOver() ? Color.gray : CraftingRegister.hasMaterialFor(res) ? Color.lightGray : Color.lightGray.darker().darker());
			g2.fill(rectangle);

			g2.setColor(CraftingRegister.hasMaterialFor(res) ? Color.gray.darker() : Color.darkGray.darker());
			g2.fill(sub);

			g2.setColor(Color.black);
			g2.draw(sub);
			g2.draw(rectangle);


			g2.pushTransform();

			g2.scale(0.5F, 0.5F);
			g2.translate(rectangle.getX() + 20, rectangle.getY() + 40);
			RenderUtil.renderItem(g2, item, (int) rectangle.getX(), (int) rectangle.getY(), item.getItem().getRenderMode());
			g2.scale(2, 2);
			g2.popTransform();

			String required = "";

			int g = 0;
			for(ItemStack req : res.input){
				required += req.getStackSize() + "x " + req.getItem().getItemName();

				if((g + 1) < res.input.length){
					required += ", ";
				}

				if((g & 1) != 0) required += "\n";

				g += 1;
			}

			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 12);
			FontUtils.drawRight(g2.getFont(), item.getStackSize() + "x", x, y + 30, 40, g2.getColor());
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString(item.getItem().getItemName(), x + 50, y);
			RenderUtil.resetFont(g2);

			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 10);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Requires: " + required, x + 50, y + 20);
			RenderUtil.resetFont(g2);

			g2.setClip(null);

		}
	}

	class craftButton extends GuiObject {

		public craftButton( int x, int y, int width, int height, Menu menu ) {
			super(x, y, width, height, menu);
		}

		@Override
		public void onClicked( int button, int x, int y, Menu menu ) {

			try {
				if (CraftingRegister.hasMaterialFor(selectedRes)) {

					for (ItemStack item : selectedRes.input) {
						MainFile.currentWorld.player.consumeItem(item);
					}

					MainFile.currentWorld.player.addItem(new ItemStack(selectedRes.output));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void renderObject( Graphics g2, Menu menu ) {
			Rectangle rectangle = new Rectangle(x, y, width, height);

			g2.setColor(enabled ? isMouseOver() ? Color.lightGray.darker(0.3F) : Color.darkGray : Color.darkGray.darker());
			g2.fill(rectangle);

			g2.setColor(Color.white.darker());
			g2.draw(rectangle);

			g2.setColor(enabled ? Color.white : Color.black);
			RenderUtil.resizeFont(g2, 12);

			FontUtils.drawCenter(g2.getFont(), "Craft", (int) rectangle.getX(), (int) rectangle.getY() + 5, (int) rectangle.getWidth(), g2.getColor());

			RenderUtil.resetFont(g2);

		}
	}

	class scrollBar extends GuiObject {

		int trant = 0;

		public scrollBar( int x, int y, int width, int height, Menu menu ) {
			super(x, y, width, height, menu);
		}

		@Override
		public void onClicked( int button, int x, int y, Menu menu ) {

			if (y >= this.y && y <= (this.y + height)) {
				float yy = (y - getY()) - 10;
				trant = (int) yy;
			}
		}

		@Override
		public void renderObject( Graphics g2, Menu menu ) {
			if (trant < 0) {
				trant = 0;
			}

			if (trant > 221) {
				trant = 221;
			}

			float t = (float) trant / 221F;
			translate = t * ((float) (CraftingRegister.recipes.size() - 4F) * 18F);

			Rectangle rect = new Rectangle(x, y, width, height);

			g2.setColor(Color.lightGray);
			g2.fill(rect);

			g2.setColor(Color.black);
			g2.draw(rect);

			g2.setColor(Color.gray);
			g2.fill(new Rectangle(x, y + trant + 1, width - 1, 25));

			g2.setColor(Color.darkGray);
			g2.draw(new Rectangle(x + 1, y + trant + 1, width - 2, 25));
		}

		public void onMouseWheelMoved( int change ) {
			trant += -((change / 120) * 9);
		}
	}

}
