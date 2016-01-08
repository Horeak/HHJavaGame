package Render.Renders;


import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import Utils.RenderUtil;
import WorldFiles.EnumWorldTime;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

public class BackgroundRender extends AbstractWindowRender {

	public static Image sunImage = RenderUtil.getImage("textures", "sun");
	public static Image moonImage = RenderUtil.getImage("textures", "moon");

	public static void render( Graphics g2, World world ) {
		Color temp = g2.getColor();

		Color curColor = world.worldTimeOfDay.SkyColor;
		Color nextColor = world.getNextWorldTime().SkyColor;


		//TODO Good enough for now but must be improved at a later stage!
		float ratio = (((float) world.WorldTime - (float) world.worldTimeOfDay.timeBegin) / (float) world.worldTimeOfDay.timeEnd) * 1.5F;

		int red = (int) Math.abs((ratio * nextColor.getRed()) + ((1 - ratio) * curColor.getRed()));
		int green = (int) Math.abs((ratio * nextColor.getGreen()) + ((1 - ratio) * curColor.getGreen()));
		int blue = (int) Math.abs((ratio * nextColor.getBlue()) + ((1 - ratio) * curColor.getBlue()));

		Color skyColor = new Color(red, green, blue);

		GradientFill fill = new GradientFill(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, skyColor, ConfigValues.size * ConfigValues.renderXSize, ConfigValues.size * ConfigValues.renderYSize, skyColor.brighter());
		g2.fill(new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, ConfigValues.size * ConfigValues.renderXSize, ConfigValues.size * ConfigValues.renderYSize), fill);

		if (world.WorldTime < EnumWorldTime.NIGHT.timeBegin) {
			float t = (float) world.WorldTime / (float) EnumWorldTime.EVENING.timeEnd;

			float y = t > 0.5F ? (t * 200) : 200 - (t * 200);
			float x = ((ConfigValues.renderXSize * ConfigValues.size) * t);

			sunImage.draw(x, y);
		} else {
			float t = ((float) (world.WorldTime - EnumWorldTime.EVENING.timeEnd) / (float) EnumWorldTime.NIGHT.timeEnd) * 2.5F;

			float y = t > 0.5F ? (t * 200) : 200 - (t * 200);
			float x = ((ConfigValues.renderXSize * ConfigValues.size) * t);

			moonImage.draw(x, y);
		}

		g2.setColor(temp);
	}

	public static Color blend( Color color1, Color color2, double ratio ) {
		float r = (float) ratio;
		float ir = (float) 1.0 - r;

		float rgb1[] = new float[]{ color1.getRed(), color1.getGreen(), color1.getBlue() };
		float rgb2[] = new float[]{ color2.getRed(), color2.getGreen(), color2.getBlue() };

		Color color = new Color(rgb1[ 0 ] * r + rgb2[ 0 ] * ir, rgb1[ 1 ] * r + rgb2[ 1 ] * ir, rgb1[ 2 ] * r + rgb2[ 2 ] * ir);

		return color;
	}

	@Override
	public void render( Graphics g2 ) {
		render(g2, MainFile.getServer().getWorld());
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_BACKGROUND && !MainFile.getServer().getWorld().generating;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}
}
