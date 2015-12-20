package Main;

import Interface.Interfaces.GuiMainMenu;
import Interface.Menu;
import Render.AbstractWindowRender;
import Render.Renders.BlockRendering;
import Render.Renders.HotbarRender;
import Settings.Config;
import Utils.BlockAction;
import Utils.ConfigValues;
import Utils.Registrations;
import Utils.RenderUtil;
import WorldFiles.World;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.Font;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainFile extends BasicGame implements InputListener {

	//TODO Add main menu and world saving/creation
	//TODO Start a menu system to allow main menu and player inventory

	//TODO Add ingame version of settings screen

	public static MainFile file = new MainFile("Test Game");
	public static Random random = new Random();

	public static World currentWorld;
	public static Menu currentMenu;

	public static Rectangle blockRenderBounds = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size) - ConfigValues.renderXSize, (ConfigValues.renderYSize * ConfigValues.size));

	public static AppGameContainer gameContainer;

	public static int xWindowSize = (ConfigValues.renderXSize * ConfigValues.size) - 25;
	public static int yWindowSize = (ConfigValues.renderYSize * ConfigValues.size);

	public static boolean hasDebugSize = false;
	public static boolean hasScrolled = false;
	public static int debugSize = 265;


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
		Registrations.registerGenerations();
		Registrations.registerWindowRenders();

		Config.addOptionsToList();
		container.getInput().addKeyListener(this);

		currentMenu = new GuiMainMenu();

		container.getInput().addMouseListener(new MouseListener() {
			//TODO Registery for mouse wheel (or add it to the keybind system i have to make)
			public void mouseWheelMoved( int change ) {
				if (!hasScrolled) {
					HotbarRender.slotSelected += (change / -120);

					if (HotbarRender.slotSelected > 10) HotbarRender.slotSelected = 1;
					if (HotbarRender.slotSelected <= 0) HotbarRender.slotSelected = 10;

					hasScrolled = true;
				} else {
					hasScrolled = false;
				}
			}

			public void mouseClicked( int button, int x, int y, int clickCount ) {
			}


			public void mousePressed( int button, int x, int y ) {
					for (AbstractWindowRender render : Registrations.windowRenders) {
						if (currentMenu == null || render.canRenderWithWindow()) render.mouseClick(button, x, y);
					}

				if (currentMenu != null) {
					currentMenu.mouseClick(button, x, y);
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


	@Override
	public void update( GameContainer container, int delta ) throws SlickException {
		updateKeys(container, delta);
		BlockAction.update(container);

		if (ConfigValues.debug && !hasDebugSize) {
			gameContainer.setDisplayMode(container.getWidth() + debugSize, container.getHeight(), false);
			hasDebugSize = true;

		} else if (!ConfigValues.debug && hasDebugSize) {
			gameContainer.setDisplayMode(container.getWidth() - debugSize, container.getHeight(), false);
			hasDebugSize = false;
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

		if (currentMenu != null) {
			if (currentMenu.canRender()) {
				currentMenu.render(g2);
				currentMenu.renderObject(g2);
			}
		}

		for (AbstractWindowRender render : Registrations.windowRenders) {
			Color tempC = g2.getColor();

			if (currentMenu == null || render.canRenderWithWindow()) if (render.canRender()) {
				render.render(g2);
			}
			g2.setColor(tempC);
		}


		g2.setClip(c);
	}

	//TODO Add keyregister
	@Override
	public void keyPressed( int key, char c ) {
		if (Character.isDigit(c)) {
			Integer tt = Integer.parseInt(c + "");
			if (tt > 0 && tt < 10) {
				HotbarRender.slotSelected = tt;
			}
		}

		//TODO Make it open a ingame version of the main menu
		if (currentMenu == null && currentWorld != null) {
			if (key == Input.KEY_ESCAPE) {
				currentMenu = new GuiMainMenu();
			}
		}

		for (AbstractWindowRender render : Registrations.windowRenders) {
			if (currentMenu == null || render.canRenderWithWindow()) render.keyPressed(key, c);
		}

		if (currentMenu != null) {
			currentMenu.keyPressed(key, c);
		}
	}

	@Override
	public void keyReleased( int key, char c ) {
		for (AbstractWindowRender render : Registrations.windowRenders) {
			if (currentMenu == null || render.canRenderWithWindow()) render.keyReleased(key, c);
		}

		if (currentMenu != null) {
			currentMenu.keyReleased(key, c);
		}
	}

	public void updateKeys( GameContainer gameContainer, int delta ) {
		if (MainFile.currentWorld != null) {
			if (gameContainer.getInput().isKeyPressed(Input.KEY_A) || gameContainer.getInput().isKeyDown(Input.KEY_A)) {
				if (currentWorld.player.facing == 1) {
					currentWorld.player.moveTo(currentWorld.player.getEntityPostion().x - 0.14F, currentWorld.player.getEntityPostion().y);
				}

				currentWorld.player.facing = 1;

			} else if (gameContainer.getInput().isKeyPressed(Input.KEY_D) || gameContainer.getInput().isKeyDown(Input.KEY_D)) {
				if (currentWorld.player.facing == 2) {
					currentWorld.player.moveTo(currentWorld.player.getEntityPostion().x + 0.14F, currentWorld.player.getEntityPostion().y);
				}

				currentWorld.player.facing = 2;
			}


			if (gameContainer.getInput().isKeyPressed(Input.KEY_W) || gameContainer.getInput().isKeyDown(Input.KEY_W) && (delta & 700) != 0) {
				if (currentWorld.player.isOnGround) {
					if (currentWorld.player.moveTo(currentWorld.player.getEntityPostion().x, currentWorld.player.getEntityPostion().y - 1F)) {
						currentWorld.player.isOnGround = false;
					}
				}
			}
		}
	}
}



