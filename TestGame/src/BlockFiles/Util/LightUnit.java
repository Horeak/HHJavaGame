package BlockFiles.Util;

import org.newdawn.slick.Color;

import java.io.Serializable;

public class LightUnit implements Serializable{

	private Color lightColor;
	private float lightValue;

	public LightUnit( Color c, float strength ) {
		this.lightColor = c;
		this.lightValue = strength;
	}

	public Color getLightColor() {
		return lightColor;
	}

	public void setLightColor( Color lightColor ) {
		this.lightColor = lightColor;
	}

	public float getLightValue() {
		return lightValue;
	}

	public void setLightValue( float lightValue ) {
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
		result = 31 * result + Math.round(getLightValue());
		return result;
	}
}
