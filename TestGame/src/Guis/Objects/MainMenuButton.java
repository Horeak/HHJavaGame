package Guis.Objects;

import GameFiles.BaseGame;
import Interface.UIMenu;
import Utils.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;


public abstract class MainMenuButton extends GuiButton {

	public MainMenuButton( BaseGame game, int x, int y, int width, int height, String name, UIMenu menu ) {
		super(game, x, y, width, height, name, menu);
	}

	@Override
	public void renderObject( Graphics g2, UIMenu menu ) {
		Color temp = g2.getColor();

		boolean hover = isMouseOver();

		if (hover) {
			g2.setColor(new Color(95, 95, 95, 174));
		} else {
			g2.setColor(new Color(95, 95, 95, 86));
		}

		if (!enabled) {
			g2.setColor(new Color(41, 41, 41, 174));
		}

		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(hover ? Color.white : Color.lightGray);

		if (!enabled) g2.setColor(Color.gray);

		FontHandler.resizeFont(g2, 22);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), text, x, y, width);

		FontHandler.resetFont(g2);


		g2.setColor(temp);
	}

}
