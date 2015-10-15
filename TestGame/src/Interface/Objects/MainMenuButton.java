package Interface.Objects;

import Interface.Gui;
import Interface.Interfaces.AbstractMainMenuGui;
import Utils.RenderUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;


public abstract class MainMenuButton extends GuiButton {

	//TODO Remove the hardcoded 325-x position when adding window resizing
	public MainMenuButton(int x, int y, int width, int height, String name) {
		super(x, y, width, height, name);
	}



	public abstract void onClicked(MouseEvent e, JFrame frame, Gui gui);

	public boolean isMouseOver(Point p) {
		try {
			return p.x > AbstractMainMenuGui.renderStart && p.x < (AbstractMainMenuGui.renderStart + AbstractMainMenuGui.renderWidth) && p.y > (y - 14) && p.y < (y + 24);
		} catch (Exception e) {
		}

		return false;
	}

	@Override
	public void renderObject(JFrame frame, Graphics2D g2, Gui gui) {
		Color temp = g2.getColor();

		boolean hover = isMouseOver(frame.getMousePosition());

		if (hover) {
			g2.setColor(new Color(95, 95, 95, 174));
		} else {
			g2.setColor(new Color(95, 95, 95, 86));
		}

		if(!enabled){
			g2.setColor(new Color(41, 41, 41, 174));
		}

		g2.fill(new Rectangle(AbstractMainMenuGui.renderStart, y - 6 - (height * 2), AbstractMainMenuGui.renderWidth, height * 2));

		g2.setColor(hover ? Color.WHITE : Color.LIGHT_GRAY);

		if(!enabled) g2.setColor(Color.GRAY);

		RenderUtil.resizeFont(g2, 22);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString(text, x, y - height);

		RenderUtil.resetFont(g2);


		g2.setColor(temp);
	}

}
