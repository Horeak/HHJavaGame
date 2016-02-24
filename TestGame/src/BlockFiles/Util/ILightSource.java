package BlockFiles.Util;

import org.newdawn.slick.Color;

public interface ILightSource {
	int MAX_LIGHT_STRENGTH = 16;
	Color DEFAULT_LIGHT_COLOR = new Color(0, 0, 0);

	//Strength between 1 and 16
	int getOutputStrength();

	default Color getLightColor() {
		return DEFAULT_LIGHT_COLOR;
	}
}
