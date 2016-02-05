package WorldFiles;

import Utils.FontHandler;

import java.awt.*;

public enum EnumWorldTime {

	MORNING("Morning", 0, 450, FontHandler.getColorToSlick(new Color(0, 235, 255)), 0.7F),
	DAY("Midday", 450, 1200, FontHandler.getColorToSlick(new Color(0, 135, 242)), 0.9F),
	EVENING("Evening", 1200, 1500, FontHandler.getColorToSlick(new Color(112, 44, 27)), 0.4F),
	NIGHT("Night", 1500, 2400, FontHandler.getColorToSlick(new Color(14, 0, 58)), 0.2F);

	public String name;
	public int timeBegin, timeEnd;
	public org.newdawn.slick.Color SkyColor;
	public float lightMultiplier;

	EnumWorldTime( String name, int timeBegin, int timeEnd, org.newdawn.slick.Color skyColor, float lightMultiplier ) {
		this.name = name;
		this.timeBegin = timeBegin;
		this.timeEnd = timeEnd;
		this.SkyColor = skyColor;
		this.lightMultiplier = lightMultiplier;
	}
}
