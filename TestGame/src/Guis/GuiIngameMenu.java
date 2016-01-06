package Guis;

import Interface.Interfaces.MainMenu;
import Interface.Objects.MainMenuButton;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Settings.Config;
import Utils.ConfigValues;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;

public class GuiIngameMenu extends Gui {

	public static int renderStart = 290;
	public static int renderWidth = 190;
	public GuiIngameMenu guiInst = this;
	Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

	@Override
	public void render( Graphics g2 ) {
		guiObjects.clear();

		int buttonSize = 50, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 2) + 50;

		guiObjects.add(new ResumeButton(buttonPos += buttonSize));
		guiObjects.add(new SettingsButton(buttonPos += buttonSize));
		guiObjects.add(new ExitButton(buttonPos += buttonSize));


		g2.setColor(org.newdawn.slick.Color.black);
		g2.drawLine(renderStart, BlockRendering.START_Y_POS, renderStart, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));
		g2.drawLine(renderStart + renderWidth, BlockRendering.START_Y_POS, renderStart + renderWidth, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));

		g2.setColor(new Color(152, 152, 152, 60));
		g2.fill(rectangle);

		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(renderStart, BlockRendering.START_Y_POS, renderWidth, (ConfigValues.renderYSize * ConfigValues.size)));


		RenderUtil.resizeFont(g2, 22);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		RenderUtil.changeFontName(g2, "Times New Roman");

		g2.setColor(new Color(0.9F, 0.9F, 0.9F, 0.25F));
		g2.fill(new org.newdawn.slick.geom.Rectangle(renderStart, 70, renderWidth, 50));

		g2.setColor(Color.black);
		FontUtils.drawCenter(g2.getFont(), "Game Paused", renderStart, 80, renderWidth - 2, g2.getColor());
		RenderUtil.resetFont(g2);

	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void keyPressed( int key, char c ) {
		if (key == Config.getKeybindFromID("exit").getKey()) {
			closeGui();
		}
	}

	class SettingsButton extends MainMenuButton {

		public SettingsButton( int y ) {
			super(renderStart, y, 190, 32, "Settings", guiInst);
		}


		@Override
		public void onClicked( int button, int x, int y, Interface.Menu menu ) {
			MainFile.currentMenu = new GuiSettings();
		}

	}

	class ExitButton extends MainMenuButton {

		public ExitButton( int y ) {
			super(renderStart, y, 190, 32, "Exit To Main Menu", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Interface.Menu menu ) {
			MainFile.currentWorld.stop();
			MainFile.currentWorld = null;

			MainFile.currentMenu = new MainMenu();
		}

	}

	class ResumeButton extends MainMenuButton {

		public ResumeButton( int y ) {
			super(renderStart, y, 190, 32, "Resume", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Interface.Menu menu ) {
			closeGui();
		}

	}
}
