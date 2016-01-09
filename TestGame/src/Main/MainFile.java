package Main;

import Crafting.CraftingRegister;
import Guis.Gui;
import Guis.GuiCrafting;
import Guis.GuiIngameMenu;
import Guis.GuiInventory;
import Interface.Interfaces.MainMenu;
import Render.AbstractWindowRender;
import Render.Renders.BlockRendering;
import Render.Renders.HotbarRender;
import Settings.Config;
import Settings.Values.KeybindingAction;
import Sided.Client;
import Sided.Server;
import Utils.BlockAction;
import Utils.ConfigValues;
import Utils.Registrations;
import Utils.RenderUtil;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainFile extends BasicGame implements InputListener {

	//TODO Add world saving
	//TODO Add big map (fullscreen/gui) (similar to Terraria)

	//TODO Start optimizing the game. Remove all unessecary creation of new veriables. (For example creating a new rectangle each rendering call)

	public static AppGameContainer gameContainer;
	public static MainFile file = new MainFile("Test Game");
	public static Random random = new Random();
	
	public static Rectangle blockRenderBounds = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size) - ConfigValues.renderXSize, (ConfigValues.renderYSize * ConfigValues.size));
	public static int xWindowSize = (ConfigValues.renderXSize * ConfigValues.size) - 25;
	public static int yWindowSize = (ConfigValues.renderYSize * ConfigValues.size);

	public static boolean hasDebugSize = false;
	public static boolean hasScrolled = false;
	public static int debugSize = 265;
	
	private static Client client;
	private static Server server;


	public MainFile( String title ) {
		super(title);
	}

	public static void main( String[] args ) {
		try {
			//Try make it use ScaleableGame?
			gameContainer = new AppGameContainer(file);

			//TODO fix rendering when using ScaleableGame
			gameContainer.setDisplayMode(xWindowSize, yWindowSize, false);

			gameContainer.setShowFPS(false);
			gameContainer.setVSync(true);
			gameContainer.setUpdateOnlyWhenVisible(false);


			gameContainer.start();
		} catch (SlickException ex) {
			Logger.getLogger(MainFile.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


	@Override
	public void init( GameContainer container ) throws SlickException {
		client = new Client("Player1");
		server = new Server();
		
		Registrations.registerGenerations();
		Registrations.registerWindowRenders();
		CraftingRegister.registerRecipes();

		container.getInput().addKeyListener(this);
		addKEybindings();

		getClient().setCurrentMenu(new MainMenu());

		container.getInput().addMouseListener(new MouseListener() {
			public void mouseWheelMoved( int change ) {
				if (getClient().getCurrentMenu() == null) {
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

				if (getClient().getCurrentMenu() != null) {
					getClient().getCurrentMenu().onMouseWheelMoved(change);
				}

			}

			public void mouseClicked( int button, int x, int y, int clickCount ) {
			}


			public void mousePressed( int button, int x, int y ) {
					for (AbstractWindowRender render : Registrations.windowRenders) {
						if (getClient().getCurrentMenu() == null || render.canRenderWithWindow()) render.mouseClick(button, x, y);
					}

				if (getClient().getCurrentMenu() != null) {
					getClient().getCurrentMenu().mouseClick(button, x, y);
				}
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

	public static ArrayList<KeybindingAction> keybindingActions = new ArrayList<>();
	public static void addKEybindings(){
		keybindingActions.add(new KeybindingAction(Config.getKeybindFromID("inventory")) {
			@Override
			public void performAction() {
				getClient().setCurrentMenu(new GuiInventory());
			}
		});

		keybindingActions.add(new KeybindingAction(Config.getKeybindFromID("crafting")) {
			@Override
			public void performAction() {
				getClient().setCurrentMenu(new GuiCrafting());
			}
		});

		keybindingActions.add(new KeybindingAction(Config.getKeybindFromID("drop")) {
			@Override
			public void performAction() {
				getClient().getPlayer().dropItem();
			}
		});

		keybindingActions.add(new KeybindingAction(Config.getKeybindFromID("map")) {
			@Override
			public void performAction() {
				getClient().setCurrentMenu(null);
			}
		});

		keybindingActions.add(new KeybindingAction(Config.getKeybindFromID("exit")) {
			@Override
			public void performAction() {
				getClient().setCurrentMenu(new GuiIngameMenu());
			}
		});
	}


	@Override
	public void update( GameContainer container, int delta ) throws SlickException {
		if (!(getClient().getCurrentMenu() instanceof Gui)) {
			updateKeys(container, delta);
			BlockAction.update(container);
		}

		//TODO Find a better way to call this on the main thread
		if(getServer().getWorld() != null && getClient().getPlayer() != null && !getServer().getWorld().generating && !getClient().hasSpawnedPlayer){
			getServer().getWorld().doneGenerating();
		}

		if (ConfigValues.debug && !hasDebugSize) {
			gameContainer.setDisplayMode(container.getWidth() + debugSize, container.getHeight(), false);
			hasDebugSize = true;

		} else if (!ConfigValues.debug && hasDebugSize) {
			gameContainer.setDisplayMode(container.getWidth() - debugSize, container.getHeight(), false);
			hasDebugSize = false;
		}

		if (container.isPaused() && !(getClient().getCurrentMenu() instanceof Gui)) {
			container.setPaused(false);
		}
	}

	@Override
	public void render( GameContainer container, Graphics g2 ) throws SlickException {
		Rectangle c = g2.getClip();

		if (g2.getFont() instanceof AngelCodeFont) {
			g2.setFont(RenderUtil.getFont(new Font("Arial", 0, 0)));
		}

		g2.setBackground(Color.lightGray);
		g2.setClip(blockRenderBounds);

		for (AbstractWindowRender render : Registrations.windowRenders) {
			Color tempC = g2.getColor();

			if ((getClient().getCurrentMenu()instanceof Gui) || getClient().getCurrentMenu()== null || render.canRenderWithWindow()) {
				if (render.canRender()) {
					render.render(g2);
				}
			}

			g2.setColor(tempC);
		}

		g2.setBackground(Color.lightGray);
		g2.setClip(blockRenderBounds);

		Color tempC = g2.getColor();

		if (getClient().getCurrentMenu()!= null) {
			if (getClient().getCurrentMenu().canRender()) {
				getClient().getCurrentMenu().render(g2);
				g2.setClip(null);

				getClient().getCurrentMenu().renderObject(g2);
				g2.setClip(null);

				if (getClient().getCurrentMenu()instanceof Gui) {
					((Gui) getClient().getCurrentMenu()).renderPost(g2);
				}
				g2.setClip(null);
			}
		}

		g2.setColor(tempC);
		g2.setClip(c);
	}

	@Override
	public void keyPressed( int key, char c ) {
		if(getServer().getWorld() != null && getClient().getPlayer() != null && getClient().getCurrentMenu() == null){
			for(KeybindingAction ac : keybindingActions){
				if(key == ac.key.getKey()){
					ac.performAction();
					return;
				}
			}
		}

		if(getClient().getCurrentMenu()== null && getServer().getWorld() != null) {
			if (Character.isDigit(c)) {
				Integer tt = Integer.parseInt(c + "");
				if (tt >= 0 && tt < 10) {
					if(tt == 0) tt = 10;

					HotbarRender.slotSelected = tt;
				}
			}
		}

		for (AbstractWindowRender render : Registrations.windowRenders) {
			if ((getClient().getCurrentMenu()instanceof Gui) || getClient().getCurrentMenu()== null || render.canRenderWithWindow()) {
				render.keyPressed(key, c);
			}
		}

		if (getClient().getCurrentMenu()!= null) {
			getClient().getCurrentMenu().keyPressed(key, c);
		}
	}

	@Override
	public void keyReleased( int key, char c ) {
		for (AbstractWindowRender render : Registrations.windowRenders) {
			if ((getClient().getCurrentMenu()instanceof Gui) || getClient().getCurrentMenu()== null || render.canRenderWithWindow()) {
				render.keyReleased(key, c);
			}
		}

		if (getClient().getCurrentMenu()!= null) {
			getClient().getCurrentMenu().keyReleased(key, c);
		}
	}

	public void updateKeys( GameContainer gameContainer, int delta ) {
		if (getServer().getWorld() != null) {
			if (gameContainer.getInput().isKeyPressed(Config.getKeybindFromID("left.walk").getKey()) || gameContainer.getInput().isKeyDown(Config.getKeybindFromID("left.walk").getKey())) {
				if (getClient().getPlayer().facing == 1) {
					getClient().getPlayer().moveTo(getClient().getPlayer().getEntityPostion().x - 0.14F, getClient().getPlayer().getEntityPostion().y);
				}

				getClient().getPlayer().facing = 1;

			} else if (gameContainer.getInput().isKeyPressed(Config.getKeybindFromID("right.walk").getKey()) || gameContainer.getInput().isKeyDown(Config.getKeybindFromID("right.walk").getKey())) {
				if (getClient().getPlayer().facing == 2) {
					getClient().getPlayer().moveTo(getClient().getPlayer().getEntityPostion().x + 0.14F, getClient().getPlayer().getEntityPostion().y);
				}

				getClient().getPlayer().facing = 2;
			}


			if (gameContainer.getInput().isKeyPressed(Config.getKeybindFromID("jump.walk").getKey()) || gameContainer.getInput().isKeyDown(Config.getKeybindFromID("jump.walk").getKey()) && (delta & 700) != 0) {
				if (getClient().getPlayer().isOnGround) {
					if (getClient().getPlayer().moveTo(getClient().getPlayer().getEntityPostion().x, getClient().getPlayer().getEntityPostion().y - 1F)) {
						getClient().getPlayer().isOnGround = false;
					}
				}
			}
		}
	}

	public static Client getClient() {
		return client;
	}

	public static Server getServer() {
		return server;
	}
}



