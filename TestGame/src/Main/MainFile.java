package Main;

import BlockFiles.BlockRender.DefaultBlockRendering;
import BlockFiles.Util.Block;
import Crafting.CraftingRegister;
import GameFiles.BaseGame;
import Guis.GuiCrafting;
import Guis.GuiIngameMenu;
import Guis.GuiInventory;
import Guis.Interfaces.MainMenu;
import Interface.Gui;
import Items.Utils.ItemStack;
import Render.EnumRenderMode;
import Render.Renders.*;
import Rendering.AbstractWindowRender;
import Settings.ConfigFile;
import Settings.Values.KeybindingAction;
import Sided.Client;
import Sided.Server;
import Utils.*;
import Utils.TexutrePackFiles.TextureLoader;
import Utils.TexutrePackFiles.TexturePack;
import WorldFiles.Chunk;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.imageout.ImageOut;

import java.awt.*;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFile extends BaseGame {
	//TODO Add big map (fullscreen/gui) (similar to Terraria)

	//TODO Start optimizing the game. Remove all unessecary creation of new veriables. (For example creating a new rectangle each rendering call)
	public static Rectangle blockRenderBounds = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size) - ConfigValues.renderXSize, (ConfigValues.renderYSize * ConfigValues.size));
	public static int xWindowSize = (ConfigValues.renderXSize * ConfigValues.size) - 25;
	public static int yWindowSize = (ConfigValues.renderYSize * ConfigValues.size);

	public static String Title = "Test Game";

	public static MainFile game = new MainFile(Title, xWindowSize, yWindowSize, false);
	public static Random random = new Random();

	public static boolean hasDebugSize = false;
	public static boolean hasScrolled = false;
	public static int debugSize = 265;

	public static TexturePack defaultTexturePack = null;

	public TextureLoader imageLoader = new TextureLoader(this);
	public TexturePack texturePack;

	
	private Client client;
	private Server server;

	public MainFile( String title, int xSize, int ySize, boolean fullscreen ) {
		super(title, xSize, ySize, fullscreen);
	}

