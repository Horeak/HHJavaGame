package Interface.Interfaces;

import Interface.Menu;
import Interface.Objects.GuiButton;
import Interface.Objects.MainMenuButton;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.RenderUtil;
import WorldFiles.EnumWorldSize;
import WorldFiles.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;


public class CreateWorldMenu extends AbstractMainMenu {

	public CreateWorldMenu guiInst = this;

	public EnumWorldSize selected;
	public boolean textInput = false;
	public String worldName = "";
	createWorldButton createWorldButton;


	//TODO Add world loading. (Make it its own menu and have load/create buttons in this one?)
	public CreateWorldMenu() {
		super();

		int buttonSize = 50, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize);

		createWorldButton = new createWorldButton(buttonPos * 7);
		createWorldButton.enabled = false;

		guiObjects.add(new backButton((buttonPos * 14) - (buttonSize / 2)));
		guiObjects.add(new worldNameInput(buttonPos * 2 + 20));
		guiObjects.add(createWorldButton);

		buttonPos += (buttonSize * 2);
		for (EnumWorldSize size : EnumWorldSize.values()) {
			guiObjects.add(new worldSizeButton(buttonPos += (int) (buttonSize / 1.2), size));
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

		super.render(g2);

		//g2.setPaint(p);
		g2.setColor(org.newdawn.slick.Color.black);

		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Enter world name:", renderStart + 5, 100);
		g2.drawString("World size:", renderStart + 5, 170);
		RenderUtil.resetFont(g2);

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
		public void onClicked( int button, int x, int y, Menu menu ) {
			MainFile.currentMenu = (new MainMenu());
		}
	}

	class createWorldButton extends MainMenuButton {
		public createWorldButton( int y ) {
			super(renderStart, y, 190, 32, "Create world", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Menu menu ) {
			if (createWorldButton.enabled) {
				MainFile.currentWorld = new World(worldName, selected);
				MainFile.currentWorld.generate();
				MainFile.currentWorld.start();

				MainFile.currentMenu = (null);
			}
		}
	}

	class worldNameInput extends MainMenuButton {

		public worldNameInput( int y ) {
			super(renderStart, y, 190, 32, null, guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Menu menu ) {
			textInput ^= true;
		}

		@Override
		public void renderObject( Graphics g2, Menu menu ) {
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
			super(renderStart, y, 120, 32, size.name(), guiInst);
			this.size = size;
		}

		@Override
		public void onClicked( int button, int x, int y, Menu menu ) {
			selected = size;
			textInput = false;
		}

		@Override
		public void renderObject( Graphics g2, Interface.Menu menu ) {
			org.newdawn.slick.Color temp = g2.getColor();

			boolean hover = isMouseOver();

			if (selected != null && selected.name().equalsIgnoreCase(size.name())) {
				g2.setColor(RenderUtil.getColorToSlick(new Color(58, 58, 58, 174)));

			} else if (hover) {
				g2.setColor(RenderUtil.getColorToSlick(new Color(95, 95, 95, 174)));
			} else {
				g2.setColor(RenderUtil.getColorToSlick(new Color(95, 95, 95, 86)));
			}

			g2.fill(new Rectangle(renderStart, y, renderWidth, height));

			g2.setColor(hover ? org.newdawn.slick.Color.white : org.newdawn.slick.Color.lightGray);

			RenderUtil.resizeFont(g2, 22);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			FontUtils.drawCenter(g2.getFont(), text, x, y, renderWidth - 10, g2.getColor());
			RenderUtil.resetFont(g2);


			g2.setColor(temp);
		}
	}
}
