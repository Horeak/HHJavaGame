package Interface;

import Guis.Gui;
import Main.MainFile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.MouseOverArea;

public abstract class GuiObject extends MouseOverArea {

	public int x, y;
	public int width, height;
	public boolean enabled = true;
	public Menu menu;

	public GuiObject( int x, int y, int width, int height, Menu menu ) {
		super(MainFile.gameContainer, null, x, y, width, height);

		this.x = x;
		this.y = y;

		this.width = width;
		this.height = height;

		this.menu = menu;
	}

	public abstract void onClicked( int button, int x, int y, Menu menu );

	public void onMouseWheelMoved( int change ) {
	}

	public abstract void renderObject( Graphics g2, Menu menu );

	public boolean isMouseOver() {
		if (!(MainFile.currentMenu instanceof Gui) && menu != null && (menu instanceof Gui)) {
			return false;
		}

		return super.isMouseOver();
	}
}
