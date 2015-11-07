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
import Render.Renders.DebugInfoRender;
import Render.Renders.HotbarRender;
import Settings.Config;
import Utils.ConfigValues;
import Utils.FPScounter;
import Utils.Registrations;
import WorldFiles.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MainFile implements KeyListener {


	//TODO Create an util to make it easier to add random noise to blocks
	//TODO Add main menu and world saving/creation
	//TODO Fix y-max being at the bottom line of the world
	//TODO Start a gui system to allow main menu and player inventory

	//TODO Add float position for entities and block render to allow adding motion based movement for smoother player control and to allow jump/fall.
	//TODO Add ingame version of settings screen
	//TODO Change AbstractWindowRender system to allow window renders in guis (ingame guis like inventories and ingame options)


	public static MainFile file = new MainFile();
	public static Random random = new Random();

	public static World currentWorld;
	public static Gui currentGui;

	public static CustomFrame frame;
	public static Rectangle blockRenderBounds = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size) - ConfigValues.renderXSize, (ConfigValues.renderYSize * ConfigValues.size));
	public FPScounter fpScounter = new FPScounter();
	boolean hasDebugSize = false;
	int debugSize = 300;

	public static void main( String[] args ) {
		file.run();
	}

	public static JFrame getFrame() {
		return frame;
	}

	public void run() {
		Registrations.registerGenerations();
		Registrations.registerWindowRenders();

		Config.addOptionsToList();

		frame = new CustomFrame();
		frame.setTitle(ConfigValues.gameTitle);

		frame.addKeyListener(this);

		frame.setPreferredSize(new Dimension((BlockRendering.START_X_POS * 2) + (ConfigValues.renderXSize * ConfigValues.size), (BlockRendering.START_Y_POS * 2) + (ConfigValues.renderYSize * ConfigValues.size) + ConfigValues.hotbarRenderSize + 25));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed( MouseEvent e ) {
				if (e.getButton() == MouseEvent.BUTTON1) {

					for (AbstractWindowRender render : Registrations.windowRenders) {
						if (currentGui == null || render.canRenderWithGui()) render.mouseClick(e, frame);
					}

					if (currentGui != null) {
						currentGui.mouseClick(e, frame);
					}
				}
			}

		});

		currentGui = new GuiMainMenu();

		frame.pack();
		frame.setVisible(true);

		while (true) {

			//TODO Fix resizable
			if (ConfigValues.resizeable) {
				ConfigValues.renderXSize = Math.round((frame.getWidth() - (BlockRendering.START_X_POS * 2)) / ConfigValues.size);
				ConfigValues.renderYSize = Math.round((frame.getHeight() - (BlockRendering.START_Y_POS * 2)) / ConfigValues.size);

//			ConfigValues.renderRange = ((ConfigValues.renderXSize + ConfigValues.renderYSize) / 2) / 2;
//
//			float temp = ((ConfigValues.renderXSize + ConfigValues.renderYSize) / 2) / 25;
//			ConfigValues.renderDistance = 20;
			} else {
				ConfigValues.renderXSize = 25;
				ConfigValues.renderYSize = 25;

				ConfigValues.renderRange = ((ConfigValues.renderXSize + ConfigValues.renderYSize) / 2) / 2;
			}

			if (ConfigValues.resizeable && !frame.isResizable() || !ConfigValues.resizeable && frame.isResizable())
				frame.setResizable(ConfigValues.resizeable);

			if (frame != null && frame.getContentPane() != null) frame.getContentPane().repaint();


			if (ConfigValues.debug && !hasDebugSize) {
				frame.setPreferredSize(new Dimension(frame.getPreferredSize().width + debugSize, frame.getPreferredSize().height));
				frame.pack();
				hasDebugSize = true;

			} else if (!ConfigValues.debug && hasDebugSize) {
				frame.setPreferredSize(new Dimension(frame.getPreferredSize().width - debugSize, frame.getPreferredSize().height));
				frame.pack();
				hasDebugSize = false;

				BlockSelectionRender.selectedX = -1;
				BlockSelectionRender.selectedY = -1;
			}
		}

	}

	@Override
	public void keyTyped( KeyEvent e ) {
		for (AbstractWindowRender render : Registrations.windowRenders) {
			if (currentGui == null || render.canRenderWithGui()) render.keyTyped(e, frame);
		}

		if (currentGui != null) {
			currentGui.keyTyped(e, frame);
		}
	}

	//TODO Add proper move controls with motion instead of block moving
	@Override
	public void keyPressed( KeyEvent e ) {

		if (MainFile.currentWorld != null) {
			if (e.getKeyChar() == 'a') {
				if (currentWorld.player.facing == 1) {
					currentWorld.player.moveTo(currentWorld.player.getEntityPostion().x - 0.4F, currentWorld.player.getEntityPostion().y);
				}

				currentWorld.player.facing = 1;

			} else if (e.getKeyChar() == 'd') {
				if (currentWorld.player.facing == 2) {
					currentWorld.player.moveTo(currentWorld.player.getEntityPostion().x + 0.4F, currentWorld.player.getEntityPostion().y);
				}

				currentWorld.player.facing = 2;
			}


			if (e.getKeyChar() == 'w') {
				if (currentWorld.player.jump && currentWorld.getBlock((int) currentWorld.player.getEntityPostion().x, (int) currentWorld.player.getEntityPostion().y + 1) != null) {
					if (currentWorld.player.moveTo(currentWorld.player.getEntityPostion().x, currentWorld.player.getEntityPostion().y - 1.5F)) {
						currentWorld.player.jump = false;
					}
				}
			}
		}

		//TODO Make it open a ingame version of the main menu
		if (currentGui == null && currentWorld != null) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				currentGui = new GuiMainMenu();
			}
		}

		for (AbstractWindowRender render : Registrations.windowRenders) {
			if (currentGui == null || render.canRenderWithGui()) render.keyPressed(e, frame);
		}

		if (currentGui != null) {
			currentGui.keyPressed(e, frame);
		}
	}

	@Override
	public void keyReleased( KeyEvent e ) {
		for (AbstractWindowRender render : Registrations.windowRenders) {
			if (currentGui == null || render.canRenderWithGui()) render.keyReleased(e, frame);
		}

		if (currentGui != null) {
			currentGui.keyReleased(e, frame);
		}
	}

	class CustomFrame extends JFrame {

		public CustomFrame() {
			CustomJpanel panel = new CustomJpanel();

			setContentPane(panel);
			addMouseWheelListener(panel);
		}

		class CustomJpanel extends JPanel implements MouseWheelListener {
			boolean hasScrolled = false;

			@Override
			public void paintComponent( Graphics g ) {
				super.paintComponent(g);

				fpScounter.tick();

				Graphics2D g2 = (Graphics2D) g;

				Shape c = g2.getClip();


				g2.setClip(blockRenderBounds);

				if (currentGui != null) {
					if (currentGui.canRender(frame)) {
						currentGui.render(frame, g2);
						currentGui.renderObject(frame, g2);
					}
				}

				for (AbstractWindowRender render : Registrations.windowRenders) {
					if (currentGui == null || render.canRenderWithGui()) if (render.canRender(frame)) {
						render.render(frame, g2);
					}
				}


				g2.setClip(blockRenderBounds);

				Rectangle e = new Rectangle(blockRenderBounds.x, blockRenderBounds.y, blockRenderBounds.width - 1, blockRenderBounds.height - 1);
				g2.draw(e);

				g2.setClip(c);

				DebugInfoRender.fps = fpScounter.getFPS();

			}

			@Override
			public void mouseWheelMoved( MouseWheelEvent e ) {
				if (!hasScrolled) {
					HotbarRender.slotSelected += (e.getWheelRotation());

					if (HotbarRender.slotSelected > 10) HotbarRender.slotSelected = 1;

					if (HotbarRender.slotSelected <= 0) HotbarRender.slotSelected = 10;

					hasScrolled = true;
				} else {
					hasScrolled = false;
				}
			}
		}
	}

}



