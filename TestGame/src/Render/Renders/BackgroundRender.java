package Render.Renders;


import Main.MainFile;
import Rendering.AbstractWindowRender;
import Utils.ConfigValues;
import Utils.ImageLoader;
import WorldFiles.EnumWorldTime;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.fills.GradientFill;

public class BackgroundRender extends AbstractWindowRender {

	public void loadTextures(ImageLoader imageLoader){
		sunImage =  imageLoader.getImage("textures", "sun");
		moonImage =  imageLoader.getImage("textures", "moon");
	}

	public static Image sunImage =  null;
	public static Image moonImage =  null;

	public static void render( Graphics g2, World world ) {
		Color skyColor = world.worldTimeOfDay.SkyColor;

		GradientFill fill = new GradientFill(MainFile.blockRenderBounds.getX(), MainFile.blockRenderBounds.getY(), skyColor, MainFile.blockRenderBounds.getX() + MainFile.blockRenderBounds.getWidth(), MainFile.blockRenderBounds.getY() + MainFile.blockRenderBounds.getHeight(), skyColor.brighter());
		g2.fill(MainFile.blockRenderBounds, fill);

		if (world.WorldTime < EnumWorldTime.NIGHT.timeBegin) {
			float t = (float) world.WorldTime / (float) EnumWorldTime.EVENING.timeEnd;

			float y = t > 0.5F ? (t * 200) : 200 - (t * 200);
			float x = (MainFile.blockRenderBounds.getWidth() * t);

			if (sunImage != null) {
				sunImage.startUse();
				sunImage.drawEmbedded(x, y, 128, 128);
				sunImage.endUse();
			}
		} else {
			float t = ((float) (world.WorldTime - EnumWorldTime.EVENING.timeEnd) / (float) EnumWorldTime.NIGHT.timeEnd) * 2.5F;

			float y = t > 0.5F ? (t * 200) : 200 - (t * 200);
			float x = (MainFile.blockRenderBounds.getWidth() * t);

			if (moonImage != null) {
				moonImage.startUse();
				moonImage.drawEmbedded(x, y, 64, 64);
				moonImage.endUse();
			}
		}
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
		render(g2, MainFile.game.getServer().getWorld());
	}

	@Override
	public boolean canRender() {
		return ConfigValues.RENDER_BACKGROUND && (MainFile.game.getServer().getWorld() != null && !MainFile.game.getServer().getWorld().generating);
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}

}
