package Settings;

import Main.MainFile;
import Render.EnumRenderMode;
import Settings.Values.ConfigOption;
import Settings.Values.Keybinding;
import Utils.ConfigValues;
import org.newdawn.slick.Input;

public class ConfigFile extends Config{

	private ConfigOption[] configOptions = new ConfigOption[]{
	   new ConfigOption("Simple Rendering", new Boolean[]{ false, true }, ConfigValues.simpleBlockRender) {
		@Override
		public void setValue( Object ob ) {
			ConfigValues.simpleBlockRender = (Boolean)ob;
		}

	}, new ConfigOption("Render mode", new EnumRenderMode[]{ EnumRenderMode.render2_5D, EnumRenderMode.render2D }, ConfigValues.renderMod) {
		@Override
		public void setValue( Object ob ) {
			ConfigValues.renderMod = (EnumRenderMode)ob;
		}

	}, new ConfigOption("Debug mode", new Boolean[]{ false, true }, ConfigValues.debug) {
		@Override
		public void setValue( Object ob ) {
			ConfigValues.debug = (Boolean)ob;
		}

	}, new ConfigOption("Render minimap", new Boolean[]{true, false}, ConfigValues.RENDER_MINIMAP) {
		@Override
		public void setValue( Object ob ) {
			ConfigValues.RENDER_MINIMAP = (Boolean)ob;
		}

	}, new ConfigOption("Minimap size", new String[]{"Normal", "Large", "Small"}, ConfigValues.MinimapSize) {
		@Override
		public void setValue( Object ob ) {
			ConfigValues.MinimapSize = (String)ob;
		}

	}, new ConfigOption("Vsync", new Boolean[]{true, false}, MainFile.game.gameContainer.isVSyncRequested()) {
		@Override
		public void setValue(Object ob) {
			MainFile.game.gameContainer.setVSync((Boolean)ob);
		}
	}
	};

	private Keybinding[] keybindings = new Keybinding[]{
			new Keybinding("Exit/menu", "exit", Input.KEY_ESCAPE, "Menus"),
			new Keybinding("Open map", "map", Input.KEY_M, "Menus"),

			new Keybinding("Jump", "jump.walk", Input.KEY_W, "Movement"),
			new Keybinding("Walk right", "right.walk", Input.KEY_D, "Movement"),
			new Keybinding("Walk left", "left.walk", Input.KEY_A, "Movement"),

			new Keybinding("Open inventory", "inventory", Input.KEY_E, "Inventory"),
			new Keybinding("Crafting Inventory", "crafting", Input.KEY_C, "Inventory"),

			new Keybinding("Drop item", "drop", Input.KEY_Q, "Action")};


	@Override
	public Keybinding[] getKeybindings() {
		return keybindings;
	}

	@Override
	public ConfigOption[] getConfigOptions() {
		return configOptions;
	}

}
