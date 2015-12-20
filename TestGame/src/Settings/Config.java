package Settings;

import Settings.Values.ConfigOption;
import Settings.Values.DebugModeOption;
import Settings.Values.RenderModeOption;
import Settings.Values.SimpleRenderOption;

import java.util.ArrayList;

public class Config {

	public static ArrayList<ConfigOption> options = new ArrayList<>();

	//TODO
	@Deprecated
	public static void writeToFile() {
	}

	public static void addOptionsToList() {
		options.add(new DebugModeOption());
		options.add(new SimpleRenderOption());
		options.add(new RenderModeOption());
	}

}
