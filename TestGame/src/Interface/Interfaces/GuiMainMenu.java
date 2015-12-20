package Interface.Interfaces;

import Interface.Menu;
import Interface.Objects.GuiButton;
import Interface.Objects.MainMenuButton;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;


public class GuiMainMenu extends AbstractMainMenuGui {

	public GuiMainMenu guiInst = this;

	public GuiMainMenu() {
		super();

		int buttonSize = 50, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 2) + 20;

		guiObjects.add(new NewGameButton(buttonPos += buttonSize));
		guiObjects.add(new SettingsButton(buttonPos += buttonSize));
		guiObjects.add(new ExitButton(buttonPos += buttonSize));


		if (MainFile.currentWorld != null) {
			MainFile.currentWorld.stop();
			MainFile.currentWorld = null;
		}
	}

	@Override
	public void render( Graphics g2 ) {
		Color temp = g2.getColor();
		super.render(g2);

		RenderUtil.resizeFont(g2, 22);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		RenderUtil.changeFontName(g2, "Times New Roman");

		g2.setColor(new Color(0.9F, 0.9F, 0.9F, 0.25F));
		g2.fill(new org.newdawn.slick.geom.Rectangle(renderStart, 70, renderWidth, 50));

		g2.setColor(Color.black);
		FontUtils.drawCenter(g2.getFont(), ConfigValues.gameTitle, renderStart, 80, renderWidth - 2, g2.getColor());
		RenderUtil.resetFont(g2);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void buttonPressed( GuiButton button ) {
		System.out.println(button.text);
	}


	//TODO Replace with proper menu screen where you can both load and create worlds once saving is added
	public class NewGameButton extends MainMenuButton {

		public NewGameButton( int y ) {
			super(renderStart, y, 190, 32, "Start Game", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Interface.Menu menu ) {
			MainFile.currentMenu = (new GuiCreateWorld());
		}

	}

	class SettingsButton extends MainMenuButton {

		public SettingsButton( int y ) {
			super(renderStart, y, 190, 32, "Settings", guiInst);
		}


		@Override
		public void onClicked( int button, int x, int y, Menu menu ) {
			MainFile.currentMenu = (new GuiSettingsMainMenu());
		}

	}

	class ExitButton extends MainMenuButton {

		public ExitButton( int y ) {
			super(renderStart, y, 190, 32, "Exit", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Menu menu ) {
			//TODO Add proper exit with saving
			System.exit(0);
		}

	}
}

