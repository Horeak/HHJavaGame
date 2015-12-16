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
import org.newdawn.slick.geom.Line;
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

		//TODO What was Paint used for and should it be reimplemented?
		//Paint p = g2.getPaint();
		Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

		super.render(g2);

		//g2.setPaint(p);
		g2.setColor(Color.black);


		RenderUtil.resizeFont(g2, 22);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Settings: ", 328, 80);
		RenderUtil.resetFont(g2);


		int pos = 325;
		g2.draw(new Line(pos, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));
		g2.draw(new Line(pos += 190, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));

		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(325, BlockRendering.START_Y_POS, 190, (ConfigValues.renderYSize * ConfigValues.size)));

		g2.setColor(new Color(152, 152, 152, 96));
		g2.fill(rectangle);

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
			MainFile.setCurrentGui(new GuiMainMenu());
		}
	}
}
