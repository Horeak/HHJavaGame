package Interface.Objects;

import Interface.Gui;
import Interface.GuiObject;
import Utils.RenderUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GuiButton extends GuiObject {

	public String text;

	public GuiButton( int x, int y, int width, int height, String text ) {
		super(x, y, width, height);
		this.text = text;
	}

	@Override
	public void onClicked( MouseEvent e, JFrame frame, Gui gui ) {
		gui.buttonPressed(this);
	}

	@Override
	public void renderObject( JFrame frame, Graphics2D g2, Gui gui ) {
		Color temp = g2.getColor();

		int tempY = y - 15;

		boolean hover = isMouseOver(frame.getMousePosition());
		g2.setColor(hover ? Color.ORANGE : Color.YELLOW);

		RenderUtil.resizeFont(g2, 12);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString(text, x, tempY);

		RenderUtil.resetFont(g2);


		g2.setColor(temp);
	}
}
