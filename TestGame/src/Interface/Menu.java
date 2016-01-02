package Interface;

import Interface.Objects.GuiButton;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public abstract class Menu {

	public ArrayList<GuiObject> guiObjects = new ArrayList<>();

	public abstract void render( org.newdawn.slick.Graphics g2 );

	public abstract boolean canRender();

	public boolean renderOtherWindowsRenders() {
		return true;
	}

	public void keyPressed( int key, char c ) {
	}

	public void keyReleased( int key, char c ) {
	}

	public void onMouseWheelMoved( int change ) {
		for (GuiObject ob : guiObjects) {
			if (ob.isMouseOver()) {
				ob.onMouseWheelMoved(change);
			}
		}
	}

	public void renderObject( Graphics g2 ) {
		for (GuiObject object : guiObjects) {
			object.renderObject(g2, this);
		}
	}

	public void mouseClick( int button, int x, int y ) {
		for (GuiObject ob : guiObjects) {
			if (ob.isMouseOver()) {
				ob.onClicked(button, x, y, this);
			}
		}
	}

	public void buttonPressed( GuiButton button ) {
	}
}