//TODO Add key which generates an image which shows playerlocation and chunk colors similar to debug feature in BlockRendering. make it use same size as the world.
	public static void main( String[] args ) {
		try {

			game.gameContainer.setAlwaysRender(true);
			game.gameContainer.setShowFPS(false);
			game.gameContainer.setVSync(true);
			game.gameContainer.start();
		} catch (Exception ex) {
			Logger.getLogger(MainFile.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		LoggerUtil.activate(title);
		LoggerUtil.activateLogFile("log/", this);

		super.init(container);
	}

	@Override
	public void initGame( GameContainer container ) throws SlickException {
		LoggerUtil.out.log(Level.INFO, "Log file activated.");

		defaultTexturePack = new TexturePack("Default", getClass().getResource((game.getTextureLocation() + "/")).getPath().replace("%20", " "));
		texturePack = defaultTexturePack;

		FileUtil.worlds = FileUtil.getSavedWorlds();
		FileUtil.texturePacks = FileUtil.getTexturePacks();




		client = new Client(saveUtil.getDataHandler("config/settings.cfg").getStringDefault("playerName", "Player"));
		server = new Server();

		LoggerUtil.out.log(Level.INFO, "Client and Server instance initiated.");
		
		Registrations.registerGenerations();
		CraftingRegister.registerRecipes();

		imageLoader.reloadTextures();

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

			public void mouseClicked( int button, int x, int y, int clickCount ) {
			}

			public void mousePressed( int button, int x, int y ) {
			}

			public void mouseReleased( int button, int x, int y ) {
			}

			public void mouseMoved( int oldx, int oldy, int newx, int newy ) {
			}

			public void mouseDragged( int oldx, int oldy, int newx, int newy ) {
			}

			public void setInput( Input input ) {
			}

			public boolean isAcceptingInput() {
				return true;
			}

			public void inputEnded() {
			}

			public void inputStarted() {
			}
		});


	}
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

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("drop")) {
			@Override
			public void performAction() {
				if(MainFile.game.getServer().getWorld() != null && getCurrentMenu() == null)
				getClient().getPlayer().dropItem();
			}
		});

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("map")) {
			@Override
			public void performAction() {
				if(MainFile.game.getServer().getWorld() != null && getCurrentMenu() == null)
				setCurrentMenu(null);
			}
		});

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("exit")) {
			@Override
			public void performAction() {
				if(MainFile.game.getServer().getWorld() != null && getCurrentMenu() == null)
				setCurrentMenu(new GuiIngameMenu(gameContainer, ConfigValues.PAUSE_GAME_IN_GUI));
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


		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("worldMap")) {
			@Override
			public void performAction() {
				if(!ConfigValues.debug) return;

				float scale = getServer().getWorld().worldSize.xSize / 128;

				try {
					Image image = new Image((getServer().getWorld().worldSize.xSize * ConfigValues.size) / (int)scale,(getServer().getWorld().worldSize.ySize * ConfigValues.size) / (int)scale);
					Graphics g2 = image.getGraphics();

					g2.setBackground(getServer().getWorld().worldTimeOfDay.SkyColor);
					g2.clear();

					g2.pushTransform();
					g2.scale(1 / scale, 1 / scale);

					for(int i = (ConfigValues.renderMod == EnumRenderMode.render2D || ConfigValues.simpleBlockRender ? 2 : 0); i < 3; i++) {
						HashMap<Point, Block> b = new HashMap<>();

						for (int x = 0; x < (getServer().getWorld().worldSize.xSize); x++) {
							for (int y = 0; y < (getServer().getWorld().worldSize.ySize); y++) {
//								getServer().getWorld().loadChunk(x / Chunk.chunkSize, y / Chunk.chunkSize);

								if (MainFile.game.getServer().getWorld().getBlock(x, y) != null) {
									Block block = MainFile.game.getServer().getWorld().getBlock(x, y);
									if (block != null)
										if (block.isBlockSolid()) {
											((DefaultBlockRendering) block.getRender()).renderBlock(g2, (int) ((x) * ConfigValues.size), (int) ((y) * ConfigValues.size), block.getRenderMode(), new ItemStack(block), MainFile.game.getServer().getWorld(), x, y, i, false);
										} else {
											b.put(new Point(x, y), block);
										}
								}
							}
						}

						//Non-solid block rendering is delayed to prevent overlay issues
						for (Map.Entry<Point, Block> bb : b.entrySet()) {
							float blockX = (float) bb.getKey().x * ConfigValues.size;
							float blockY = (float) bb.getKey().y * ConfigValues.size;

							((DefaultBlockRendering) bb.getValue().getRender()).renderBlock(g2, (int) ((blockX)), (int) ((blockY)), bb.getValue().getRenderMode(), new ItemStack(bb.getValue()), MainFile.game.getServer().getWorld(), bb.getKey().x, bb.getKey().y, i, false);
						}
					}


					if(ConfigValues.renderChunks){
						for(int x = 0; x < (getServer().getWorld().worldSize.xSize / Chunk.chunkSize); x++){
							for(int y = 0; y < (getServer().getWorld().worldSize.ySize / Chunk.chunkSize); y++){
								g2.setColor(Color.black);
								g2.draw(new Rectangle((x * Chunk.chunkSize) * ConfigValues.size, (y * Chunk.chunkSize) * ConfigValues.size, Chunk.chunkSize * ConfigValues.size, Chunk.chunkSize * ConfigValues.size));
							}
						}
					}


					g2.fill(new Rectangle(getClient().getPlayer().getEntityPostion().x - (ConfigValues.size / 2), getClient().getPlayer().getEntityPostion().y - (ConfigValues.size / 2), (ConfigValues.size), (ConfigValues.size)));

					g2.popTransform();

					g2.flush();

					FileUtils.getFolder(getFilesSaveLocation() + "/maps/");
					ImageOut.write(image, getFilesSaveLocation() + "/maps/worldMap_" + getServer().getWorld().worldName + ".png");

				}catch (Exception e){
					LoggerUtil.exception(e);
				}
			}
		});

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("chunkMap")) {
			@Override
			public void performAction() {
				if(!ConfigValues.debug) return;

				try {
					Image image = new Image(getServer().getWorld().worldSize.xSize, getServer().getWorld().worldSize.ySize);
					Graphics g2 = image.getGraphics();

					g2.setBackground(new Color(0,0,0,0));
					g2.clear();

					for(int x = 0; x < (getServer().getWorld().worldSize.xSize / Chunk.chunkSize); x++){
						for(int y = 0; y < (getServer().getWorld().worldSize.ySize / Chunk.chunkSize); y++){

							if(!getServer().getWorld().isChunkLoaded(x, y)){
								g2.setColor(Color.red);
							}

							if(getServer().getWorld().isChunkLoaded(x, y)){
								if(getServer().getWorld().getChunk(x * Chunk.chunkSize, y * Chunk.chunkSize) != null && getServer().getWorld().getChunk(x * Chunk.chunkSize, y * Chunk.chunkSize).shouldBeLoaded()){
									g2.setColor(Color.green);
								}else{
									g2.setColor(Color.yellow);
								}
							}

							g2.fill(new Rectangle(x * Chunk.chunkSize, y * Chunk.chunkSize, Chunk.chunkSize, Chunk.chunkSize));


							g2.setColor(Color.black);
							g2.draw(new Rectangle(x * Chunk.chunkSize, y * Chunk.chunkSize, Chunk.chunkSize, Chunk.chunkSize));
						}
					}


					g2.fill(new Rectangle(getClient().getPlayer().getEntityPostion().x - 1, getClient().getPlayer().getEntityPostion().y - 1, 2, 2));

					g2.flush();

					FileUtils.getFolder(getFilesSaveLocation() + "/maps/");
					ImageOut.write(image, getFilesSaveLocation() + "/maps/chunkMap_" + getServer().getWorld().worldName + ".png");


				}catch (Exception e){
					LoggerUtil.exception(e);
				}
			}
		});
	}


	@Override
	public void updateGame( GameContainer container, int delta ) throws SlickException {
		if (!(getCurrentMenu() instanceof Gui)) {
			updateKeys(container, delta);
			BlockAction.update(container, delta);

			if(BlockAction.timeSince >= (BlockAction.blockBreakReach / 2)){
				BlockAction.blockBreakDelay = 0;
				BlockAction.blockDamage = 0;
				BlockAction.prevX = -1;
				BlockAction.prevY = -1;
			}else{
				BlockAction.timeSince += 1;
			}
		}

		//TODO Find a better way to call this on the main thread
		if(getServer().getWorld() != null && getClient().getPlayer() != null && !getServer().getWorld().generating && !getClient().hasSpawnedPlayer){
			getServer().getWorld().doneGenerating();
		}

		if (ConfigValues.debug && !hasDebugSize) {
			gameContainer.setDisplayMode(xWindowSize + debugSize, yWindowSize, false);
			hasDebugSize = true;

		} else if (!ConfigValues.debug && hasDebugSize) {
			gameContainer.setDisplayMode(xWindowSize, yWindowSize, false);
			hasDebugSize = false;
		}

		if (container.isPaused() && !(getCurrentMenu() instanceof Gui)) {
			container.setPaused(false);
		}
	}


	@Override
	public void render( GameContainer container, Graphics g2 ) throws SlickException {
		Rectangle c = g2.getClip();

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

		g2.setColor(tempC);
		g2.setClip(c);
	}

	@Override
	public void renderGame( GameContainer container, Graphics g2 ) throws SlickException {
	}

	@Override
	public void loadGame() {
	}

	@Override
	public void closeGame() {
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
		return "../textures";
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
					new BackgroundRender(), new BlockRendering(), new EntityRendering(),
					new BlockSelectionRender(), new HotbarRender(), new MinimapRender(),
					new DebugInfoRender(), new WorldGenerationScreen()};
		}

		return renders;
	}

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
				if (getClient().getPlayer().isOnGround) {
					if (getClient().getPlayer().moveTo(getClient().getPlayer().getEntityPostion().x, getClient().getPlayer().getEntityPostion().y - 1F)) {
						getClient().getPlayer().isOnGround = false;
					}
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



