package Guis.Interfaces;

import Guis.Objects.MainMenuButton;
import Interface.UIMenu;
import Main.MainFile;
import Settings.Values.ConfigOption;
import Utils.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;


public class SettingsMenu extends AbstractMainMenu {


	public SettingsMenu guiInst = this;

	public SettingsMenu() {
		super();
	}

	@Override
	public void render( Graphics g2 ) {
		Color temp = g2.getColor();

		guiObjects.clear();
		int buttonSize = 40, buttonPos =  (buttonSize * 2);


		guiObjects.add(new keyBinds(buttonPos + (buttonSize * (MainFile.game.getConfig().getConfigOptions().length + 3))));
		guiObjects.add(new texturePack(buttonPos + (buttonSize * (MainFile.game.getConfig().getConfigOptions().length + 4))));

		guiObjects.add(new backButton(buttonPos + (buttonSize * (14))));

		for (ConfigOption option : MainFile.game.getConfig().getConfigOptions()) {
			if(option.showOption())
			guiObjects.add(new configButton(buttonPos += buttonSize, option));
		}

		super.render(g2);
		g2.setColor(Color.black);


		FontHandler.resizeFont(g2, 22);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Settings", renderStart, 80, renderWidth, g2.getColor());
		FontHandler.resetFont(g2);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return true;
	}


	class configButton extends MainMenuButton {

		ConfigOption option;

		public configButton( int y, ConfigOption option ) {
			super(MainFile.game,renderStart, y, 190, 32, "button." + option.getName(), guiInst);
			this.option = option;
		}


		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			option.change();
		}


		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {
			Color temp = g2.getColor();

			boolean hover = isMouseOver();

			if (hover) {
				g2.setColor(new Color(95, 95, 95, 174));
			} else {
				g2.setColor(new Color(95, 95, 95, 86));
			}

			if (!enabled) {
				g2.setColor(new Color(41, 41, 41, 174));
			}

			g2.fill(new Rectangle(renderStart, y, width, height));

			g2.setColor(hover ? Color.white : Color.lightGray);

			if (!enabled) g2.setColor(Color.gray);

			FontHandler.resizeFont(g2, 12);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			org.newdawn.slick.util.FontUtils.drawLeft(g2.getFont(), (option.getName() + ": " + option.getOb()), x + 5, y);
			FontHandler.resetFont(g2);

			g2.setColor(temp);
		}
	}

	class keyBinds extends MainMenuButton {

		public keyBinds( int y ) {
			super(MainFile.game,renderStart, y, 190, 32, "Keybindings", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new KeybindsMenu());
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

	class texturePack extends MainMenuButton {

		public texturePack( int y ) {
			super(MainFile.game, renderStart, y, 190, 32, "Texturepacks", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new TexturepacksInterface());
		}
	}
}
