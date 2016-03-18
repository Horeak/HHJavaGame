package Render.Renders;


import BlockFiles.BlockRender.DefaultBlockRendering;
import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import BlockFiles.Util.ILightSource;
import BlockFiles.Util.LightUnit;
import Items.Utils.ItemStack;
import Main.MainFile;
import Rendering.AbstractWindowRender;
import Utils.ConfigValues;
import WorldFiles.Chunk;
import WorldFiles.EnumWorldTime;
import WorldFiles.World;
import WorldGeneration.Structures.Structure;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.*;

import java.awt.*;
import java.awt.Rectangle;

public class BackgroundRender extends AbstractWindowRender {

	public void loadTextures(){
		sunImage =  MainFile.game.imageLoader.getImage("textures", "sun");
		moonImage =  MainFile.game.imageLoader.getImage("textures", "moon");
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

			if (sunImage != null)
				sunImage.draw(x, y);
		} else {
			float t = ((float) (world.WorldTime - EnumWorldTime.EVENING.timeEnd) / (float) EnumWorldTime.NIGHT.timeEnd) * 2.5F;

			float y = t > 0.5F ? (t * 200) : 200 - (t * 200);
			float x = (MainFile.blockRenderBounds.getWidth() * t);

			if (moonImage != null)
				moonImage.draw(x, y);
		}

		if(MainFile.game.getClient().getPlayer() != null && MainFile.game.getServer().getWorld() != null) {
			Vec2d plPos = new Vec2d(MainFile.game.getClient().getPlayer().getEntityPostion().x, MainFile.game.getClient().getPlayer().getEntityPostion().y);

			int xxx = (ConfigValues.renderXSize * ConfigValues.size), yyy = (ConfigValues.renderYSize * ConfigValues.size);
			int j = ((xxx) / ConfigValues.size), g = ((yyy) / ConfigValues.size);

			for (int x = -(j / 2) - 2; x < (j / 2) + 2; x++) {
				for (int y = -(g / 2) - 2; y < (g / 2) + 2; y++) {

					int xx = (int) (x + plPos.x);
					int yy = (int) (y + plPos.y);

					if (!Chunk.shouldRangeLoad(xx / Chunk.chunkSize, yy / Chunk.chunkSize))
						continue;

					if (MainFile.game.getServer().getWorld().isChunkLoaded(xx / Chunk.chunkSize, yy / Chunk.chunkSize) && MainFile.game.getServer().getWorld().getChunk(xx, yy) != null && MainFile.game.getServer().getWorld().getChunk(xx, yy).shouldBeLoaded()) {

						float blockX = (float) (((xx) - plPos.x) + ConfigValues.renderRange);
						float blockY = (float) (((yy) - plPos.y) + ConfigValues.renderRange);

						LightUnit bb = world.getLightUnit(xx, yy);

						int height = (MainFile.game.getServer().getWorld().getBiome(xx).getHeight(xx));
						float plHeight = (float) plPos.y;

						Image im = getBlockImageFromDepth(yy, height, xx, yy);
						if (im == null) continue;

						im.draw((int) ((blockX) * ConfigValues.size), (int) ((blockY) * ConfigValues.size), ConfigValues.size, ConfigValues.size);

						if (bb != null) {
							float t = (float) bb.getLightValue() / (float) ILightSource.MAX_LIGHT_STRENGTH;

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

						DefaultBlockRendering.drawFront(g2, (int) ((blockX) * ConfigValues.size), (int) ((blockY) * ConfigValues.size), new Color(0, 0, 0, 0.4F));

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
		if(MainFile.game.getServer().getWorld() != null){
			if(MainFile.game.getServer().getWorld().getStructure(x, y) != null) {
				Structure st = MainFile.game.getServer().getWorld().getStructure(x, y);

				if (st.name.toLowerCase().contains("dungeon")) {

					if (st.name.contains("1")) {
						return Blocks.blockCrackedStone.getBlockTextureFromSide(EnumBlockSide.FRONT, MainFile.game.getServer().getWorld(), x, height);

					} else if (st.name.contains("2")) {
						return Blocks.blockBlueDungeonBricks.getBlockTextureFromSide(EnumBlockSide.FRONT, MainFile.game.getServer().getWorld(), x, height);

					} else if (st.name.contains("3")) {
						return Blocks.blockGreenDungeonBricks.getBlockTextureFromSide(EnumBlockSide.FRONT, MainFile.game.getServer().getWorld(), x, height);

					} else if (st.name.contains("4")) {
						return Blocks.blockRedDungeonBricks.getBlockTextureFromSide(EnumBlockSide.FRONT, MainFile.game.getServer().getWorld(), x, height);

					} else if (st.name.contains("5")) {
						return Blocks.blockYellowDungeonBricks.getBlockTextureFromSide(EnumBlockSide.FRONT, MainFile.game.getServer().getWorld(), x, height);

					}

				}
			}
		}

		if(depth > height && depth < (height + Chunk.chunkSize)) return Blocks.blockDirt.getBlockTextureFromSide(EnumBlockSide.FRONT, null, 0,0);
		if(depth >= (height + Chunk.chunkSize)) return Blocks.blockStone.getBlockTextureFromSide(EnumBlockSide.FRONT, null, 0,0);

		return null;
	}

}
