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
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;


public class GuiSettingsMainMenu extends AbstractMainMenuGui {


	public GuiSettingsMainMenu guiInst = this;

	public GuiSettingsMainMenu() {
		super();
	}

	@Override
	public void render( Graphics g2 ) {
		Color temp = g2.getColor();

		guiObjects.clear();
		int buttonSize = 40, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 2);

		for (ConfigOption option : Config.options) {
			guiObjects.add(new configButton(buttonPos += buttonSize, option));
		}

		guiObjects.add(new backButton(buttonPos += (buttonSize * (11))));

		super.render(g2);
		g2.setColor(Color.black);


		RenderUtil.resizeFont(g2, 22);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Settings: ", 328, 80);
		RenderUtil.resetFont(g2);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
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
			super(renderStart, y, 190, 32, "button." + option.getOptionCodeName(), guiInst);

			this.option = option;
		}


		@Override
		public void onClicked( int button, int x, int y, Gui gui ) {
			option.changeValue();
		}


		@Override
		public void renderObject( Graphics g2, Gui gui ) {
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

			RenderUtil.resizeFont(g2, 12);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			FontUtils.drawLeft(g2.getFont(), (option.getOptionDisplayName() + ": " + option.getValueDisplay()), x + 5, y);

			RenderUtil.resetFont(g2);

			g2.setColor(temp);
		}
	}

	class backButton extends MainMenuButton {

		public backButton( int y ) {
			super(renderStart, y, 190, 32, "Back", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Gui gui ) {
			MainFile.currentGui = new GuiMainMenu();
		}
	}
}
