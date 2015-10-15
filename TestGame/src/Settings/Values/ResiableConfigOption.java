package Settings.Values;

import Utils.ConfigValues;

public class ResiableConfigOption extends ConfigOption {
	@Override
	public Object getValue() {
		return ConfigValues.resizeable;
	}

	@Override
	public String getValueDisplay() {
		return Boolean.toString(ConfigValues.resizeable);
	}

	@Override
	public String getOptionDisplayName() {
		return "[WIP]Resizeable";
	}

	@Override
	public void changeValue() {
		ConfigValues.resizeable ^= true;
	}
}
