package Guis.Interfaces;

import Guis.Objects.GuiButton;
import Guis.Objects.MainMenuButton;
import Interface.UIMenu;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import Utils.FileUtil;
import Utils.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import javax.swing.*;
import java.awt.*;


public class MainMenu extends AbstractMainMenu {

	public MainMenu guiInst = this;

	public MainMenu() {
		super();
		int buttonSize = 40, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 2) + 40;

		guiObjects.add(new NewGameButton(buttonPos += buttonSize));
		guiObjects.add(new LoadGameButton(buttonPos += buttonSize));
		guiObjects.add(new SettingsButton(buttonPos += buttonSize));
		guiObjects.add(new ExitButton(buttonPos += buttonSize));


		if (MainFile.game.getServer().getWorld() != null) {
			MainFile.game.getServer().getWorld().stop();
			MainFile.game.getServer().setWorld(null);
		}
	}

	@Override
	public void render( Graphics g2 ) {
		Color temp = g2.getColor();
		super.render(g2);

		FontHandler.resizeFont(g2, 22);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		FontHandler.changeFontName(g2, "Times New Roman");

		g2.setColor(new Color(0.9F, 0.9F, 0.9F, 0.25F));
		g2.fill(new org.newdawn.slick.geom.Rectangle(renderStart, 70, renderWidth, 50));

		g2.setColor(Color.black);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), ConfigValues.gameTitle, renderStart, 80, renderWidth - 2, g2.getColor());
		FontHandler.resetFont(g2);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return true;
	}

	public class NewGameButton extends MainMenuButton {

		public NewGameButton( int y ) {
			super(MainFile.game, renderStart, y, 190, 32, "Start New Game", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new CreateWorldMenu());
		}

	}

	public class LoadGameButton extends MainMenuButton {

		public LoadGameButton( int y ) {
			super(MainFile.game, renderStart, y, 190, 32, "Load Game", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new LoadWorldMenu());
		}

	}

	class SettingsButton extends MainMenuButton {

		public SettingsButton( int y ) {
			super(MainFile.game, renderStart, y, 190, 32, "Settings", guiInst);
		}


		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new SettingsMenu());
		}

	}

	class ExitButton extends MainMenuButton {

		public ExitButton( int y ) {
			super(MainFile.game, renderStart, y, 190, 32, "Exit", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			if(MainFile.game.closeRequested())
				System.exit(0);
		}

	}
}

