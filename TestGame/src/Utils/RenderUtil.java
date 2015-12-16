package Utils;
/*
* Project: Random Java Creations
* Package: Utils
* Created: 26.07.2015
*/


import Main.MainFile;
import WorldFiles.EnumWorldTime;
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

	public static org.newdawn.slick.Image getBlockImage( String blockID ) {
		try {

			InputStream stream = MainFile.class.getResourceAsStream("../textures/blocks/" + blockID + ".png");
			return new Image(stream, blockID, false);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Image getItemImage( String itemID ) {
		try {

			InputStream stream = MainFile.class.getResourceAsStream("../textures/items/" + itemID + ".png");
			return new Image(stream, itemID, false);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
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

	public static void darkenColorBasedOnTime( Graphics g2 ) {
		if (MainFile.currentWorld != null) if (MainFile.currentWorld.worldTimeOfDay == EnumWorldTime.EVENING) {
			g2.setColor(g2.getColor().darker());

		} else if (MainFile.currentWorld.worldTimeOfDay == EnumWorldTime.NIGHT) {
			g2.setColor(g2.getColor().darker().darker());
		}
	}

}
