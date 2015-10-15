package Settings.Values;

import Utils.ConfigValues;

public class DebugModeOption extends ConfigOption {
	@Override
	public Object getValue() {
		return ConfigValues.debug;
	}

	@Override
	public String getValueDisplay() {
		return Boolean.toString(ConfigValues.debug);
	}

	@Override
	public String getOptionDisplayName() {
		return "Debug mode";
	}

	@Override
	public void changeValue() {
		ConfigValues.debug ^= true;
	}
}
