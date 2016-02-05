package Guis.Objects;

import GameFiles.BaseGame;
import Interface.GuiObject;
import Interface.UIMenu;
import Utils.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.*;


public class GuiButton extends GuiObject {

	public String text;

	public GuiButton( BaseGame game, int x, int y, int width, int height, String text, UIMenu menu ) {
		super(game, x, y, width, height, menu);
		this.text = text;
	}

	@Override
	public void onClicked( int button, int x, int y, UIMenu menu ) {
		menu.buttonPressed(this);
	}

	@Override
	public void renderObject( Graphics g2, UIMenu menu ) {
		Color temp = g2.getColor();

		int tempY = y - 15;

		boolean hover = isMouseOver();
		g2.setColor(hover ? Color.orange : Color.yellow);

		FontHandler.resizeFont(g2, 12);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString(text, x, tempY);
		FontHandler.resetFont(g2);

		g2.setColor(temp);
	}
}
