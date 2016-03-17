package Guis;

import Guis.Interfaces.MainMenu;
import Guis.Objects.MainMenuButton;
import Interface.UIMenu;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import Utils.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class GuiIngameMenu extends GuiGame {

	public static int renderStart = 290;
	public static int renderWidth = 190;
	public GuiIngameMenu guiInst = this;
	Rectangle rectangle = new Rectangle(0, 0, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

	public GuiIngameMenu( GameContainer container, boolean b ) {
		super(container, b);
	}

	@Override
	public void render( Graphics g2 ) {
		guiObjects.clear();

		int buttonSize = 50, buttonPos =  (buttonSize * 2) + 50;

		guiObjects.add(new ResumeButton(buttonPos += buttonSize));
		guiObjects.add(new SettingsButton(buttonPos += buttonSize));
		guiObjects.add(new ExitButton(buttonPos += buttonSize));


		g2.setColor(org.newdawn.slick.Color.black);
		g2.drawLine(renderStart, 0, renderStart,  (ConfigValues.renderYSize * ConfigValues.size));
		g2.drawLine(renderStart + renderWidth, 0, renderStart + renderWidth,  (ConfigValues.renderYSize * ConfigValues.size));

		g2.setColor(new Color(152, 152, 152, 60));
		g2.fill(rectangle);

		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(renderStart, 0, renderWidth, (ConfigValues.renderYSize * ConfigValues.size)));


		FontHandler.resizeFont(g2, 22);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		FontHandler.changeFontName(g2, "Times New Roman");

		g2.setColor(new Color(0.9F, 0.9F, 0.9F, 0.25F));
		g2.fill(new org.newdawn.slick.geom.Rectangle(renderStart, 70, renderWidth, 50));

		g2.setColor(Color.black);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Game Paused", renderStart, 80, renderWidth - 2, g2.getColor());
		FontHandler.resetFont(g2);

	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void keyPressed( int key, char c ) {
		if (key == MainFile.game.getConfig().getKeybindFromID("exit").getKey()) {
			closeGui();
		}
	}

	class SettingsButton extends MainMenuButton {

		public SettingsButton( int y ) {
			super(MainFile.game,renderStart, y, 190, 32, "Settings", guiInst);
		}


		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new GuiSettings(MainFile.game.gameContainer, ConfigValues.PAUSE_GAME_IN_GUI));
		}

	}

	class ExitButton extends MainMenuButton {

		public ExitButton( int y ) {
			super(MainFile.game,renderStart, y, 190, 32, "Exit To Main Menu", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.getServer().getWorld().stop();
			MainFile.game.getServer().setWorld(null);

			MainFile.game.setCurrentMenu(new MainMenu());
		}

	}

	class ResumeButton extends MainMenuButton {

		public ResumeButton( int y ) {
			super(MainFile.game,renderStart, y, 190, 32, "Resume", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			closeGui();
		}

	}
}
