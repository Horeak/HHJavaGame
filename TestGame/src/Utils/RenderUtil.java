package Utils;
/*
* Project: Random Java Creations
* Package: Utils
* Created: 26.07.2015
*/


import Blocks.BlockRender.IBlockRenderer;
import Blocks.Util.Block;
import Items.IItem;
import Main.MainFile;
import Render.EnumRenderMode;
import WorldFiles.EnumWorldTime;
import WorldFiles.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.*;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

public class RenderUtil {

	public static HashMap<Graphics, Font> tempFontStore = new HashMap<>();
	public static HashMap<Font, UnicodeFont> store = new HashMap<>();

	public static org.newdawn.slick.Image getImage( String folder, String id ) {
		try {

			InputStream stream = MainFile.class.getResourceAsStream("../textures/" + folder + "/" + id + ".png");
			return new Image(stream, id, false);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static org.newdawn.slick.Image getBlockImage( String blockID ) {
		return getImage("blocks", blockID);
	}

	public static Image getItemImage( String itemID ) {
		return getImage("items", itemID);
	}


	//This is made because of IDE specific features related to java.awt.Color which is not compatible with org.newdawn.slick.Color
	public static org.newdawn.slick.Color getColorToSlick( Color c ) {
		return new org.newdawn.slick.Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}


	//Overcomplicated font system because of Slick2D having to remake everything....
	public static UnicodeFont getFont( Font font ) {
		if (font != null) {

			if (!store.containsKey(font)) {
				UnicodeFont fontt = new UnicodeFont(font);

				try {
					fontt.addAsciiGlyphs();
					fontt.getEffects().add(new ColorEffect());
					fontt.loadGlyphs();
				} catch (SlickException e) {
					e.printStackTrace();
				}
				store.put(font, fontt);


				return fontt;
			} else {
				return store.get(font);
			}
		}

		return null;
	}

	public static void setFont( Graphics g2, org.newdawn.slick.Font font ) {
		g2.setFont(font);
	}

	public static Font getFontFromSlick( org.newdawn.slick.Font font ) {
		if (font instanceof UnicodeFont) {
			UnicodeFont font1 = (UnicodeFont) font;

			try {
				Field field = font.getClass().getDeclaredField("font");
				field.setAccessible(true);

				Font ft = (Font) field.get(font1);

				return ft;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	public static void resizeFont( Graphics g2, int size ) {
		Font temp = getFontFromSlick(g2.getFont());

		if (temp != null) {
			if (tempFontStore.get(g2) == null) tempFontStore.put(g2, temp);

			Font fnt = new Font(temp.getName(), temp.getStyle(), size);
			setFont(g2, getFont(fnt));
		}
	}

	public static void changeFontStyle( Graphics g2, int style ) {
		Font temp = getFontFromSlick(g2.getFont());

		if (temp != null) {
			if (tempFontStore.get(g2) == null) tempFontStore.put(g2, temp);

			Font fnt = new Font(temp.getName(), style, temp.getSize());
			setFont(g2, getFont(fnt));
		}
	}

	public static void changeFontName( Graphics g2, String name ) {
		Font temp = getFontFromSlick(g2.getFont());

		if (temp != null) {
			if (tempFontStore.get(g2) == null) tempFontStore.put(g2, temp);

			Font fnt = new Font(name, temp.getStyle(), temp.getSize());
			setFont(g2, getFont(fnt));
		}
	}


	public static void resetFont( Graphics g2 ) {
		Font temp = tempFontStore.get(g2);

		if (temp != null) setFont(g2, getFont(temp));
	}

	public static org.newdawn.slick.Color getColorBasedOnLight( org.newdawn.slick.Color c, World world, int x, int y ) {
		if (MainFile.currentWorld != null) if (MainFile.currentWorld.worldTimeOfDay == EnumWorldTime.EVENING) {
			return c.darker();

		} else if (MainFile.currentWorld.worldTimeOfDay == EnumWorldTime.NIGHT) {
			return c.darker().darker();
		}


		return c;
	}


	public static org.newdawn.slick.Color getColorWithAlpha( org.newdawn.slick.Color c, float alpha ) {
		return new org.newdawn.slick.Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}

	public static void renderItem( Graphics g2, IItem item, int x, int y, EnumRenderMode mode ) {
		if (item != null && item.getRender() != null) {
			if (item instanceof Block) {
				((IBlockRenderer) (item.getRender())).renderBlock(g2, x, y, mode, (Block) item, true, true, false, true);
			} else {
				item.getRender().renderItem(g2, x, y, mode, item);
			}
		}
	}

}
