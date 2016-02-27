package Guis;

import Guis.Objects.MainMenuButton;
import Interface.UIMenu;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Settings.Values.ConfigOption;
import Utils.ConfigValues;
import Utils.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class GuiSettings extends GuiGame {
	public static int renderStart = 290;
	public static int renderWidth = 190;
	public GuiSettings guiInst = this;
	Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

	public GuiSettings( GameContainer container, boolean b ) {
		super(container, b);
	}

	@Override
	public void render( Graphics g2 ) {
		g2.setColor(org.newdawn.slick.Color.black);
		g2.drawLine(renderStart, BlockRendering.START_Y_POS, renderStart, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));
		g2.drawLine(renderStart + renderWidth, BlockRendering.START_Y_POS, renderStart + renderWidth, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));

		g2.setColor(new Color(152, 152, 152, 60));
		g2.fill(rectangle);

		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(renderStart, BlockRendering.START_Y_POS, renderWidth, (ConfigValues.renderYSize * ConfigValues.size)));


		guiObjects.clear();
		int buttonSize = 40, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 2);


		guiObjects.add(new keyBinds(buttonPos + (buttonSize * (MainFile.game.getConfig().getConfigOptions().length + 3))));
		guiObjects.add(new texturePack(buttonPos + (buttonSize * (MainFile.game.getConfig().getConfigOptions().length + 4))));
		guiObjects.add(new backButton(buttonPos + (buttonSize * (14))));

		for (ConfigOption option : MainFile.game.getConfig().getConfigOptions()) {
			guiObjects.add(new configButton(buttonPos += buttonSize, option));
		}


		g2.setColor(Color.black);


		FontHandler.resizeFont(g2, 22);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Settings", renderStart, 80, renderWidth, g2.getColor());
		FontHandler.resetFont(g2);

	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void keyPressed( int key, char c ) {
		if (key == MainFile.game.getConfig().getKeybindFromID("exit").getKey()) {
			MainFile.game.setCurrentMenu(new GuiIngameMenu(MainFile.game.gameContainer, ConfigValues.PAUSE_GAME_IN_GUI));
		}
	}

	class configButton extends MainMenuButton {

		ConfigOption option;

		public configButton( int y, ConfigOption option ) {
			super(MainFile.game, renderStart, y, 190, 32, option.getName(), guiInst);

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

			if (!enabled) {
				g2.setColor(new Color(41, 41, 41, 174));
			}

			if (hover) {
				g2.setColor(new Color(95, 95, 95, 174));
			} else {
				g2.setColor(new Color(95, 95, 95, 86));
			}


			g2.fill(new Rectangle(renderStart, y, width, height));

			g2.setColor(hover ? Color.white : Color.lightGray);

			if (!enabled) {
				g2.setColor(Color.gray);
			}

			FontHandler.resizeFont(g2, 12);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			org.newdawn.slick.util.FontUtils.drawLeft(g2.getFont(), (option.getName() + ": " + option.getOb()), x + 5, y);

			FontHandler.resetFont(g2);

			g2.setColor(temp);
		}
	}

	class keyBinds extends MainMenuButton {

		public keyBinds( int y ) {
			super(MainFile.game, renderStart, y, 190, 32, "Keybindings", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new GuiKeybindings(MainFile.game.gameContainer, false));
		}
	}

	class backButton extends MainMenuButton {

		public backButton( int y ) {
			super(MainFile.game, renderStart, y, 190, 32, "Back", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new GuiIngameMenu(MainFile.game.gameContainer, ConfigValues.PAUSE_GAME_IN_GUI));
		}
	}

	class texturePack extends MainMenuButton {

		public texturePack( int y ) {
			super(MainFile.game, renderStart, y, 190, 32, "Texturepacks", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new GuiTexturepacks(MainFile.game.gameContainer, false));
		}
	}
}
