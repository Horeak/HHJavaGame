package Settings;

import Settings.Values.*;
import org.newdawn.slick.Input;

import java.util.ArrayList;

public class Config {

	public static ArrayList<ConfigOption> options = new ArrayList<>();
	public static Keybinding[] keybindings = new Keybinding[]{ new Keybinding("Exit/menu", "exit", Input.KEY_ESCAPE, "Menus"),

			new Keybinding("Jump", "jump.walk", Input.KEY_W, "Movement"), new Keybinding("Walk right", "right.walk", Input.KEY_D, "Movement"), new Keybinding("Walk left", "left.walk", Input.KEY_A, "Movement"),

			new Keybinding("Open inventory", "inventory", Input.KEY_E, "Inventory") };

	//TODO add saving for options. (Look at old game project like GameLogic)
	@Deprecated
	public static void writeToFile() {
	}

	public static void addOptionsToList() {
		options.add(new DebugModeOption());
		options.add(new SimpleRenderOption());
		options.add(new RenderModeOption());
	}

	public static Keybinding getKeybindFromID( String id ) {
		for (Keybinding keybinding : keybindings) {
			if (keybinding.getId().equals(id)) {
				return keybinding;
			}
		}

		return null;
	}

}
