package Interface.Interfaces;

import Interface.Gui;
import Interface.Objects.GuiButton;
import Interface.Objects.MainMenuButton;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import Utils.RenderUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

public class GuiMainMenu extends AbstractMainMenuGui {

	public GuiMainMenu(){
		super();

		int buttonSize = 50, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 2) + 20;

		guiObjects.add(new NewGameButton(buttonPos += buttonSize));
		guiObjects.add(new SettingsButton(buttonPos += buttonSize));
		guiObjects.add(new ExitButton(buttonPos += buttonSize));


		if(MainFile.currentWorld != null) {
			MainFile.currentWorld.worldUpdateThread.stop();
			MainFile.currentWorld.worldEntityUpdateThread.stop();

			MainFile.currentWorld = null;
		}
	}

	@Override
	public void render(JFrame frame, Graphics2D g2) {
		Color temp = g2.getColor();

		Paint p = g2.getPaint();
		Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

		super.render(frame, g2);

		g2.setPaint(p);
		g2.setColor(Color.black);

		int pos = renderStart;
		g2.draw(new Line2D.Double(pos, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));
		g2.draw(new Line2D.Double(pos += 190, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));

		RenderUtil.resizeFont(g2, 22);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString(ConfigValues.gameTitle, renderStart + ((ConfigValues.gameTitle.length() / 2) * (g2.getFont().getSize() / 3)) + 10, 80);
		RenderUtil.resetFont(g2);


		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(renderStart, BlockRendering.START_Y_POS, renderWidth, (ConfigValues.renderYSize * ConfigValues.size)));

		g2.setColor(new Color(152, 152, 152, 96));
		g2.fill(rectangle);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender(JFrame frame) {
		return true;
	}

	public void buttonPressed(GuiButton button)
	{
		System.out.println(button.text);
	}


	//TODO Replace with proper gui screen where you can both load and create worlds once saving is added
	public class NewGameButton extends MainMenuButton {

		public NewGameButton(int y) {
			super(renderStart + 40, y, 120, 16, "Start Game");
		}

		@Override
		public void onClicked(MouseEvent e, JFrame frame, Gui gui) {
			MainFile.currentGui = new GuiCreateWorld();
		}

	}

	class SettingsButton extends MainMenuButton{

		public SettingsButton(int y) {
			super(renderStart + 55, y, 120, 16, "Settings");
		}


		@Override
		public void onClicked(MouseEvent e, JFrame frame, Gui gui) {
			MainFile.currentGui = new GuiSettingsMainMenu();
		}

	}

	class ExitButton extends MainMenuButton{

		public ExitButton(int y) {
			super(renderStart + 70, y, 120, 16, "Exit");
		}

		@Override
		public void onClicked(MouseEvent e, JFrame frame, Gui gui) {
			//TODO Add proper exit with saving
			System.exit(0);
		}

	}
}

