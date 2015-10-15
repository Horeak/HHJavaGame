package Settings.Values;

import Render.EnumRenderMode;
import Utils.ConfigValues;

public class RenderModeOption extends ConfigOption {
	@Override
	public Object getValue() {
		return ConfigValues.renderMod;
	}

	@Override
	public String getValueDisplay() {
		return ConfigValues.renderMod == EnumRenderMode.render2_5D ? "2.5D" : "2D";
	}

	@Override
	public String getOptionDisplayName() {
		return "Render mode";
	}

	@Override
	public void changeValue() {
		if (!ConfigValues.simpleBlockRender) {
			if (ConfigValues.renderMod == EnumRenderMode.render2_5D) {
				ConfigValues.renderMod = EnumRenderMode.render2D;

			} else if (ConfigValues.renderMod == EnumRenderMode.render2D) {
				ConfigValues.renderMod = EnumRenderMode.render2_5D;
			}
		}
	}
}
