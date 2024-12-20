package Main;

import Crafting.CraftingRegister;
import GameFiles.BaseGame;
import Guis.GuiCrafting;
import Guis.GuiCreativeMenu;
import Guis.GuiIngameMenu;
import Guis.GuiInventory;
import Guis.Interfaces.MainMenu;
import Interface.Gui;
import Render.Renders.*;
import Rendering.AbstractWindowRender;
import Settings.ConfigFile;
import Settings.Values.KeybindingAction;
import Sided.Client;
import Sided.Server;
import Sounds.SoundLoader;
import Utils.*;
import Utils.TexutrePackFiles.TextureLoader;
import Utils.TexutrePackFiles.TexturePack;
import WorldFiles.GameMode;
import WorldGeneration.Util.DungeonLootGenerator;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.Font;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFile extends BaseGame {
	//TODO Add chat to allow ingame commands like gamemode change and teleport

	//TODO Add scroll bars to all settings menues
	//TODO Start optimizing the game. Remove all unessecary creation of new veriables. (For example creating a new rectangle each rendering call)
	public static Rectangle blockRenderBounds = new Rectangle(0,0, (ConfigValues.renderXSize * ConfigValues.size) - ConfigValues.renderXSize, (ConfigValues.renderYSize * ConfigValues.size));
	public static int xWindowSize = (ConfigValues.renderXSize * ConfigValues.size) - 25;
	public static int yWindowSize = (ConfigValues.renderYSize * ConfigValues.size);

	public static String Title = "Test Game";

	public static MainFile game = new MainFile(Title, xWindowSize, yWindowSize, false);
	public static Random random = new Random();

	public static boolean hasScrolled = false;
	public static int debugSize = 265;

	public static TexturePack defaultTexturePack = null;

	private TextureLoader imageLoader = new TextureLoader(this);
	public TexturePack texturePack;

	public SoundLoader soundLoader = new SoundLoader();

	public static boolean inDebugMode = false;
	
	private Client client;
	private Server server;

	//TODO START WORKING ON FUCKING WINDOW RESIZING FOR FUCKS SAKE!

	public MainFile( String title, int xSize, int ySize, boolean fullscreen ) {
		super(title, xSize, ySize, fullscreen);
	}

	public static void main( String[] args ) {
		try {

			Display.setResizable(false);

			String debugGame = System.getProperty("debugGame");
			inDebugMode = debugGame != null && debugGame.equalsIgnoreCase("true");

			game.gameContainer.setAlwaysRender(true);
			game.gameContainer.setShowFPS(false);
			game.gameContainer.setVSync(true);

			game.gameContainer.setMinimumLogicUpdateInterval(25);
			game.gameContainer.setTargetFrameRate(300);
			
			game.gameContainer.start();
		} catch (Exception ex) {
			Logger.getLogger(MainFile.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		LoggerUtil.activate(title);
		LoggerUtil.activateLogFile("logs/", this);
		LoggerUtil.out.log(Level.INFO, "Log file activated.");

		try {
			ExtractFolderController.extractFolder(getClass().getResource("../extractFolder/").getPath().replace("%20", " "));
		}catch (Exception e){
			LoggerUtil.exception(e);
		}

		FileUtil.worlds = FileUtil.getSavedWorlds();
		FileUtil.texturePacks = FileUtil.getTexturePacks();

		LoggerUtil.out.log(Level.INFO, "Loaded texturepacks and worlds!");


		super.init(container);
	}

	@Override
	public void initGame( GameContainer container ) throws SlickException {

		if(inDebugMode)
		LoggerUtil.out.log(Level.INFO, "Game debug mode is Enabled");

		if(FileUtil.getTexturePackByName("Default") != null){
			defaultTexturePack = FileUtil.getTexturePackByName("Default");

		}else{
			defaultTexturePack = new TexturePack("Default", getClass().getResource((game.getExtractFolder() + "/texturepacks/Default/")).getPath().replace("%20", " "));
			FileUtil.texturePacks.add(defaultTexturePack);
		}

		if(texturePack == null)
			texturePack = defaultTexturePack;

		//TODO Save player name!
		client = new Client(saveUtil.getDataHandler("config/settings.cfg").getStringDefault("playerName", "Player"));
		server = new Server();

		LoggerUtil.out.log(Level.INFO, "Client and Server instance initiated.");
		
		Registrations.registerGenerations();
		CraftingRegister.registerRecipes();

		imageLoader.reloadTextures();
		DungeonLootGenerator.addLootTable();

		setCurrentMenu(new MainMenu());

		container.getInput().addMouseListener(new MouseListener() {
			public void mouseWheelMoved( int change ) {
				if (getCurrentMenu() == null) {
					if (!hasScrolled) {
						HotbarRender.slotSelected += (change / -120);

						if (HotbarRender.slotSelected > 10) {
							HotbarRender.slotSelected = 1;
						}
						if (HotbarRender.slotSelected <= 0) {
							HotbarRender.slotSelected = 10;
						}

						hasScrolled = true;
					} else {
						hasScrolled = false;
					}
				}
			}

			public void mouseClicked( int button, int x, int y, int clickCount ) {}
			public void mousePressed( int button, int x, int y ) {}
			public void mouseReleased( int button, int x, int y ) {}
			public void mouseMoved( int oldx, int oldy, int newx, int newy ) {}
			public void mouseDragged( int oldx, int oldy, int newx, int newy ) {}
			public void setInput( Input input ) {}
			public boolean isAcceptingInput() {
				return true;
			}
			public void inputEnded() {}
			public void inputStarted() {
			}
		});


	}


	//TODO Make the KeybindingAction an part of the actual Keybinding instance?
	public void addKeybindings(){
		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("inventory")) {
			@Override
			public void performAction() {
				if(MainFile.game.getServer().getWorld() != null && getCurrentMenu() == null)
				setCurrentMenu(new GuiInventory(gameContainer, ConfigValues.PAUSE_GAME_IN_INV));
			}
		});

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("crafting")) {
			@Override
			public void performAction() {
				if(MainFile.game.getServer().getWorld() != null && getCurrentMenu() == null)
				setCurrentMenu(new GuiCrafting(gameContainer, ConfigValues.PAUSE_GAME_IN_INV));
			}
		});

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("creative")) {
			@Override
			public void performAction() {
				if(MainFile.game.getServer().getWorld() != null && getCurrentMenu() == null) {
					if(MainFile.game.getClient().getPlayer().playerGameMode.canUseCreativeMenu) {
						setCurrentMenu(new GuiCreativeMenu(gameContainer, ConfigValues.PAUSE_GAME_IN_INV));
					}
				}

			}
		});

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("drop")) {
			@Override
			public void performAction() {
				if(MainFile.game.getServer().getWorld() != null && getCurrentMenu() == null)
				getClient().getPlayer().dropItem();

			}
		});

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("exit")) {
			@Override
			public void performAction() {
				if(MainFile.game.getServer().getWorld() != null && getCurrentMenu() == null)
				setCurrentMenu(new GuiIngameMenu(gameContainer, ConfigValues.PAUSE_GAME_IN_GUI));
			}
		});


		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("flight.toggle")) {
			@Override
			public void performAction() {
				if(getClient().getPlayer().playerGameMode == GameMode.CREATIVE){
					getClient().getPlayer().flying ^= true;
				}
			}
		});



		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("chunkRender")) {
			@Override
			public void performAction() {
				if(!ConfigValues.debug) return;

				ConfigValues.renderChunks ^= true;
				ConfigValues.renderChunkColors ^= true;
			}
		});

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("structureBounds")) {
			@Override
			public void performAction() {
				if(!ConfigValues.debug) return;

				ConfigValues.renderStructureBounds ^= true;

			}
		});
	}


	@Override
	public void updateGame( GameContainer container, int delta ) throws SlickException {
		if (!(getCurrentMenu() instanceof Gui)) {
			updateKeys(container, delta);
			BlockAction.update(container, delta);

			//TODO Need to improve the break time....
			if(BlockAction.timeSince >= (BlockAction.blockBreakReach / 2)){
				BlockAction.blockBreakDelay = 0;
				BlockAction.blockDamage = 0;
				BlockAction.prevX = -1;
				BlockAction.prevY = -1;
			}else{
				BlockAction.timeSince += 1;
			}
		}

		if(!inDebugMode && ConfigValues.debug){
			ConfigValues.debug = false;
		}

		//TODO Why?...
		if (container.isPaused() && !(getCurrentMenu() instanceof Gui)) {
			container.setPaused(false);
		}
	}


	//TODO Add screenshot feature. (Just make it where it creates an image from the Graphics instance at the end of the render function)
	@Override
	public void render( GameContainer container, Graphics g2 ) throws SlickException {
		if (g2.getFont() instanceof AngelCodeFont) {
			g2.setFont(FontHandler.getFont(new Font("Arial", 0, 0)));
		}

		g2.setBackground(Color.lightGray);
		g2.setClip(blockRenderBounds);

		for (AbstractWindowRender render : getAbstractWindowRenderers()) {
			Color tempC = g2.getColor();

			if ((getCurrentMenu()instanceof Gui) ||getCurrentMenu()== null || render.canRenderWithWindow()) {
				if (render.canRender()) {
					render.render(g2);
				}
			}

			g2.setColor(tempC);
		}

		g2.setBackground(Color.lightGray);
		g2.setClip(blockRenderBounds);

		Color tempC = g2.getColor();

		if (getCurrentMenu()!= null) {
			if (getCurrentMenu().canRender()) {
				getCurrentMenu().render(g2);
				g2.setClip(null);

				getCurrentMenu().renderObject(g2);
				g2.setClip(null);

				if (getCurrentMenu()instanceof Gui) {
					((Gui) getCurrentMenu()).renderPost(g2);
				}
				g2.setClip(null);
			}
		}
	}

	public static void reloadTextures(){
		game.imageLoader.reloadTextures();
	}

	@Deprecated
	public TextureLoader getImageLoader(){
		return imageLoader;
	}

	@Override
	public void renderGame( GameContainer container, Graphics g2 ) throws SlickException {
	}

	@Override
	public void loadGame() {
		String t = saveUtil.getDataHandler("config/settings.cfg").getString("texturePackName");

		if(FileUtil.getTexturePackByName(t) != null){
			MainFile.game.texturePack = FileUtil.getTexturePackByName(t);
		}
	}

	@Override
	public void closeGame() {
		try {
			saveUtil.getDataHandler("config/settings.cfg").setObject("texturePackName", texturePack.name);

			//TODO Is player instance being reset before this can be saved?
			if(getClient().getPlayer() != null) {
				saveUtil.getDataHandler("config/settings.cfg").setObject("playerName", getClient().playerId);
			}
		}catch (Exception e){
			LoggerUtil.exception(e);
		}
	}

	@Override
	public void keyPressed( int key, char c ) {
		super.keyPressed(key, c);

		if(getCurrentMenu()== null && getServer().getWorld() != null) {
			if (Character.isDigit(c)) {
				Integer tt = Integer.parseInt(c + "");
				if (tt >= 0 && tt < 10) {
					if(tt == 0) tt = 10;

					HotbarRender.slotSelected = tt;
				}
			}
		}
	}

	public static ConfigFile file = new ConfigFile();
	@Override
	public ConfigFile getConfig() {
		return file;
	}

	@Override
	public String getTextureLocation() {
		return null;
	}

	public String getExtractFolder(){
		return "../extractFolder";
	}

	@Override
	public String getFilesSaveLocation() {
		return System.getProperty("user.home")+"/Documents/" + Title + "/";
	}

	public static AbstractWindowRender[] renders;

	@Override
	public Rendering.AbstractWindowRender[] getAbstractWindowRenderers() {
		if(renders == null){
			renders = new AbstractWindowRender[]{
					new BackgroundRender(), new BackgroundBlockRender(), new BlockRendering(), new EntityRendering(),
					new BlockSelectionRender(), new HotbarRender(), new DebugInfoRender()};
		}

		return renders;
	}

	//TODO Make all keybindings have an option to allow held down functionality
	public void updateKeys( GameContainer gameContainer, int delta ) {

		float tt = (float)delta / 25;
		float t = 0.14f * tt;

		if (getServer().getWorld() != null) {
			if (gameContainer.getInput().isKeyPressed(getConfig().getKeybindFromID("left.walk").getKey()) || gameContainer.getInput().isKeyDown(getConfig().getKeybindFromID("left.walk").getKey())) {
				if (getClient().getPlayer().facing == 1) {
					getClient().getPlayer().moveTo(getClient().getPlayer().getEntityPostion().x - t, getClient().getPlayer().getEntityPostion().y);
				}

				getClient().getPlayer().facing = 1;

			} else if (gameContainer.getInput().isKeyPressed(getConfig().getKeybindFromID("right.walk").getKey()) || gameContainer.getInput().isKeyDown(getConfig().getKeybindFromID("right.walk").getKey())) {
				if (getClient().getPlayer().facing == 2) {
					getClient().getPlayer().moveTo(getClient().getPlayer().getEntityPostion().x + t, getClient().getPlayer().getEntityPostion().y);
				}

				getClient().getPlayer().facing = 2;
			}


			if (gameContainer.getInput().isKeyPressed(getConfig().getKeybindFromID("jump.walk").getKey()) || gameContainer.getInput().isKeyDown(getConfig().getKeybindFromID("jump.walk").getKey()) && (delta & 700) != 0) {
				if (getClient().getPlayer().isOnGround || getClient().getPlayer().flying) {
					if (getClient().getPlayer().moveTo(getClient().getPlayer().getEntityPostion().x, getClient().getPlayer().getEntityPostion().y - 1F)) {
						getClient().getPlayer().isOnGround = false;
					}
				}
			}

			if (gameContainer.getInput().isKeyPressed(getConfig().getKeybindFromID("flight.down").getKey()) || gameContainer.getInput().isKeyDown(getConfig().getKeybindFromID("flight.down").getKey()) && (delta & 700) != 0) {
				if (!getClient().getPlayer().isOnGround) {
					getClient().getPlayer().moveTo(getClient().getPlayer().getEntityPostion().x, getClient().getPlayer().getEntityPostion().y + 1F);
				}
			}
		}
	}

	public Client getClient() {
		return client;
	}

	public Server getServer() {
		return server;
	}

	@Override
	public boolean closeRequested()
	{
		if(getServer().getWorld() != null){
			getServer().getWorld().stop();
		}
		
		return super.closeRequested();
	}
}




