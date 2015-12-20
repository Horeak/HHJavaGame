package Interface.Objects;

import Interface.GuiObject;
import Interface.Menu;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.*;

public class GuiButton extends GuiObject {

	public String text;

	public GuiButton( int x, int y, int width, int height, String text, Menu menu ) {
		super(x, y, width, height, menu);
		this.text = text;
	}

	@Override
	public void onClicked( int button, int x, int y, Interface.Menu menu ) {
		menu.buttonPressed(this);
	}

	@Override
	public void renderObject( Graphics g2, Menu menu ) {
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
