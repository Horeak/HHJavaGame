package Guis;

import Guis.Button.InventoryButton;
import Interface.GuiObject;
import Interface.Menu;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.ConfigValues;
import Utils.RenderUtil;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

public abstract class Gui extends Menu {

	public ItemStack heldItem;
	public HashMap<Point, String[]> toolTipRendering = new HashMap<>();

	public Gui( boolean b ) {
		MainFile.gameContainer.setPaused(b);
	}

	public Gui(){
		MainFile.gameContainer.setPaused(ConfigValues.PAUSE_GAME_IN_GUI);
	}

	public void renderTooltip(int x, int y, String[] text){
		toolTipRendering.put(new Point(x, y), text);
	}

	public void renderPost( Graphics g2 ) {
		int mouseX = MainFile.gameContainer.getInput().getMouseX();
		int mouseY = MainFile.gameContainer.getInput().getMouseY();


		g2.pushTransform();

		g2.scale(0.5F, 0.5F);
		g2.translate(mouseX - 16, mouseY - 16);

		if (heldItem != null)
			RenderUtil.renderItem(g2, heldItem, mouseX, mouseY, heldItem.getItem().getRenderMode());

		g2.scale(2, 2);
		g2.popTransform();

		for (Map.Entry<Point, String[]> ent : toolTipRendering.entrySet()) {
			int x = (int) ent.getKey().getX(), y = ((int) ent.getKey().getY()) - (11 * (ent.getValue().length + 1));

			AffineTransform affinetransform = new AffineTransform();
			FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

			Font fontB = new Font("ARIAL", Font.BOLD, 10);
			Font fontP = new Font("ARIAL", Font.PLAIN, 10);

			int width = 0, useWidth = (int) (fontB.getStringBounds(ent.getValue()[ 0 ], frc).getWidth());
			int sHeight = (int) (fontP.getStringBounds("t", frc).getHeight());

			int length = ent.getValue()[0].length();

			if (ent.getValue().length > 1) {
				for (String t : ent.getValue()) {
					int textwidth = (int) (fontP.getStringBounds(t, frc).getWidth());

					if (textwidth > width) {
						width = textwidth;
					}

					if(t.length() > length){
						useWidth = width;
					}
				}

			}

			org.newdawn.slick.geom.Rectangle rect = new org.newdawn.slick.geom.Rectangle(x - 20, y, useWidth + 10, (sHeight * (ent.getValue().length)) + 10);

			g2.setColor(org.newdawn.slick.Color.lightGray);
			g2.fill(rect);

			g2.setColor(org.newdawn.slick.Color.black);
			g2.draw(rect);


			for (int g = 0; g < ent.getValue().length; g++) {
				RenderUtil.resizeFont(g2, 10);

				if (g == 0) {
					g2.setColor(org.newdawn.slick.Color.black);
					RenderUtil.changeFontStyle(g2, Font.BOLD);
				} else {
					g2.setColor(org.newdawn.slick.Color.darkGray);
				}

				g2.drawString(ent.getValue()[ g ], x - 15, (y + 5) + (g * sHeight));

				RenderUtil.resetFont(g2);
			}

		}
		toolTipRendering.clear();
	}

	public void renderInventoryButtons(){
		for (GuiObject ob : guiObjects) {
			if (ob instanceof InventoryButton) {
				InventoryButton button = (InventoryButton) ob;
				ItemStack item = MainFile.currentWorld.player.getItem(button.num);

				if (button.isMouseOver() && item != null) {
					int mouseX = MainFile.gameContainer.getInput().getMouseX();
					int mouseY = MainFile.gameContainer.getInput().getMouseY();

					int length = item.getTooltips() != null ? item.getTooltips().size() + 1 : 1;

					String[] tt = new String[length];

					tt[0] = item.getStackSize() + "x " + item.getStackName();

					if(item.getTooltips() != null)
					for(int j = 0; j < tt.length-1; j++){
						tt[j+1] = item.getTooltips().get(j);
					}

					renderTooltip(mouseX, mouseY, tt);
				}
			}
		}
	}

	public void closeGui() {
		MainFile.gameContainer.setPaused(false);
		MainFile.currentMenu = null;
	}
}
