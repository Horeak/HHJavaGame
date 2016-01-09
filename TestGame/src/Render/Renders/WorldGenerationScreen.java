package Render.Renders;

import Blocks.Util.Block;
import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import Utils.RenderUtil;
import WorldFiles.EnumWorldSize;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;


public class WorldGenerationScreen extends AbstractWindowRender {

	public static String generationStatus = "";
	String text = ".";
	int tt = 0;

	@Override
	public void render( Graphics g2 ) {
		g2.setClip(MainFile.blockRenderBounds);

		g2.setColor(MainFile.getServer().getWorld().worldTimeOfDay.SkyColor);
		g2.fill(MainFile.blockRenderBounds);

		float h = ((MainFile.getServer().getWorld().worldSize.xSize + 32) * ConfigValues.size) / MainFile.xWindowSize;
		World world = MainFile.getServer().getWorld();

		g2.pushTransform();
		g2.scale(1 / h, 1 / h);
		g2.translate(MainFile.xWindowSize / 4, (MainFile.yWindowSize / 4) * (h));

		int size = world.worldSize == EnumWorldSize.LARGE ? 9 : world.worldSize == EnumWorldSize.MEDIUM ? 2 : 1;

		for(int x = 0; x < MainFile.getServer().getWorld().worldSize.xSize; x += size){
			for(int y = 0; y < MainFile.getServer().getWorld().worldSize.ySize; y += size){
				Block bl = MainFile.getServer().getWorld().getBlock(x, y);

				if(bl != null){
//					((DefaultBlockRendering)bl.getRender()).renderBlock(g2, x * ConfigValues.size, y * ConfigValues.size, EnumRenderMode.render2D, bl, false, false, false, false, world, x, y);
					g2.setColor(bl.getDefaultBlockColor());
					g2.fill(new Rectangle((x * ConfigValues.size), (y * ConfigValues.size), ConfigValues.size * size, ConfigValues.size * size));
				}
			}
		}
		g2.popTransform();

		g2.setColor(new Color(0.2F, 0.2F, 0.2F, 0.5F));
		g2.fill(new Rectangle(0, (MainFile.yWindowSize / 2) - 15, MainFile.xWindowSize, 85));


		g2.setColor(Color.white);
		RenderUtil.resizeFont(g2, 24);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		FontUtils.drawCenter(g2.getFont(), "Generating world", 0, (MainFile.yWindowSize / 2) - 10, MainFile.xWindowSize, g2.getColor());
		FontUtils.drawLeft(g2.getFont(), text, (MainFile.xWindowSize / 2) + 100, (MainFile.yWindowSize / 2) - 10);
		RenderUtil.resetFont(g2);


		if(generationStatus != null && generationStatus.contains("-|-")) {
			String[] tg = generationStatus.split("-\\|-", 2);

			g2.setColor(Color.white);
			RenderUtil.resizeFont(g2, 16);
			FontUtils.drawCenter(g2.getFont(), "Currently generating: " + tg[ 1 ] + (ConfigValues.debug ? " - " + tg[ 0 ] : ""), 0, (MainFile.yWindowSize / 2) + 25, MainFile.xWindowSize, g2.getColor());
			RenderUtil.resetFont(g2);
		}

		g2.setColor(Color.white);
		RenderUtil.resizeFont(g2, 12);
		FontUtils.drawCenter(g2.getFont(), "Displaying generation", 0, (MainFile.yWindowSize / 2) + 45, MainFile.xWindowSize, g2.getColor());
		RenderUtil.resetFont(g2);

		if (tt >= 10) {
			tt = 0;

			if (text.contains(".....")) {
				text = ".";
			} else {
				text = text + ".";
			}
		} else {
			tt += 1;
		}
	}

	@Override
	public boolean canRender() {
		return MainFile.getServer().getWorld().generating;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}
}
