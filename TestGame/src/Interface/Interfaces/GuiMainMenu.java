package Interface.Interfaces;

import Interface.Gui;
import Interface.Objects.GuiButton;
import Interface.Objects.MainMenuButton;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

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
			MainFile.currentWorld.worldUpdateThread.stop();
			MainFile.currentWorld.worldEntityUpdateThread.stop();

			MainFile.currentWorld = null;
		}
	}

	@Override
	public void render( Graphics g2 ) {
		Color temp = g2.getColor();

//		Paint p = g2.getPaint();
		Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

		super.render(g2);

//		g2.setPaint(p);
		g2.setColor(Color.black);

		int pos = renderStart;
		g2.draw(new Line(pos, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));
		g2.draw(new Line(pos += 190, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));

		RenderUtil.resizeFont(g2, 22);
		RenderUtil.changeFontStyle(g2, Font.BOLD);

		int size = RenderUtil.getFontFromSlick(g2.getFont()) != null ? RenderUtil.getFontFromSlick(g2.getFont()).getSize() : 3;

		g2.drawString(ConfigValues.gameTitle, renderStart + ((ConfigValues.gameTitle.length() / 2) * (size / 3)) + 10, 80);
		RenderUtil.resetFont(g2);


		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(renderStart, BlockRendering.START_Y_POS, renderWidth, (ConfigValues.renderYSize * ConfigValues.size)));

		g2.setColor(new Color(152, 152, 152, 96));
		g2.fill(rectangle);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void buttonPressed( GuiButton button ) {
		System.out.println(button.text);
	}


	//TODO Replace with proper gui screen where you can both load and create worlds once saving is added
	public class NewGameButton extends MainMenuButton {

		public NewGameButton( int y ) {
			super(renderStart, y, 190, 32, "Start Game", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Gui gui ) {
			MainFile.setCurrentGui(new GuiCreateWorld());
		}

	}

	class SettingsButton extends MainMenuButton {

		public SettingsButton( int y ) {
			super(renderStart, y, 190, 32, "Settings", guiInst);
		}


		@Override
		public void onClicked( int button, int x, int y, Gui gui ) {
			MainFile.setCurrentGui(new GuiSettingsMainMenu());
		}

	}

	class ExitButton extends MainMenuButton {

		public ExitButton( int y ) {
			super(renderStart, y, 190, 32, "Exit", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Gui gui ) {
			//TODO Add proper exit with saving
			System.exit(0);
		}

	}
}

