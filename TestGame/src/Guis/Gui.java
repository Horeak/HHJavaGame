package Guis;

import Interface.Menu;
import Items.IItem;
import Main.MainFile;
import org.newdawn.slick.Graphics;

public abstract class Gui extends Menu {

	public IItem heldItem;

	public Gui() {
	}

	public Gui( boolean b ) {
		MainFile.gameContainer.setPaused(b);
	}

	public void renderPost( Graphics g2 ) {
	}

	public void closeGui() {
		MainFile.gameContainer.setPaused(false);
		MainFile.currentMenu = null;
	}
}
