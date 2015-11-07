package Settings;

import Settings.Values.*;

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

		options.add(new ResiableConfigOption());
	}

}
