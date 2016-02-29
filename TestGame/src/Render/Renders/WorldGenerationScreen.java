package Render.Renders;

import BlockFiles.Util.Block;
import Main.MainFile;
import Rendering.AbstractWindowRender;
import Utils.ConfigValues;
import Utils.FontHandler;
import WorldFiles.EnumWorldSize;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;


public class WorldGenerationScreen extends AbstractWindowRender {

	//TODO Improve generation screen!
	//TODO Worldgenerationscreen disabled as it might have been causing generation issues!

	//TODO Find an alternative method to render the generation which is not as heavy as this one
	public static String generationStatus = "";
	String text = ".";
	int tt = 0;

	@Override
	public void render( Graphics g2 ) {
		g2.setClip(MainFile.blockRenderBounds);

		g2.setColor(MainFile.game.getServer().getWorld().worldTimeOfDay.SkyColor);
		g2.fill(MainFile.blockRenderBounds);

//		float h = ((MainFile.game.getServer().getWorld().worldSize.xSize) * ConfigValues.size) / MainFile.xWindowSize;
//		World world = MainFile.game.getServer().getWorld();

		//Disable Due to it loading chunks while generating causing ConcurrentModificationError and this render wont be possible with infinite worlds
//		g2.pushTransform();
////		g2.scale(1 / h, 1 / h);
//		g2.translate(0, (MainFile.yWindowSize / 4));
//
//		float g = (1f / h);
//		int temP = world.worldSize == EnumWorldSize.LARGE ? 10 : world.worldSize == EnumWorldSize.MEDIUM ? 2 : 1;
//
//		for(int x = 0; x < MainFile.game.getServer().getWorld().worldSize.xSize; x += temP){
//			for(int y = 0; y < MainFile.game.getServer().getWorld().worldSize.ySize; y += temP){
//				Block bl = MainFile.game.getServer().getWorld().getBlock(x, y);
//
//				if(bl != null){
////					((DefaultBlockRendering)bl.getRender()).renderBlock(g2, x * ConfigValues.size, y * ConfigValues.size, EnumRenderMode.render2D, bl, false, false, false, false, world, x, y);
//					float size = (g * ConfigValues.size);
//
//					g2.setColor(bl.getDefaultBlockColor());
//					g2.fill(new Rectangle((x * size), (y * size), size * temP, size * temP));
//				}
//			}
//		}
//		g2.popTransform();

		g2.setColor(new Color(0.2F, 0.2F, 0.2F, 0.5F));
		g2.fill(new Rectangle(0, (MainFile.yWindowSize / 2) - 15, MainFile.xWindowSize, 85));


		g2.setColor(Color.white);
		FontHandler.resizeFont(g2, 24);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Generating world", 0, (MainFile.yWindowSize / 2) - 10, MainFile.xWindowSize, g2.getColor());
		org.newdawn.slick.util.FontUtils.drawLeft(g2.getFont(), text, (MainFile.xWindowSize / 2) + 100, (MainFile.yWindowSize / 2) - 10);
		FontHandler.resetFont(g2);


		if(generationStatus != null && generationStatus.contains("-|-")) {
			String[] tg = generationStatus.split("-\\|-", 2);

			g2.setColor(Color.white);
			FontHandler.resizeFont(g2, 16);
			org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Currently generating: " + tg[ 1 ] + (ConfigValues.debug ? " - " + tg[ 0 ] : ""), 0, (MainFile.yWindowSize / 2) + 25, MainFile.xWindowSize, g2.getColor());
			FontHandler.resetFont(g2);
		}

//		g2.setColor(Color.white);
//		FontHandler.resizeFont(g2, 12);
//		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Displaying generation", 0, (MainFile.yWindowSize / 2) + 45, MainFile.xWindowSize, g2.getColor());
//		FontHandler.resetFont(g2);

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
		return MainFile.game.getServer().getWorld() != null && MainFile.game.getServer().getWorld().generating && false;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}
}
