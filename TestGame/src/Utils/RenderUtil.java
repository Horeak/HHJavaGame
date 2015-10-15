package Utils;
/*
* Project: Random Java Creations
* Package: Utils
* Created: 26.07.2015
*/

import Blocks.Util.Block;
import Main.MainFile;
import WorldFiles.EnumWorldTime;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;

public class RenderUtil
{

	public static BufferedImage getBlockImage(String blockID){
		try {

			InputStream stream = MainFile.class.getResourceAsStream("../textures/blocks/" + blockID + ".png");
			BufferedImage image = ImageIO.read(stream);
			return image;

		}catch (Exception e){
			e.printStackTrace();
		}

		return null;
	}

	public static BufferedImage getItemImage(String itemID){
		try {

			InputStream stream = MainFile.class.getResourceAsStream("../textures/items/" + itemID + ".png");
			BufferedImage image = ImageIO.read(stream);
			return image;

		}catch (Exception e){
			e.printStackTrace();
		}

		return null;
	}

	public static HashMap<Graphics2D, Font> tempFontStore = new HashMap<>();

	public static void resizeFont(Graphics2D g2, int size){
		Font temp = g2.getFont();

		if(tempFontStore.get(g2) == null)
		tempFontStore.put(g2, temp);

		Font fnt = new Font(temp.getName(), temp.getStyle(), size);
		g2.setFont(fnt);
	}

	public static void changeFontStyle(Graphics2D g2, int style){
		Font temp = g2.getFont();

		if(tempFontStore.get(g2) == null)
		tempFontStore.put(g2, temp);

		Font fnt = new Font(temp.getName(), style, temp.getSize());
		g2.setFont(fnt);
	}

	public static void changeFontName(Graphics2D g2, String name){
		Font temp = g2.getFont();

		if(tempFontStore.get(g2) == null)
		tempFontStore.put(g2, temp);

		Font fnt = new Font(name, temp.getStyle(), temp.getSize());
		g2.setFont(fnt);
	}

	public static void resetFont(Graphics2D g2){
		Font temp = tempFontStore.get(g2);
		g2.setFont(temp);
	}

	public static void renderBlock(Graphics2D g2, Block block, int x, int y){
		if (ConfigValues.simpleBlockRender) {
			BlockUtils.renderDefaultBlockDebug(g2, block, x, y);
		} else {
			block.renderBlock(g2, x, y);
		}
	}

	public static void renderDefault2_5DBlock(Graphics2D g2, Block block, int x, int y, boolean top, boolean right){
			if (right) {
				int xStart = x + ConfigValues.size, yStart = y;

				Path2D.Double path = new Path2D.Double();

				path.moveTo(xStart, yStart);

				path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
				path.lineTo(xStart + (ConfigValues.size / 2), yStart + (ConfigValues.size / 2));
				path.lineTo(xStart, yStart + ConfigValues.size);

				path.closePath();

				g2.setColor(block.getDefaultBlockColor().darker());
				darkenColorBasedOnTime(g2);

				g2.fill(path);
			}

			if (top) {
				int xStart = x, yStart = y;

				Path2D.Double path = new Path2D.Double();

				path.moveTo(xStart, yStart);
				path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
				path.lineTo(xStart + (ConfigValues.size + (ConfigValues.size / 2)), yStart - (ConfigValues.size / 2));
				path.lineTo(xStart + ConfigValues.size, yStart);

				path.closePath();

				g2.setColor(block.getDefaultBlockColor().brighter());

				if(MainFile.currentWorld != null)
				if (MainFile.currentWorld.worldTimeOfDay == EnumWorldTime.EVENING) {
					g2.setColor(g2.getColor().darker());

				} else if (MainFile.currentWorld.worldTimeOfDay == EnumWorldTime.NIGHT){
					g2.setColor(g2.getColor().darker().darker());
				}

				g2.fill(path);
			}
		}


	public static void darkenColorBasedOnTime(Graphics2D g2){
		if(MainFile.currentWorld != null)
			if (MainFile.currentWorld.worldTimeOfDay == EnumWorldTime.EVENING) {
				g2.setColor(g2.getColor().darker());

			} else if (MainFile.currentWorld.worldTimeOfDay == EnumWorldTime.NIGHT){
				g2.setColor(g2.getColor().darker().darker());
			}
	}

}
