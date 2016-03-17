package Guis.Interfaces;

import Guis.Objects.GuiButton;
import Guis.Objects.MainMenuButton;
import Interface.UIMenu;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.FontHandler;
import WorldFiles.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;


public class CreateWorldMenu extends AbstractMainMenu {

	public CreateWorldMenu guiInst = this;

	public boolean textInput = false;
	public String worldName = "";
	createWorldButton createWorldButton;


	public CreateWorldMenu() {
		super();

		int buttonSize = 50, buttonPos =  (buttonSize);

		createWorldButton = new createWorldButton(buttonPos * 4);
		createWorldButton.enabled = false;

		guiObjects.add(new backButton((buttonPos * 14) - (buttonSize / 2)));
		guiObjects.add(new worldNameInput(buttonPos * 2 + 20));
		guiObjects.add(createWorldButton);

	}

	@Override
	public void render( Graphics g2 ) {
		if (!worldName.isEmpty()) {
			createWorldButton.enabled = true;
		} else {
			createWorldButton.enabled = false;
		}

		org.newdawn.slick.Color temp = g2.getColor();

		super.render(g2);

		//g2.setPaint(p);
		g2.setColor(org.newdawn.slick.Color.black);

		FontHandler.resizeFont(g2, 16);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Enter world name:", renderStart + 5, 100);
		FontHandler.resetFont(g2);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void buttonPressed( GuiButton button ) {
	}

	public void keyPressed( int key, char c ) {
		if (textInput) {
			if (key == Input.KEY_BACK) {
				if (worldName.length() > 0) {
					worldName = worldName.substring(0, worldName.length() - 1);
				}
			} else {
				if (worldName.length() < 15) {

					if (Character.isDefined(c))
						if (Character.isLetter(c) || Character.isDigit(c) || Character.isSpaceChar(c)) {
						worldName += c;
					}
				}
			}

		}
	}

	class backButton extends MainMenuButton {

		public backButton( int y ) {
			super(MainFile.game,renderStart, y, 190, 32, "Back", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new MainMenu());
		}
	}

	class createWorldButton extends MainMenuButton {
		public createWorldButton( int y ) {
			super(MainFile.game,renderStart, y, 190, 32, "Create world", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			if (createWorldButton.enabled) {
				MainFile.game.getServer().setWorld(new World(worldName));
				MainFile.game.getServer().getWorld().generate();
				MainFile.game.getServer().getWorld().start();

				MainFile.game.setCurrentMenu(null);
			}
		}
	}

	class worldNameInput extends MainMenuButton {

		public worldNameInput( int y ) {
			super(MainFile.game,renderStart, y, 190, 32, null, guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			textInput ^= true;
		}

		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {

			org.newdawn.slick.Color temp = g2.getColor();
			g2.setColor(FontHandler.getColorToSlick(textInput ? new Color(91, 91, 91, 185) : new Color(20, 20, 20, 185)));


			g2.fill(new Rectangle(x, y, width, height));

			g2.setColor(org.newdawn.slick.Color.white);
			FontHandler.resizeFont(g2, 22);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString(worldName, renderStart + 3, y);

			FontHandler.resetFont(g2);

			if (textInput) {
				int xx = renderStart + 3 + (worldName != null ? worldName.length() * 12 : 0);

				FontHandler.resizeFont(g2, 22);
				g2.drawString("_", xx, y);
				FontHandler.resetFont(g2);
			}


			g2.setColor(temp);
		}
	}

}
