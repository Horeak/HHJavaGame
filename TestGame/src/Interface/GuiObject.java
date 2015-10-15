package Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class GuiObject {


	public int x, y;
	public int width, height;
	public boolean enabled = true;

	public GuiObject(int x, int y, int width, int height){
		this.x = x;
		this.y = y;

		this.width = width;
		this.height = height;
	}

	public abstract void onClicked(MouseEvent e, JFrame frame, Gui gui);
	public abstract void renderObject(JFrame frame, Graphics2D g2, Gui gui);

	public boolean isMouseOver(Point p){
		try {
			if (p != null) {
				if(p.x >= x && p.y >= y){
					if(p.x < (x + width) && p.y < (y + height)){
						return true;
					}
				}
			}

		}catch (Exception ee){

		}

		return false;
	}
}
