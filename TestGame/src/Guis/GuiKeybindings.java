package Guis;

import Interface.GuiObject;
import Interface.Objects.MainMenuButton;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Settings.Config;
import Settings.Values.Keybinding;
import Utils.ConfigValues;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuiKeybindings extends Gui {
	public static int renderStart = 290;
	public static int renderWidth = 190;
	public GuiKeybindings guiInst = this;
	public HashMap<String, ArrayList<Keybinding>> keyGroupss = new HashMap<>();
	Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));
	int dd = 0;
	boolean selecting = false;


	public GuiKeybindings() {
		super(true);

		for (Keybinding key : Config.keybindings) {
			if (keyGroupss.get(key.getGroup()) != null) {
				keyGroupss.get(key.getGroup()).add(key);

			} else {
				ArrayList<Keybinding> keybindings = new ArrayList<>();
				keybindings.add(key);
				keyGroupss.put(key.getGroup(), keybindings);
			}
		}

		int buttonSize = 40, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 3);
		guiObjects.add(new backButton(buttonPos + (buttonSize * (14))));


		for (Map.Entry<String, ArrayList<Keybinding>> ent : keyGroupss.entrySet()) {
			int pos = buttonPos += ((buttonSize) * .5F);
			for (Keybinding binding : ent.getValue()) {
				guiObjects.add(new keybindButton(pos, binding));
				pos = buttonPos += (buttonSize);
			}
		}
	}
	public void keyPressed( int key, char c ) {
		if (selecting) {
			for (GuiObject object : guiObjects) {
				if (object instanceof keybindButton) {
					if (((keybindButton) object).selected) {

						((keybindButton) object).option.setKey(key);
						((keybindButton) object).selected = false;

						selecting = false;
						break;
					}
				}
			}
		} else {
			if (key == Config.getKeybindFromID("exit").getKey()) {
				MainFile.currentMenu = new GuiSettings();
			}
		}
	}


	@Override
	public void render( Graphics g2 ) {
		dd += 1;

		g2.setColor(org.newdawn.slick.Color.black);
		g2.drawLine(renderStart, BlockRendering.START_Y_POS, renderStart, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));
		g2.drawLine(renderStart + renderWidth, BlockRendering.START_Y_POS, renderStart + renderWidth, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));

		g2.setColor(new Color(152, 152, 152, 60));
		g2.fill(rectangle);

		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(renderStart, BlockRendering.START_Y_POS, renderWidth, (ConfigValues.renderYSize * ConfigValues.size)));

		int buttonSize = 40, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 3);

		for (Map.Entry<String, ArrayList<Keybinding>> ent : keyGroupss.entrySet()) {
			int pos = buttonPos += ((buttonSize) * .5F);

			g2.setColor(new Color(0.2F, 0.2F, 0.2F, 0.5F));
			g2.fill(new org.newdawn.slick.geom.Rectangle(renderStart, pos - (buttonSize / 2), renderWidth, 16));

			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 12);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			FontUtils.drawCenter(g2.getFont(), ent.getKey(), renderStart, pos - (buttonSize / 2), renderWidth, g2.getColor());
			RenderUtil.resetFont(g2);

			for (Keybinding bindingg : ent.getValue()) {
				pos = buttonPos += (buttonSize);
			}
		}


		RenderUtil.resizeFont(g2, 22);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		FontUtils.drawCenter(g2.getFont(), "Keybindings", renderStart, 80, renderWidth, g2.getColor());
		RenderUtil.resetFont(g2);

	}


	@Override
	public boolean canRender() {
		return true;
	}

	class keybindButton extends MainMenuButton {

		public Keybinding option;
		boolean selected = false;

		public keybindButton( int y, Keybinding option ) {
			super(renderStart, y, 190, 32, "button." + option.getId(), guiInst);

			this.option = option;
		}


		@Override
		public void onClicked( int button, int x, int y, Interface.Menu menu ) {
			if (dd >= 10) {
				selected ^= true;
				selecting = true;

				for (GuiObject object : guiObjects) {
					if (object instanceof keybindButton) {
						if (((keybindButton) object).option.getId() != option.getId()) {
							((keybindButton) object).selected = false;
						}
					}
				}
			}
		}


		@Override
		public void renderObject( Graphics g2, Interface.Menu menu ) {
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

			g2.fill(new org.newdawn.slick.geom.Rectangle(renderStart, y, width, height));

			g2.setColor(hover ? Color.white : Color.lightGray);
			if (!enabled) {
				g2.setColor(Color.gray);
			}

			String name = (option.getName() + ": [" + Input.getKeyName(option.getKey()) + "]");

			RenderUtil.resizeFont(g2, 12);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			FontUtils.drawLeft(g2.getFont(), selected ? option.getName() + ": [Press any key]" : name, x + 5, y);

			RenderUtil.resetFont(g2);

			g2.setColor(temp);
		}
	}

	class backButton extends MainMenuButton {

		public backButton( int y ) {
			super(renderStart, y, 190, 32, "Back", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, Interface.Menu menu ) {
			MainFile.currentMenu = new GuiSettings();
		}
	}

}
