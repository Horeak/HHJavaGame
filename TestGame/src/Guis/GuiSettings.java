package Guis;

import Interface.Objects.MainMenuButton;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Settings.Config;
import Settings.Values.ConfigOption;
import Utils.ConfigValues;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;

public class GuiSettings extends Gui {
	public static int renderStart = 290;
	public static int renderWidth = 190;
	public GuiSettings guiInst = this;
	Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

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


		guiObjects.add(new keyBinds(buttonPos + (buttonSize * (Config.configOptions.length + 2))));
		guiObjects.add(new backButton(buttonPos + (buttonSize * (14))));

		for (ConfigOption option : Config.configOptions) {
			guiObjects.add(new configButton(buttonPos += buttonSize, option));
		}


		g2.setColor(Color.black);


		RenderUtil.resizeFont(g2, 22);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		FontUtils.drawCenter(g2.getFont(), "Settings", renderStart, 80, renderWidth, g2.getColor());
		RenderUtil.resetFont(g2);

	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void keyPressed( int key, char c ) {
		if (key == Config.getKeybindFromID("exit").getKey()) {
			MainFile.getClient().setCurrentMenu(new GuiIngameMenu());
		}
	}

	class configButton extends MainMenuButton {

		ConfigOption option;

		public configButton( int y, ConfigOption option ) {
			super(renderStart, y, 190, 32, option.getName(), guiInst);

			this.option = option;
		}


		@Override
		public void onClicked( int button, int x, int y, Interface.Menu menu ) {
			option.change();
		}


		@Override
		public void renderObject( Graphics g2, Interface.Menu menu ) {
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

			RenderUtil.resizeFont(g2, 12);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			FontUtils.drawLeft(g2.getFont(), (option.getName() + ": " + option.getOb()), x + 5, y);

			RenderUtil.resetFont(g2);

			g2.setColor(temp);
		}
	}

	class keyBinds extends MainMenuButton {

		public keyBinds( int y ) {
			super(renderStart, y, 190, 32, "Keybindings", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Interface.Menu menu ) {
			MainFile.getClient().setCurrentMenu(new GuiKeybindings());
		}
	}

	class backButton extends MainMenuButton {

		public backButton( int y ) {
			super(renderStart, y, 190, 32, "Back", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Interface.Menu menu ) {
			MainFile.getClient().setCurrentMenu(new GuiIngameMenu());
		}
	}
}
