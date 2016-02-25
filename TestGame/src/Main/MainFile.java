package Main;

import Crafting.CraftingRegister;
import GameFiles.BaseGame;
import Guis.GuiCrafting;
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
import Utils.*;
import WorldFiles.World;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import javax.swing.filechooser.FileSystemView;
import java.awt.Font;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFile extends BaseGame {

	//TODO Add world saving
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
	
	private Client client;
	private Server server;

	public MainFile( String title, int xSize, int ySize, boolean fullscreen ) {
		super(title, xSize, ySize, fullscreen);
	}


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
	public void initGame( GameContainer container ) throws SlickException {
		//TODO Should be easy to add ingame world loading now!
//		FileUtil.worlds = FileUtil.getSavedWorlds();

		client = new Client(saveUtil.getDataHandler("config/settings.cfg").getStringDefault("playerName", "Player"));
		server = new Server();
		
		Registrations.registerGenerations();
		CraftingRegister.registerRecipes();

		setCurrentMenu(new MainMenu());

		if(FileUtil.worlds != null && FileUtil.worlds.size() > 0) {
			LoggerUtil.out.log(Level.INFO, "Found saved worlds: ");

			for (World w : FileUtil.worlds) {
				LoggerUtil.out.log(Level.INFO, w.toString());
			}
		}

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
		return FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/" + Title + "/";
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



