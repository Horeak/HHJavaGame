package Interface.Objects;

import Interface.Gui;
import Interface.GuiObject;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.*;

public class GuiButton extends GuiObject {

	public String text;

	public GuiButton( int x, int y, int width, int height, String text, Gui gui ) {
		super(x, y, width, height, gui);
		this.text = text;
	}

	@Override
	public void onClicked( int button, int x, int y, Gui gui ) {
		gui.buttonPressed(this);
	}

	@Override
	public void renderObject( Graphics g2, Gui gui ) {
		Color temp = g2.getColor();

		int tempY = y - 15;

		boolean hover = isMouseOver();
		g2.setColor(hover ? Color.orange : Color.yellow);

		RenderUtil.resizeFont(g2, 12);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString(text, x, tempY);
		RenderUtil.resetFont(g2);

		g2.setColor(temp);
	}
}
