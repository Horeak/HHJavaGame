package Blocks.Util;

import org.newdawn.slick.Color;

public class LightUnit {

	private Color lightColor;
	private int lightValue;

	public LightUnit( Color c, int strength ) {
		this.lightColor = c;
		this.lightValue = strength;
	}

	public Color getLightColor() {
		return lightColor;
	}

	public void setLightColor( Color lightColor ) {
		this.lightColor = lightColor;
	}

	public int getLightValue() {
		return lightValue;
	}

	public void setLightValue( int lightValue ) {
		this.lightValue = lightValue;
	}

	@Override
	public boolean equals( Object o ) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LightUnit)) {
			return false;
		}

		LightUnit lightUnit = (LightUnit) o;

		if (getLightValue() != lightUnit.getLightValue()) {
			return false;
		}
		return getLightColor().equals(lightUnit.getLightColor());

	}

	@Override
	public int hashCode() {
		int result = getLightColor().hashCode();
		result = 31 * result + getLightValue();
		return result;
	}
}
