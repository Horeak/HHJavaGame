package Utils;


import Render.EnumRenderMode;

public class ConfigValues {


	public static final String gameTitle = "Test Game";

	//Final Settings
	public static final boolean RENDER_BLOCKS = true;
	public static final boolean RENDER_ENTITIES = true;
	public static final boolean RENDER_BACKGROUND = true;
	public static final boolean RENDER_HOTBAR = true;

	public static final boolean PAUSE_GAME_IN_GUI = true;
	public static final boolean PAUSE_GAME_IN_INV = false;

	public static final boolean SPLIT_LOG_FILES = false;

	//Render and debug options
	public static boolean debug = false;
	public static boolean simpleBlockRender = false;

	public static boolean renderChunks = false;
	public static boolean renderChunkColors = false;
	public static boolean renderStructureBounds = false;

	//Sizes and random values
	public static int renderXSize = 25;
	public static int renderYSize = 25;

	public static int renderDistance = 16;
	public static int renderRange = ((renderXSize + renderYSize) / 2) / 2;
	public static int hotbarRenderSize = 54;

	public static int size = 32;
	public static EnumRenderMode renderMod = EnumRenderMode.render2_5D;

	public static float brightness = 1F; //Higher value = darker
	public static int lightUpdateRenderRange = 64;

}
