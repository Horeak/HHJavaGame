package Utils;


import Render.EnumRenderMode;

public class ConfigValues {


	public static final String gameTitle = "Test Game";

	public static boolean RENDER_BLOCKS = true;
	public static boolean RENDER_ENTITIES = true;
	public static boolean RENDER_BACKGROUND = true;
	public static boolean RENDER_HOTBAR = true;
	public static boolean RENDER_MINIMAP = true;

	public static boolean PAUSE_GAME_IN_GUI = true;
	public static boolean PAUSE_GAME_IN_INV = false;

	public static int renderXSize = 25;
	public static int renderYSize = 25;

	public static int renderDistance = 16;
	public static int renderRange = ((renderXSize + renderYSize) / 2) / 2;
	public static int hotbarRenderSize = 54;

	public static int lightUpdateRenderRange = 64;

	public static int size = 32;

	public static boolean debug = false;
	public static boolean simpleBlockRender = false;

	public static boolean renderChunks = false;
	public static boolean renderChunkColors = false;

	public static EnumRenderMode renderMod = EnumRenderMode.render2_5D;

	public static String MinimapSize = "Normal";

	public static float brightness = 1F; //Higher value = darker

}
