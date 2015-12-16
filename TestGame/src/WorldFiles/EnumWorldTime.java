package WorldFiles;

import Utils.RenderUtil;

import java.awt.*;

public enum EnumWorldTime {

	MORNING("Morning", 0, 450, RenderUtil.getColorToSlick(new Color(0, 201, 255))),
	DAY("Midday", 450, 900, RenderUtil.getColorToSlick(new Color(0, 135, 242))),
	EVENING("Evening", 900, 1350, RenderUtil.getColorToSlick(new Color(213, 211, 142))),
	NIGHT("Night", 1350, 1800, RenderUtil.getColorToSlick(new Color(14, 0, 58)));

	public String name;
	public int timeBegin, timeEnd;
	public org.newdawn.slick.Color SkyColor;

	EnumWorldTime( String name, int timeBegin, int timeEnd, org.newdawn.slick.Color skyColor ) {
		this.name = name;
		this.timeBegin = timeBegin;
		this.timeEnd = timeEnd;
		this.SkyColor = skyColor;
	}
}
