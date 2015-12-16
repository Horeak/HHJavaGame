package Interface.Interfaces;

import Interface.Gui;
import Interface.Objects.GuiButton;
import Interface.Objects.MainMenuButton;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import Utils.RenderUtil;
import WorldFiles.EnumWorldSize;
import WorldFiles.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;


public class GuiCreateWorld extends AbstractMainMenuGui {

	public GuiCreateWorld guiInst = this;

	public EnumWorldSize selected;
	public boolean textInput = false;
	public String worldName = "";
	createWorldButton createWorldButton;
	//TODO Add world loading. (Make it its own gui and have load/create buttons in this one?)
	public GuiCreateWorld() {
		super();

		int buttonSize = 50, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize);

		createWorldButton = new createWorldButton(buttonPos * 7);
		createWorldButton.enabled = false;

		guiObjects.add(new backButton(buttonPos * 9));
		guiObjects.add(new worldNameInput(buttonPos * 2 + 20));
		guiObjects.add(createWorldButton);

		buttonPos += (buttonSize * 3);
		for (EnumWorldSize size : EnumWorldSize.values()) {
			guiObjects.add(new worldSizeButton(buttonPos += buttonSize, size));
		}
	}

	@Override
	public void render( Graphics g2 ) {
		if (!worldName.isEmpty() && selected != null) {
			createWorldButton.enabled = true;
		} else {
			createWorldButton.enabled = false;
		}

		org.newdawn.slick.Color temp = g2.getColor();

		//TODO FIx Paint
		//Paint p = g2.getPaint();
		Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

		super.render(g2);

		//g2.setPaint(p);
		g2.setColor(org.newdawn.slick.Color.black);

		int pos = 325;
		g2.draw(new Line(pos, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));
		g2.draw(new Line(pos += 190, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));

		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Enter world name:", renderStart + 5, 120);
		g2.drawString("World size:", renderStart + 5, 220);
		RenderUtil.resetFont(g2);

		g2.setColor(RenderUtil.getColorToSlick(new Color(95, 95, 95, 112)));
		g2.fill(new Rectangle(325, BlockRendering.START_Y_POS, 190, (ConfigValues.renderYSize * ConfigValues.size)));

		g2.setColor(RenderUtil.getColorToSlick(new Color(152, 152, 152, 96)));
		g2.fill(rectangle);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void buttonPressed( GuiButton button ) {
	}

	public void keyPressed( int key, char c ) {
		if (textInput) {
			if (key == Input.KEY_BACK) {
				if (worldName.length() > 0) {
					worldName = worldName.substring(0, worldName.length() - 1);
				}
			} else {
				if (worldName.length() < 15) {

					if (Character.isDefined(c))
						if (Character.isLetter(c) || Character.isDigit(c) || Character.isSpaceChar(c)) {
						worldName += c;
					}
				}
			}

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

	class createWorldButton extends MainMenuButton {
		public createWorldButton( int y ) {
			super(renderStart, y, 190, 32, "Create world", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Gui gui ) {
			MainFile.currentWorld = new World(worldName, selected);
			MainFile.currentWorld.generate();
			MainFile.currentWorld.start();

			MainFile.setCurrentGui(null);
		}
	}

	class worldNameInput extends MainMenuButton {

		public worldNameInput( int y ) {
			super(renderStart, y, 190, 32, null, guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Gui gui ) {
			textInput ^= true;
		}

		@Override
		public void renderObject( Graphics g2, Gui gui ) {
			org.newdawn.slick.Color temp = g2.getColor();
			if (textInput) {
				g2.setColor(RenderUtil.getColorToSlick(new Color(91, 91, 91, 185)));

			} else {
				g2.setColor(RenderUtil.getColorToSlick(new Color(20, 20, 20, 185)));
			}

			g2.fill(new Rectangle(x, y, width, height));

			g2.setColor(org.newdawn.slick.Color.white);
			RenderUtil.resizeFont(g2, 22);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString(worldName, renderStart + 3, y);

			RenderUtil.resetFont(g2);

			if (textInput) {
				int xx = renderStart + 3 + (worldName != null ? worldName.length() * 12 : 0);

				RenderUtil.resizeFont(g2, 22);
				g2.drawString("_", xx, y);
				RenderUtil.resetFont(g2);
			}


			g2.setColor(temp);
		}
	}

	class worldSizeButton extends MainMenuButton {

		public EnumWorldSize size;

		public worldSizeButton( int y, EnumWorldSize size ) {
			super(renderStart + (53), y, 120, 16, size.name(), guiInst);
			this.size = size;
		}

		@Override
		public void onClicked( int button, int x, int y, Gui gui ) {
			selected = size;
			textInput = false;
		}

		@Override
		public void renderObject( Graphics g2, Gui gui ) {
			org.newdawn.slick.Color temp = g2.getColor();

			boolean hover = isMouseOver();

			if (selected != null && selected.name().equalsIgnoreCase(size.name())) {
				g2.setColor(RenderUtil.getColorToSlick(new Color(58, 58, 58, 174)));

			} else if (hover) {
				g2.setColor(RenderUtil.getColorToSlick(new Color(95, 95, 95, 174)));
			} else {
				g2.setColor(RenderUtil.getColorToSlick(new Color(95, 95, 95, 86)));
			}

			g2.fill(new Rectangle(renderStart, y - 6 - (height), renderWidth, height * 2));

			g2.setColor(hover ? org.newdawn.slick.Color.white : org.newdawn.slick.Color.lightGray);

			RenderUtil.resizeFont(g2, 22);
			RenderUtil.changeFontStyle(g2, Font.BOLD);

			g2.drawString(text, x, y - height);

			RenderUtil.resetFont(g2);


			g2.setColor(temp);
		}
	}
}
