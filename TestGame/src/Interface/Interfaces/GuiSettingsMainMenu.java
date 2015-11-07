package Interface.Interfaces;

import Interface.Gui;
import Interface.Objects.GuiButton;
import Interface.Objects.MainMenuButton;
import Main.MainFile;
import Render.EnumRenderMode;
import Render.Renders.BlockRendering;
import Settings.Config;
import Settings.Values.ConfigOption;
import Utils.ConfigValues;
import Utils.RenderUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

public class GuiSettingsMainMenu extends AbstractMainMenuGui {

	public GuiSettingsMainMenu() {
		super();
	}

	@Override
	public void render( JFrame frame, Graphics2D g2 ) {
		Color temp = g2.getColor();

		guiObjects.clear();
		int buttonSize = 40, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 2);

		for (ConfigOption option : Config.options) {
			guiObjects.add(new configButton(buttonPos += buttonSize, option));
		}

		guiObjects.add(new backButton(buttonPos += (buttonSize * (11))));

		Paint p = g2.getPaint();
		Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

		super.render(frame, g2);

		g2.setPaint(p);
		g2.setColor(Color.black);


		RenderUtil.resizeFont(g2, 22);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Settings: ", 328, 100);
		RenderUtil.resetFont(g2);


		int pos = 325;
		g2.draw(new Line2D.Double(pos, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));
		g2.draw(new Line2D.Double(pos += 190, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));

		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(325, BlockRendering.START_Y_POS, 190, (ConfigValues.renderYSize * ConfigValues.size)));

		g2.setColor(new Color(152, 152, 152, 96));
		g2.fill(rectangle);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender( JFrame frame ) {
		return true;
	}

	public void buttonPressed( GuiButton button ) {
		if (button.text.toLowerCase().contains("rendermod")) {
			if (ConfigValues.renderMod == EnumRenderMode.render2_5D) ConfigValues.renderMod = EnumRenderMode.render2D;
			else ConfigValues.renderMod = EnumRenderMode.render2_5D;
		}
	}

	class configButton extends MainMenuButton {

		ConfigOption option;

		public configButton( int y, ConfigOption option ) {
			super(0, y, 120, 16, "button." + option.getOptionCodeName());

			this.option = option;
		}


		@Override
		public void onClicked( MouseEvent e, JFrame frame, Gui gui ) {
			option.changeValue();
		}


		@Override
		public void renderObject( JFrame frame, Graphics2D g2, Gui gui ) {
			Color temp = g2.getColor();

			boolean hover = isMouseOver(frame.getMousePosition());

			if (hover) {
				g2.setColor(new Color(95, 95, 95, 174));
			} else {
				g2.setColor(new Color(95, 95, 95, 86));
			}

			if (!enabled) {
				g2.setColor(new Color(41, 41, 41, 174));
			}

			g2.fill(new Rectangle(325, y - 6 - (height * 2), 190, height * 2));

			g2.setColor(hover ? Color.WHITE : Color.LIGHT_GRAY);

			if (!enabled) g2.setColor(Color.GRAY);

			RenderUtil.resizeFont(g2, 12);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString(option.getOptionDisplayName() + ": " + option.getValueDisplay(), 328, y - height);

			RenderUtil.resetFont(g2);

			g2.setColor(temp);
		}
	}

	class backButton extends MainMenuButton {

		public backButton( int y ) {
			super((BlockRendering.START_X_POS) + (((ConfigValues.renderXSize - 1) / 2) * ConfigValues.size) - 13, y, 120, 16, "Back");
		}

		@Override
		public void onClicked( MouseEvent e, JFrame frame, Gui gui ) {
			MainFile.currentGui = new GuiMainMenu();
		}
	}
}
