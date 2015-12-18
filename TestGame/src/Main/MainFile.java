package Main;/*
* Project: Random Java Creations
* Package: PACKAGE_NAME
* Created: 18.07.2015
*/

import Interface.Gui;
import Interface.Interfaces.GuiMainMenu;
import Render.AbstractWindowRender;
import Render.Renders.BlockRendering;
import Render.Renders.BlockSelectionRender;
import Render.Renders.HotbarRender;
import Settings.Config;
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

	//TODO Recreate game with Slick2D as the base to allow easier physics
	//TODO Add physics to both blocks and player

	//TODO Create an util to make it easier to add random noise to blocks
	//TODO Add main menu and world saving/creation
	//TODO Fix y-max being at the bottom line of the world
	//TODO Start a gui system to allow main menu and player inventory

	//TODO Add float position for entities and block render to allow adding motion based movement for smoother player control and to allow jump/fall.
	//TODO Add ingame version of settings screen
	//TODO Change AbstractWindowRender system to allow window renders in guis (ingame guis like inventories and ingame options)


	public static MainFile file = new MainFile("Test Game");
	public static Random random = new Random();

	public static World currentWorld;
	public static Rectangle blockRenderBounds = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size) - ConfigValues.renderXSize, (ConfigValues.renderYSize * ConfigValues.size));
	public static boolean hasScrolled = false;
	public static AppGameContainer gameContainer;
	public static int xWindowSize = (ConfigValues.renderXSize * ConfigValues.size) - 25;
	public static int yWindowSize = (ConfigValues.renderYSize * ConfigValues.size);
	public static Gui currentGui;
	boolean hasDebugSize = false;
	int debugSize = 265;

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

		currentGui = new GuiMainMenu();

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
						if (currentGui == null || render.canRenderWithGui()) render.mouseClick(button, x, y);
					}

					if (currentGui != null) {
						currentGui.mouseClick(button, x, y);
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

//		//TODO Fix resizable
//		if (ConfigValues.resizeable) {
//			ConfigValues.renderXSize = Math.round((container.getWidth() - (BlockRendering.START_X_POS * 2)) / ConfigValues.size);
//			ConfigValues.renderYSize = Math.round((container.getHeight() - (BlockRendering.START_Y_POS * 2)) / ConfigValues.size);
//
//		} else {
//			ConfigValues.renderXSize = 25;
//			ConfigValues.renderYSize = 25;
//
//			ConfigValues.renderRange = ((ConfigValues.renderXSize + ConfigValues.renderYSize) / 2) / 2;
//		}

		if (ConfigValues.debug && !hasDebugSize) {
			gameContainer.setDisplayMode(container.getWidth() + debugSize, container.getHeight(), false);
			hasDebugSize = true;

		} else if (!ConfigValues.debug && hasDebugSize) {
			gameContainer.setDisplayMode(container.getWidth() - debugSize, container.getHeight(), false);
			hasDebugSize = false;

			BlockSelectionRender.selectedX = -1;
			BlockSelectionRender.selectedY = -1;
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

		if (currentGui != null) {
			if (currentGui.canRender()) {
				currentGui.render(g2);
				currentGui.renderObject(g2);
			}
		}

		for (AbstractWindowRender render : Registrations.windowRenders) {
			Color tempC = g2.getColor();

			if (currentGui == null || render.canRenderWithGui()) if (render.canRender()) {
				render.render(g2);
			}
			g2.setColor(tempC);
		}


		g2.setClip(blockRenderBounds);
		g2.setClip(c);
	}

	//TODO Add keyregister
	//TODO Add proper move controls with motion instead of block moving
	@Override
	public void keyPressed( int key, char c ) {
		if (Character.isDigit(c)) {
			Integer tt = Integer.parseInt(c + "");
			if (tt > 0 && tt < 10) {
				HotbarRender.slotSelected = tt;
			}
		}

		//TODO Make it open a ingame version of the main menu
		if (currentGui == null && currentWorld != null) {
			if (key == Input.KEY_ESCAPE) {
				currentGui = new GuiMainMenu();
			}
		}

		for (AbstractWindowRender render : Registrations.windowRenders) {
			if (currentGui == null || render.canRenderWithGui()) render.keyPressed(key, c);
		}

		if (currentGui != null) {
			currentGui.keyPressed(key, c);
		}
	}

	@Override
	public void keyReleased( int key, char c ) {
		for (AbstractWindowRender render : Registrations.windowRenders) {
			if (currentGui == null || render.canRenderWithGui()) render.keyReleased(key, c);
		}

		if (currentGui != null) {
			currentGui.keyReleased(key, c);
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



