package Guis.Interfaces;

import GameFiles.BaseGame;
import Guis.Objects.GuiButton;
import Guis.Objects.MainMenuButton;
import Interface.GuiObject;
import Interface.UIMenu;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.FileUtil;
import Utils.FontHandler;
import Utils.TimeTaker;
import WorldFiles.EnumWorldSize;
import WorldFiles.World;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;


public class LoadWorldMenu extends AbstractMainMenu {
	public LoadWorldMenu guiInst = this;

	public World selected = null;


	//TODO Add delete and rename
	//TODO Add scrollbar...


	public LoadWorldMenu() {
		super();
		FileUtil.worlds = FileUtil.getSavedWorlds();

		org.newdawn.slick.geom.Rectangle rectangle = new org.newdawn.slick.geom.Rectangle(MainFile.xWindowSize * 0.15F, MainFile.yWindowSize * 0.15F, MainFile.xWindowSize * 0.7F, MainFile.xWindowSize * 0.7F);
		Rectangle list = new Rectangle(rectangle.getX() + (rectangle.getWidth() * 0.02F), rectangle.getY() + (rectangle.getHeight() * 0.02F), rectangle.getWidth() * 0.5F, rectangle.getHeight() * 0.90F);

		if(FileUtil.worlds != null && FileUtil.worlds.size() > 0) {
			int i = 0;
			for (World world : FileUtil.worlds) {
				guiObjects.add(new WorldButton((int)(list.getX() + 4), (int)(list.getY() + 4 + (i * 36)), (int)(list.getWidth() - 7), 32, world));

				i += 1;
			}
		}


		guiObjects.add(new backButton((int)(MainFile.yWindowSize * 0.9F)));
		guiObjects.add(new startButton((int)list.getMaxX() + 48, (int)list.getMaxY() - 20, (int)list.getWidth() - 76, 32));

	}

	@Override
	public void render( Graphics g2 ) {
		super.render(g2);

		g2.setColor(Color.white);
		FontHandler.resizeFont(g2, 24);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Load world", renderStart + 30, 70);
		FontHandler.resetFont(g2);

		org.newdawn.slick.geom.Rectangle rectangle = new org.newdawn.slick.geom.Rectangle(MainFile.xWindowSize * 0.15F, MainFile.yWindowSize * 0.15F, MainFile.xWindowSize * 0.7F, MainFile.xWindowSize * 0.7F);
		Rectangle list = new Rectangle(rectangle.getX() + (rectangle.getWidth() * 0.02F), rectangle.getY() + (rectangle.getHeight() * 0.02F), rectangle.getWidth() * 0.5F, rectangle.getHeight() * 0.96F);

		g2.setColor(Color.lightGray);
		g2.fill(rectangle);

		g2.setColor(Color.darkGray.darker());
		g2.fill(list);

		g2.setColor(Color.darkGray);
		g2.draw(rectangle);
		g2.draw(list);

		Rectangle slider = new Rectangle(list.getMaxX() + 5, list.getY(), 31, list.getHeight());

		g2.setColor(Color.darkGray);
		g2.fill(slider);

		Rectangle rect = new Rectangle(list.getMaxX() + 40, list.getY(), list.getWidth() * 0.78F, list.getHeight());

		g2.setColor(Color.gray);
		g2.fill(rect);

		g2.setColor(Color.darkGray);
		g2.draw(rect);

		g2.setClip(rect);

		g2.setColor(Color.black);
		FontHandler.resizeFont(g2, 17);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("World: " + (selected != null ? selected.worldName : ""), rect.getX() + 5, rect.getY() + 5);

		if(selected != null) {
//			g2.drawString("Time played: " + selected.getTimePlayed(), rect.getX() + 5, rect.getY() + 30);

			//TODO Display something that does not required the world to be loaded
//			if(selected.worldSize != null) {
//				g2.drawString("World size: " + selected.worldSize.name(), rect.getX() + 5, rect.getY() + 45);
//				g2.drawString(" - " + selected.worldSize.xSize + "x" + selected.worldSize.ySize, rect.getX() + 5, rect.getY() + 60);
//			}
		}
		FontHandler.resetFont(g2);

		g2.setClip(null);

	}

	@Override
	public boolean canRender() {
		return true;
	}


	class backButton extends MainMenuButton {

		public backButton( int y ) {
			super(MainFile.game,renderStart, y, 190, 32, "Back", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new MainMenu());
		}
	}

	class WorldButton extends MainMenuButton {

		public World world;

		public WorldButton(int x, int y, int width, int height, World world ) {
			super(MainFile.game,x, y, width, height, world.worldName, guiInst);

			this.world = world;
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			if(selected != world) {
				selected = world;
			}else{
				selected = null;
			}
		}

		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {
			Color temp = g2.getColor();

			boolean hover = isMouseOver();

			if (hover) {
				g2.setColor(new Color(95, 95, 95, 174));
			} else {
				g2.setColor(new Color(95, 95, 95, 86));
			}

			if (!enabled) {
				g2.setColor(new Color(41, 41, 41, 174));
			}

			if(selected == world){
				g2.setColor(Color.gray);
			}


			g2.fill(new Rectangle(x, y, width, height));
			g2.setColor(hover ? Color.white : Color.lightGray);

			if (!enabled) g2.setColor(Color.gray);

			FontHandler.resizeFont(g2, 22);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), text, x, y, width);

			FontHandler.resetFont(g2);

			if(selected == world){
				g2.setColor(Color.lightGray);
				g2.draw(new Rectangle(x + 1, y, width, height));

			}

			g2.setColor(temp);
		}
	}

	class startButton extends MainMenuButton {
		public startButton(int x, int y, int width, int height) {
			super(MainFile.game,x, y, width, height, "Start", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			if(selected != null){
				MainFile.game.getServer().setWorld(selected);
				MainFile.game.getServer().getWorld().loadWorld(MainFile.game.getServer().getWorld().worldName);

				MainFile.game.getServer().getWorld().generate();
				MainFile.game.getServer().getWorld().start();
				MainFile.game.getServer().getWorld().loadPlayer();

				MainFile.game.setCurrentMenu(null);
			}
		}

		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {
			Color temp = g2.getColor();
			enabled = selected != null;

			boolean hover = isMouseOver();

			if (hover) {
				g2.setColor(new Color(95, 95, 95, 174).darker());
			} else {
				g2.setColor(new Color(95, 95, 95, 86).darker());
			}

			if (!enabled) {
				g2.setColor(new Color(41, 41, 41, 174).darker());
			}

			if(selected == world){
				g2.setColor(Color.gray);
			}


			g2.fill(new Rectangle(x, y, width, height));
			g2.setColor(Color.white);

			if (!enabled) g2.setColor(Color.gray);

			FontHandler.resizeFont(g2, 22);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), text, x, y, width, g2.getColor());

			FontHandler.resetFont(g2);

			if(selected == world){
				g2.setColor(Color.lightGray);
				g2.draw(new Rectangle(x + 1, y, width, height));

			}

			g2.setColor(temp);
		}


	}
}
