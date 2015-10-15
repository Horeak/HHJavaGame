package WorldFiles;

import java.awt.*;


public enum EnumWorldTime {

	MORNING("Morning", 0, 450, new Color(0, 201, 255)),
	DAY("Midday", 450, 900, new Color(0, 135, 242)),
	EVENING("Evening", 900, 1350, new Color(213, 211, 142)),
	NIGHT("Night", 1350, 1800, new Color(14, 0, 58));

	public String name;
	public int timeBegin, timeEnd;
	public Color SkyColor;

	EnumWorldTime(String name, int timeBegin, int timeEnd, Color skyColor){
		this.name = name;
		this.timeBegin = timeBegin;
		this.timeEnd = timeEnd;
		this.SkyColor = skyColor;
	}
}
