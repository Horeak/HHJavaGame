package Settings;

import Main.MainFile;
import Settings.Values.ConfigOption;
import Settings.Values.Keybinding;
import Utils.ConfigValues;
import Utils.LoggerUtil;
import org.newdawn.slick.Input;

public class ConfigFile extends Config{

	private ConfigOption[] configOptions = new ConfigOption[]{
	   new ConfigOption("Simple Rendering", new Boolean[]{ false, true }, ConfigValues.simpleBlockRender) {
		@Override
		public void setValue( Object ob ) {
			ConfigValues.simpleBlockRender = (Boolean)ob;
		}
	}, new ConfigOption("Vsync", new Boolean[]{true, false}, MainFile.game.gameContainer.isVSyncRequested()) {
		@Override
		public void setValue(Object ob) {
			MainFile.game.gameContainer.setVSync((Boolean)ob);
		}
	}, new ConfigOption("Brightness", new Float[]{1F, 0.25F, 0.5F, 0.75F}, 1F) {
		@Override
		public void setValue(Object ob) {
			if((float)ob < 1F){
				ConfigValues.brightness = (float)ob + 1F;
			}else{
				ConfigValues.brightness = (float)ob;
			}
		}




	}, new ConfigOption("Debug mode", new Boolean[]{ false, true }, ConfigValues.debug) {
		@Override
		public void setValue(Object ob) {
			ConfigValues.debug = (Boolean) ob;
			try {
				if(((Boolean)ob)){
					MainFile.game.gameContainer.setDisplayMode(MainFile.xWindowSize + MainFile.debugSize, MainFile.yWindowSize, false);
				}else{
					MainFile.game.gameContainer.setDisplayMode(MainFile.xWindowSize, MainFile.yWindowSize, false);
				}
			} catch (Exception e) {
				LoggerUtil.exception(e);
			}
		}

		public boolean showOption() {
			return MainFile.inDebugMode;
		}
	},
	};

	private Keybinding[] keybindings = new Keybinding[]{
			new Keybinding("Exit/menu", "exit", Input.KEY_ESCAPE, "Menus"),

			new Keybinding("Jump", "jump.walk", Input.KEY_W, "Movement"),
			new Keybinding("Walk right", "right.walk", Input.KEY_D, "Movement"),
			new Keybinding("Walk left", "left.walk", Input.KEY_A, "Movement"),

			new Keybinding("Toggle flight", "flight.toggle", Input.KEY_F, "Movement"),
			new Keybinding("Flight down", "flight.down", Input.KEY_S, "Movement"),

			new Keybinding("Open inventory", "inventory", Input.KEY_E, "Inventory"),
			new Keybinding("Crafting Inventory", "crafting", Input.KEY_C, "Inventory"),
			new Keybinding("Open creative menu", "creative", Input.KEY_B, "Inventory"),

			new Keybinding("Drop item", "drop", Input.KEY_Q, "Action"),

			new Keybinding("Toggle chunks", "chunkRender", Input.KEY_L, "Debug"){
				@Override
				public boolean isEnabled() {
					return MainFile.inDebugMode;
				}
			},

			new Keybinding("Toggle structure bounds", "structureBounds", Input.KEY_K, "Debug"){
				@Override
				public boolean isEnabled() {
					return MainFile.inDebugMode;
				}
			},
	};


	@Override
	public Keybinding[] getKeybindings() {
		return keybindings;
	}

	@Override
	public ConfigOption[] getConfigOptions() {
		return configOptions;
	}



}

