package Utils;


import Render.EnumRenderMode;

public class ConfigValues {


	public static final String gameTitle = "Test Game";

	public static boolean RENDER_BLOCKS = true;
	public static boolean RENDER_ENTITIES = true;
	public static boolean RENDER_BACKGROUND = true;

	public static boolean RENDER_HOTBAR = true;

	//TODO Add screen resize (Test screen size with Macbook Air and windows laptop)
	public static int renderXSize = 25;
	public static int renderYSize = 25;

	public static int renderDistance = 16;
	public static int renderRange = ((renderXSize + renderYSize) / 2) / 2;
	public static int hotbarRenderSize = 56;

	public static boolean resizeable = false;

	public static int size = 32;

	public static boolean debug = false;
	public static boolean simpleBlockRender = false;

	public static EnumRenderMode renderMod = EnumRenderMode.render2_5D;

}
