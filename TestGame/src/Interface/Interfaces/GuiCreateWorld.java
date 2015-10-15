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

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;

public class GuiCreateWorld extends AbstractMainMenuGui {

	createWorldButton createWorldButton;

	//TODO Add world loading. (Make it its own gui and have load/create buttons in this one?)
	public GuiCreateWorld(){
		super();

		int buttonSize = 50, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize);

		createWorldButton = new createWorldButton(buttonPos * 7);
		createWorldButton.enabled = false;

		guiObjects.add(new backButton(buttonPos * 9));
		guiObjects.add(new worldNameInput(buttonPos * 2 + 20));
		guiObjects.add(createWorldButton);

		buttonPos += (buttonSize * 3);
		for(EnumWorldSize size : EnumWorldSize.values()){
			guiObjects.add(new worldSizeButton(buttonPos += buttonSize, size));
		}
	}

	public EnumWorldSize selected;
	public boolean textInput = false;
	public String worldName = "";

	@Override
	public void render(JFrame frame, Graphics2D g2) {
		if(!worldName.isEmpty() && selected != null){
			createWorldButton.enabled = true;
		}else{
			createWorldButton.enabled = false;
		}


		Color temp = g2.getColor();

		Paint p = g2.getPaint();
		Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

		super.render(frame, g2);

		g2.setPaint(p);
		g2.setColor(Color.black);

		int pos = 325;
		g2.draw(new Line2D.Double(pos, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));
		g2.draw(new Line2D.Double(pos += 190, BlockRendering.START_Y_POS, pos, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size)));

		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Enter world name:", renderStart + 5, 120);
		g2.drawString("World size:", renderStart + 5, 220);
		RenderUtil.resetFont(g2);

		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(325, BlockRendering.START_Y_POS, 190, (ConfigValues.renderYSize * ConfigValues.size)));

		g2.setColor(new Color(152, 152, 152, 96));
		g2.fill(rectangle);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender(JFrame frame) {
		return true;
	}

	public void buttonPressed(GuiButton button){}

	public void keyPressed(KeyEvent e, JFrame frame)
	{
		if(textInput) {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if (worldName.length() > 0) {
						worldName = worldName.substring(0, worldName.length() - 1);
					}
				} else {
					if(worldName.length() < 15) {
						char c = e.getKeyChar();

						if(Character.isDefined(c)) {
							worldName += c;
						}
					}
				}

		}
	}

	class backButton extends MainMenuButton {

		public backButton(int y){
			super(renderStart + 65, y, 120, 16, "Back");
		}


		@Override
		public void onClicked(MouseEvent e, JFrame frame, Gui gui) {
			MainFile.currentGui = new GuiMainMenu();
		}
	}

	class createWorldButton extends MainMenuButton{
		public createWorldButton(int y){
			super(renderStart + (30), y, 120, 16, "Create world");
		}

		@Override
		public void onClicked(MouseEvent e, JFrame frame, Gui gui) {
			MainFile.currentWorld = new World(worldName, selected);
			MainFile.currentWorld.generate();
			MainFile.currentWorld.start();


			MainFile.currentGui = null;
		}
	}

	class worldNameInput extends MainMenuButton{
		public worldNameInput(int y){
			super(0, y, 120, 16, null);
		}


		boolean render = false;
		int i = -1, m = 600;

		@Override
		public void onClicked(MouseEvent e, JFrame frame, Gui gui) {
			textInput ^= true;
		}

		@Override
		public void renderObject(JFrame frame, Graphics2D g2, Gui gui) {
			Color temp = g2.getColor();

			if(textInput) {
				if (i >= m) {
					i = 0;
					render ^= true;
				} else {
					i += 1;
				}
			}else if(i > 0 || render){
				i = 0;
				render = false;
			}else if(i == -1){
				i = 0;
				render = true;
			}

			if(textInput){
				g2.setColor(new Color(91, 91, 91, 185));

			} else {
				g2.setColor(new Color(20, 20, 20, 185));
			}

			g2.fill(new Rectangle(renderStart, y - 6 - (height * 2), renderWidth, height * 2));

			g2.setColor(Color.WHITE);
			RenderUtil.resizeFont(g2, 22);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString(worldName, renderStart + 3, y - height);

			RenderUtil.resetFont(g2);
			if(render){

				FontRenderContext frc = g2.getFontRenderContext();


				RenderUtil.resizeFont(g2, 22);
				g2.drawString("_", renderStart + 3 + (worldName != null ? (g2.getFont().getLineMetrics(worldName, frc).getNumChars() * 12) : 0), y - height);
				RenderUtil.resetFont(g2);
			}


			g2.setColor(temp);
		}
	}

	class worldSizeButton extends MainMenuButton{

		public EnumWorldSize size;

		public worldSizeButton(int y, EnumWorldSize size){
			super(renderStart + (53), y, 120, 16, size.name());
			this.size = size;
		}

		@Override
		public void onClicked(MouseEvent e, JFrame frame, Gui gui) {
			selected = size;
			textInput = false;
		}

		@Override
		public void renderObject(JFrame frame, Graphics2D g2, Gui gui) {
			Color temp = g2.getColor();

			boolean hover = isMouseOver(frame.getMousePosition());

		if(selected != null && selected.name().equalsIgnoreCase(size.name())){
			g2.setColor(new Color(58, 58, 58, 174));

		}else if (hover) {
			g2.setColor(new Color(95, 95, 95, 174));
		} else {
			g2.setColor(new Color(95, 95, 95, 86));
		}

			g2.fill(new Rectangle(renderStart, y - 6 - (height * 2), renderWidth, height * 2));

			g2.setColor(hover ? Color.WHITE : Color.LIGHT_GRAY);

			RenderUtil.resizeFont(g2, 22);
			RenderUtil.changeFontStyle(g2, Font.BOLD);

			g2.drawString(text, x, y - height);

			RenderUtil.resetFont(g2);


			g2.setColor(temp);
		}
	}
}
