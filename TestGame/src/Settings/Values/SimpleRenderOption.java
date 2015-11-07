package Settings.Values;

import Render.EnumRenderMode;
import Utils.ConfigValues;

public class SimpleRenderOption extends ConfigOption {
	@Override
	public Object getValue() {
		return ConfigValues.simpleBlockRender;
	}

	@Override
	public String getValueDisplay() {
		return Boolean.toString(ConfigValues.simpleBlockRender);
	}

	@Override
	public String getOptionDisplayName() {
		return "Simple block rendering";
	}

	@Override
	public void changeValue() {
		ConfigValues.simpleBlockRender ^= true;

		if (ConfigValues.simpleBlockRender) ConfigValues.renderMod = EnumRenderMode.render2D;
	}
}
