package Render.Renders;


import BlockFiles.BlockRender.IBlockRenderer;
import BlockFiles.Util.ILightSource;
import BlockFiles.Util.LightUnit;
import Main.MainFile;
import Rendering.AbstractWindowRender;
import Utils.ConfigValues;
import WorldFiles.Biome;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Structures.Structure;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class BackgroundBlockRender extends AbstractWindowRender {

	public static void render( Graphics g2, World world ) {
		if(MainFile.game.getClient().getPlayer() != null && MainFile.game.getServer().getWorld() != null) {
			Vec2d plPos = new Vec2d(MainFile.game.getClient().getPlayer().getEntityPostion().x, MainFile.game.getClient().getPlayer().getEntityPostion().y);

			int xxx = (ConfigValues.renderXSize * ConfigValues.size), yyy = (ConfigValues.renderYSize * ConfigValues.size);
			int j = ((xxx) / ConfigValues.size), g = ((yyy) / ConfigValues.size);

			for (int x = -(j / 2) - 2; x < (j / 2) + 2; x++) {
				for (int y = -(g / 2) - 2; y < (g / 2) + 2; y++) {

					int xx = (int) (x + plPos.x);
					int yy = (int) (y + plPos.y);

					if (!Chunk.shouldRangeLoad(World.getChunkX(xx), World.getChunkY(yy)))
						continue;

					if (MainFile.game.getServer().getWorld().isChunkLoaded(World.getChunkX(xx), World.getChunkY(yy)) && MainFile.game.getServer().getWorld().getChunk(xx, yy) != null && MainFile.game.getServer().getWorld().getChunk(xx, yy).shouldBeLoaded()) {

						float blockX = (float) (((xx) - plPos.x) + ConfigValues.renderRange);
						float blockY = (float) (((yy) - plPos.y) + ConfigValues.renderRange);

						LightUnit bb = world.getLightUnit(xx, yy);

						int height = (MainFile.game.getServer().getWorld().getHeight(xx));
						float plHeight = (float) plPos.y;

						Image im = getBlockImageFromDepth(yy, height, xx, yy);
						if (im == null) continue;

						im.startUse();
						im.drawEmbedded((int) ((blockX) * ConfigValues.size), (int) ((blockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);
						im.endUse();

						if (bb != null) {
							float t = (float) bb.getLightValue() / (float) ILightSource.MAX_LIGHT_STRENGTH;

							if(world.getBlock(xx, yy) != null){
								t = (float)world.getBlock(xx, yy).getLightValue(world, xx, yy) / (float) ILightSource.MAX_LIGHT_STRENGTH;
							}

							Color temp = world.getLightUnit(x, y).getLightColor();
							Color c = new Color(0, 0, 0, ConfigValues.brightness - t);

							org.newdawn.slick.geom.Rectangle tangle = new org.newdawn.slick.geom.Rectangle((int) ((blockX) * ConfigValues.size), (int) ((blockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);

							g2.setColor(c);
							g2.fill(tangle);

							if (temp != ILightSource.DEFAULT_LIGHT_COLOR) {
								g2.setColor(new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), t));
								g2.fill(tangle);
							}

						}

						IBlockRenderer.drawFront(g2, (int) ((blockX) * ConfigValues.size), (int) ((blockY) * ConfigValues.size), new Color(0, 0, 0, 0.4F));

					}
				}
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


	//Use this to add special background blocks for different levels
	public static Image getBlockImageFromDepth(int depth, int height, int x, int y){
		Biome bm = MainFile.game.getServer().getWorld().getBiome(World.getChunkX(x));

		if(MainFile.game.getServer().getWorld() != null){
			if(MainFile.game.getServer().getWorld().getStructure(x, y) != null) {
				Structure st = MainFile.game.getServer().getWorld().getStructure(x, y);

				if(st != null && st.getBackgroundImage() != null){
					return st.getBackgroundImage();
				}
			}
		}

		if(bm != null) {
			Image im = bm.getBackgroundImage(depth - height);

			if(im != null){
				return im;
			}
		}

		return null;
	}

}
