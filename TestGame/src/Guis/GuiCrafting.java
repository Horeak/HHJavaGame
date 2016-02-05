package Guis;

import Crafting.CraftingRecipe;
import Crafting.CraftingRegister;
import Guis.Button.InventoryButton;
import Interface.GuiObject;
import Interface.UIMenu;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.FontHandler;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class GuiCrafting extends GuiGame {

	public GuiCrafting inst = this;

	public int startX = 140, startY = 150;
	public int Swidth = 520, Sheight = 297;

	Rectangle rectangle1 = new Rectangle(startX + 9, startY + 24, 216, Sheight - 49);

	CraftingRecipe selectedRes = null;
	float translate = 0; //18 = 1 down

	boolean textInput = false;
	public String input = "";

	public GuiCrafting( GameContainer container, boolean b ) {
		super(container, b);
	}


	public void init() {
		int i = 0;
		for (CraftingRecipe res : CraftingRegister.recipes) {
			if(input != null && !input.isEmpty() && res.output.getStackName().toLowerCase().contains(input.toLowerCase()) || input == null || input.isEmpty()) {
				guiObjects.add(new CraftingButton(this, startX + 10, startY + 25 + (i * (54)) - (int)(translate * 2), res));
				i += 1;
			}
		}

		guiObjects.add(new craftButton(startX + (Swidth / 2) + 8, startY + (Sheight - 35), 243, 25, this));

		boolean t = false, j = false;
		for(GuiObject ob : guiObjects) {
			if (ob instanceof scrollBar)
				t = true;
			else if(ob instanceof inputButton)
				j = true;
		}


		if(!t)
		guiObjects.add(new scrollBar(startX + (Swidth / 2) - 25, startY + 25, 20, Sheight - 50, this));

		if(!j)
			guiObjects.add(new inputButton(startX + 9, startY - 10, 246, 32));


		for (int g = 0; g < 10; g++) {
			guiObjects.add(new InventoryButton(this, (startX) + 10 + (g * (50)), (startY + Sheight) + 5, true, g));
		}

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 4; y++) {
				guiObjects.add(new InventoryButton(this, (startX) + 10 + ((x) * (50)), (startY + Sheight) + 10 + ((y + 1) * (50)), false, 10 + (x + (y * 10))));
			}
		}

	}

	public void mouseClick( int button, int x, int y ) {
		boolean t = false;

		for (GuiObject ob : guiObjects) {
			if (ob.isMouseOver()) {
				ob.onClicked(button, x, y, this);

				if(ob instanceof inputButton){
					t = true;
				}
			}
		}

		if(!t && textInput){
			textInput = false;
		}
	}

	@Override
	public void render( Graphics g2 ) {
		guiObjects.removeIf(e -> (!(e instanceof scrollBar) && !(e instanceof inputButton)));
		init();

		renderInventoryButtons();

		g2.setColor(new Color(0.3F, 0.3F, 0.3F, 0.4F));
		g2.fill(MainFile.blockRenderBounds);

		Rectangle bounder = new Rectangle(startX, startY - 30, Swidth, Sheight + 30);

		g2.setColor(Color.gray);
		g2.fill(bounder);

		g2.setColor(Color.black);
		g2.draw(bounder);


		g2.setColor(Color.white);
		FontHandler.resizeFont(g2, 12);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Crafting", startX + 5, startY - 25);
		FontHandler.resetFont(g2);

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
			FontHandler.resizeFont(g2, 12);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Current recipe: " + selectedRes.output.getItem().getItemName(), rightArea.getX() + 5, rightArea.getY() + 5);
			FontHandler.resetFont(g2);

			g2.setColor(Color.black);
			FontHandler.resizeFont(g2, 12);
			g2.drawString("Requires: ", rightArea.getX() + 5, rightArea.getY() + 35);
			FontHandler.resetFont(g2);

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
				FontHandler.resizeFont(g2, 12);
				g2.drawString(req.getStackSize() + "x " + req.getItem().getItemName(), rect.getX() + 35, rect.getY() + 8);

				org.newdawn.slick.util.FontUtils.drawRight(g2.getFont(), "(" + CraftingRegister.getAmount(req) + "/" + req.getStackSize() + ")", (int)rect.getX(), (int)rect.getY() + 8, (int)rect.getWidth() - 8, g2.getColor());

				FontHandler.resetFont(g2);

			}

			g2.setColor(Color.black);
			FontHandler.resizeFont(g2, 12);
			g2.drawString("Creates: ", rightArea.getX() + 5, rightArea.getY() + 200);
			FontHandler.resetFont(g2);


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
			FontHandler.resizeFont(g2, 12);
			g2.drawString(selectedRes.output.getStackSize() + "x " + selectedRes.output.getItem().getItemName(), rect.getX() + 35, rect.getY() + 8);
			FontHandler.resetFont(g2);

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

	public void renderObject( Graphics g2 ) {
		for (GuiObject object : guiObjects) {
			Rectangle temp = g2.getClip();

			if (object instanceof CraftingButton) {
				g2.setClip(rectangle1);

			} else {
				g2.setClip(null);
			}

			object.renderObject(g2, this);

		}
	}


	public void keyPressed( int key, char c ) {
			if (key == MainFile.game.getConfig().getKeybindFromID("crafting").getKey() && !textInput || key == MainFile.game.getConfig().getKeybindFromID("exit").getKey() && !textInput) {
				closeGui();
			}else {

				if (key == Input.KEY_BACK) {
					if (input.length() > 0) {
						input = input.substring(0, input.length() - 1);
					}
				} else {
					if (input.length() < 19) {
						if (Character.isDefined(c))
							if (Character.isLetter(c) || Character.isDigit(c) || Character.isSpaceChar(c)) {
								input += c;
							}
					}
			}
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

		public CraftingButton( GuiGame gui, int x, int y, CraftingRecipe res ) {
			super(MainFile.game,x, y, 215, 48, gui);
			area = new Rectangle(x, y, width, height);

			this.res = res;
		}


		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
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


		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {
			boolean other = false;

			for (GuiObject ob : guiObjects) {
				if (ob.isMouseOver()) {
					if (!(ob instanceof CraftingButton)) {
						other = true;
						break;
					}
				}
			}

			selected = selectedRes == res;
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
			FontHandler.resizeFont(g2, 12);
			org.newdawn.slick.util.FontUtils.drawRight(g2.getFont(), item.getStackSize() + "x", x, y + 30, 40, g2.getColor());
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString(item.getItem().getItemName(), x + 50, y);
			FontHandler.resetFont(g2);

			g2.setColor(Color.black);
			FontHandler.resizeFont(g2, 10);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Requires: " + required, x + 50, y + 20);
			FontHandler.resetFont(g2);

			g2.setClip(null);

		}
	}

	class craftButton extends GuiObject {

		public craftButton( int x, int y, int width, int height, UIMenu menu ) {
			super(MainFile.game,x, y, width, height, menu);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {

			try {
				if (CraftingRegister.hasMaterialFor(selectedRes)) {

					for (ItemStack item : selectedRes.input) {
						MainFile.game.getClient().getPlayer().consumeItem(item);
					}

					MainFile.game.getClient().getPlayer().addItem(new ItemStack(selectedRes.output));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {
			Rectangle rectangle = new Rectangle(x, y, width, height);

			g2.setColor(enabled ? isMouseOver() ? Color.lightGray.darker(0.3F) : Color.darkGray : Color.darkGray.darker());
			g2.fill(rectangle);

			g2.setColor(Color.white.darker());
			g2.draw(rectangle);

			g2.setColor(enabled ? Color.white : Color.black);
			FontHandler.resizeFont(g2, 12);

			org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Craft", (int) rectangle.getX(), (int) rectangle.getY() + 5, (int) rectangle.getWidth(), g2.getColor());

			FontHandler.resetFont(g2);

		}
	}

	class scrollBar extends GuiObject {

		int trant = 0;

		public scrollBar( int x, int y, int width, int height, UIMenu menu ) {
			super(MainFile.game,x, y, width, height, menu);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {

			if (y >= this.y && y <= (this.y + height)) {
				float yy = (y - getY()) - 10;
				trant = (int) yy;
			}
		}

		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {
			if (trant < 0) {
				trant = 0;
			}

			if (trant > 221) {
				trant = 221;
			}

			int am = 0;
			for(GuiObject ob : guiObjects)
			if(ob instanceof CraftingButton) am += 1;

			float t = (float) trant / 221F;
			translate = t * ((float) (am - (am > 4 ? 4F : am)) * 18F);

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
			trant += -((change / 120) * 10);
		}
	}

	class inputButton extends GuiObject {

		int i = 0;
		public inputButton(int x, int y, int width, int height) {
			super(MainFile.game,x, y, width, height, inst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			textInput ^= true;
		}

		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {
			if(i >= 20) i = 0;
			i += 1;
			org.newdawn.slick.Color temp = g2.getColor();

			g2.setColor(textInput ? Color.darkGray.brighter() : Color.darkGray.darker());
			g2.fill(new Rectangle(x, y, width, height));

			g2.setColor(Color.black);
			g2.draw(new Rectangle(x, y, width, height));

			g2.setColor(org.newdawn.slick.Color.white);
			FontHandler.resizeFont(g2, 22);
			g2.drawString(inst.input, x + 3, y);

			FontHandler.resetFont(g2);

			AffineTransform affinetransform = new AffineTransform();
			FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
			Font font = new Font("ARIAL", Font.PLAIN, 22);
			int width = (int) (font.getStringBounds(inst.input, frc).getWidth());

			if(textInput && i < 10 || !textInput) {
				FontHandler.resizeFont(g2, 22);
				g2.drawString("_", x + 3 + width, y);
				FontHandler.resetFont(g2);
			}

			g2.setColor(temp);
		}
	}

}
