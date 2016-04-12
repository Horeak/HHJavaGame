package Guis;

import Interface.Gui;
import Interface.GuiObject;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.FontHandler;
import Utils.LoggerUtil;
import Utils.RenderUtil;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public abstract class GuiGame extends Gui {

	public ItemStack heldItem;
	public HashMap<Point, String[]> toolTipRendering = new HashMap<>();

	public GuiGame( GameContainer container, boolean b ) {
		super(container, b);
	}


	public void renderTooltip(int x, int y, String[] text){
		toolTipRendering.put(new Point(x, y), text);
	}

	public void renderPost( Graphics g2 ) {
		int mouseX = MainFile.game.gameContainer.getInput().getMouseX();
		int mouseY = MainFile.game.gameContainer.getInput().getMouseY();


		g2.pushTransform();

		g2.scale(0.5F, 0.5F);
		g2.translate(mouseX - 16, mouseY - 16);

		//TODO Render stack size when item is held?
		if (heldItem != null)
			RenderUtil.renderItem(g2, heldItem, mouseX, mouseY);

		g2.scale(2, 2);
		g2.popTransform();


		for(GuiObject g : guiObjects){
			g.renderObjectPost(g2, this);
		}




		//Renders all tooltips which has been queued
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
				FontHandler.resizeFont(g2, 10);

				if (g == 0) {
					g2.setColor(org.newdawn.slick.Color.black);
					FontHandler.changeFontStyle(g2, Font.BOLD);
				} else {
					g2.setColor(org.newdawn.slick.Color.darkGray);
				}

				g2.drawString(ent.getValue()[ g ], x - 15, (y + 5) + (g * sHeight));
				FontHandler.resetFont(g2);
			}

		}
		toolTipRendering.clear();
	}

	public void closeGui() {
		if(buttonTimeLimiter >= buttonTimeLimit) {
			MainFile.game.gameContainer.setPaused(false);
			MainFile.game.setCurrentMenu(null);
		}else if(buttonTimeLimit == 0){
			LoggerUtil.out.log(Level.SEVERE, "ERROR: attempted closeGui() when buttonTimeLimit was less then buttonTimeLimit!");
		}
	}
}
