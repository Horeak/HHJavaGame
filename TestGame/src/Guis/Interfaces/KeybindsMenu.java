package Guis.Interfaces;

import Guis.Objects.MainMenuButton;
import Interface.GuiObject;
import Interface.UIMenu;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Settings.Values.Keybinding;
import Utils.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KeybindsMenu extends AbstractMainMenu {

	public KeybindsMenu guiInst = this;
	int dd = 0;

	boolean selecting = false;

	HashMap<String, ArrayList<Keybinding>> keyGroups = new HashMap<>();

	public KeybindsMenu() {
		super();
		for (Keybinding key : MainFile.game.getConfig().getKeybindings()) {
			if(!key.isEnabled()) continue;

			if (keyGroups.get(key.getGroup()) != null) {
				keyGroups.get(key.getGroup()).add(key);
			} else {
				ArrayList<Keybinding> keybindings = new ArrayList<>();
				keybindings.add(key);

				keyGroups.put(key.getGroup(), keybindings);
			}
		}

		int buttonSize = 40, buttonPos =  (buttonSize * 3);
		guiObjects.add(new backButton(buttonPos + (buttonSize * (14))));


		for (Map.Entry<String, ArrayList<Keybinding>> ent : keyGroups.entrySet()) {
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
		}
	}

	@Override
	public void render( Graphics g2 ) {
		dd += 1;

		Color temp = g2.getColor();

		super.render(g2);
		g2.setColor(Color.black);

		int buttonSize = 40, buttonPos =  (buttonSize * 3);

		for (Map.Entry<String, ArrayList<Keybinding>> ent : keyGroups.entrySet()) {
			int pos = buttonPos += ((buttonSize) * .5F);

			g2.setColor(new Color(0.2F, 0.2F, 0.2F, 0.5F));
			g2.fill(new org.newdawn.slick.geom.Rectangle(renderStart, pos - (buttonSize / 2), renderWidth, 16));

			g2.setColor(Color.black);
			FontHandler.resizeFont(g2, 12);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), ent.getKey(), renderStart, pos - (buttonSize / 2), renderWidth, g2.getColor());
			FontHandler.resetFont(g2);

			for (Keybinding binding : ent.getValue()) {
				pos = buttonPos += (buttonSize);
			}
		}


		FontHandler.resizeFont(g2, 22);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Keybindings", renderStart, 80, renderWidth, g2.getColor());
		FontHandler.resetFont(g2);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return true;
	}

	class keybindButton extends MainMenuButton {

		public Keybinding option;
		boolean selected = false;

		public keybindButton( int y, Keybinding option ) {
			super(MainFile.game, renderStart, y, 190, 32, "button." + option.getId(), guiInst);

			this.option = option;
		}


		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			if (dd > 10) {
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
		public void renderObject( Graphics g2, UIMenu menu ) {
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

			String name = (option.getName() + ": [ " + Input.getKeyName(option.getKey()) + " ]");

			FontHandler.resizeFont(g2, 12);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			org.newdawn.slick.util.FontUtils.drawLeft(g2.getFont(), selected ? option.getName() + ": [Press any key]" : name, x + 5, y);
			FontHandler.resetFont(g2);

			FontHandler.resizeFont(g2, 9);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			org.newdawn.slick.util.FontUtils.drawLeft(g2.getFont(), "Default: [ " + (Input.getKeyName(option.getDefaultKey())) + " ]", x + 5, y + 16);
			FontHandler.resetFont(g2);

			g2.setColor(temp);
		}
	}

	class backButton extends MainMenuButton {

		public backButton( int y ) {
			super(MainFile.game, renderStart, y, 190, 32, "Back", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new SettingsMenu());
		}
	}
}
