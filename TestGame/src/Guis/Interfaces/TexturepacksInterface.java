package Guis.Interfaces;

import Guis.GuiSettings;
import Guis.GuiTexturepacks;
import Guis.Objects.MainMenuButton;
import Interface.GuiObject;
import Interface.UIMenu;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import Utils.FileUtil;
import Utils.FontHandler;
import Utils.TexutrePackFiles.TexturePack;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class TexturepacksInterface extends AbstractMainMenu {
	public static int renderStart = 290;
	public static int renderWidth = 190;


	float translate = 0;
	public TexturepacksInterface guiInst = this;

	public TexturepacksInterface() {
		super();

		renderBar = false;

		int buttonSize = 40, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 3);
		guiObjects.add(new backButton(buttonPos + (buttonSize * (14))));
		guiObjects.add(new scrollBar(renderStart + (int)(renderWidth * 1.65F), 140, 50, 500, this));

		int i = 0;
		for(TexturePack pack : FileUtil.texturePacks){
			guiObjects.add(new texturePackButton(renderStart - renderWidth + 5, 140 + i * 70, (int)(renderWidth * 2.5F), 60, pack));

			i += 1;
		}
	}


	public void renderObject( Graphics g2 ) {
		if(buttonTimeLimiter != -1 && buttonTimeLimiter < buttonTimeLimit){
			buttonTimeLimiter += 1;
		}

		for (GuiObject object : guiObjects) {
			if(object instanceof texturePackButton){
				g2.setClip(new Rectangle(renderStart - renderWidth + 2, 138, (renderWidth * 2.5F) + 6, 500));
			}

			object.renderObject(g2, this);

			g2.setClip(null);
		}
	}

	public void onMouseWheelMoved( int change ) {
		for (GuiObject ob : guiObjects) {
			if (ob instanceof scrollBar) {
				ob.onMouseWheelMoved(change);
			}
		}
	}

	public void keyPressed( int key, char c ) {
		if (key == MainFile.game.getConfig().getKeybindFromID("exit").getKey()) {
			MainFile.game.setCurrentMenu(new GuiSettings(MainFile.game.gameContainer, ConfigValues.PAUSE_GAME_IN_GUI));
		}
	}


	@Override
	public void render( Graphics g2 ) {
		super.render(g2);

		int i = 0;
		for(GuiObject object : guiObjects){
			if(object instanceof texturePackButton){

				//TODO Fix being able to press buttons not visible
				if(!g2.getClip().contains(object.x, object.y) || !g2.getClip().contains(object.x + object.getWidth(), object.y + object.getHeight())){
					object.enabled = false;
				}else if (g2.getClip().contains(object.x, object.y) && g2.getClip().contains(object.x + object.getWidth(), object.y + object.getHeight())){
					object.enabled = true;
				}

				if(FileUtil.texturePacks.size() > 7) {
					float f1 = (float) translate / (500 - 27);
					float f2 = (FileUtil.texturePacks.size() - 7) * 70;

					((texturePackButton) object).y = (int) ((140 + i * 70) - ((f1 * f2)));
				}

				i += 1;
			}
		}

		g2.setColor(org.newdawn.slick.Color.black);
		g2.drawLine(renderStart - (renderWidth), BlockRendering.START_Y_POS, renderStart - (renderWidth), (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));
		g2.drawLine(renderStart + (renderWidth  * 2), BlockRendering.START_Y_POS, renderStart + (renderWidth  * 2), (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));


		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(renderStart - (renderWidth), BlockRendering.START_Y_POS, renderWidth + (renderWidth * 2), (ConfigValues.renderYSize * ConfigValues.size)));

		int buttonSize = 40, buttonPos = (BlockRendering.START_Y_POS) + (buttonSize * 3);

		g2.setColor(g2.getColor().darker());
		g2.fill(new Rectangle(renderStart - renderWidth, 70, renderWidth * 3, 60));

		g2.setColor(Color.white);
		FontHandler.resizeFont(g2, 22);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Texturepacks", renderStart, 80, renderWidth, g2.getColor());
		FontHandler.resetFont(g2);

		g2.setColor(new Color(95, 95, 95, 112).darker(0.75F));
		g2.fill(new Rectangle(renderStart - renderWidth + 2, 138, (renderWidth * 2.5F) + 6, 500));

	}


	@Override
	public boolean canRender() {
		return true;
	}


	class backButton extends MainMenuButton {

		public backButton( int y ) {
			super(MainFile.game, renderStart, y, 190, 32, "Back", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			MainFile.game.setCurrentMenu(new SettingsMenu());
		}
	}

	class texturePackButton extends MainMenuButton{

		TexturePack pack;

		public texturePackButton( int x, int y, int width, int height, TexturePack pack ) {
			super(MainFile.game, x, y, width, height, pack.name, guiInst);
			this.pack = pack;
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			if(MainFile.game.texturePack != pack && enabled) {
				MainFile.game.texturePack = pack;
				MainFile.game.imageLoader.reloadTextures();
			}
		}

		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {
			Color temp = g2.getColor();
			if(pack != null){
				if(pack.image != null){
					pack.image.draw(x + 5, y + 5, 50, 50);
				}
			}

			boolean hover = isMouseOver();

			if (hover) {
				g2.setColor(new Color(95, 95, 95, 174));
			} else {
				g2.setColor(new Color(95, 95, 95, 86));
			}

			if (!enabled) {
				g2.setColor(new Color(41, 41, 41, 174));
			}

			g2.fill(new Rectangle(x, y, width, height));

			if(MainFile.game.texturePack == pack){
				g2.setColor(Color.white);
				g2.draw(new Rectangle(x, y, width, height));
			}

			g2.setColor(hover ? Color.white : Color.lightGray);

			if (!enabled) g2.setColor(Color.gray);

			//			Rectangle ge = g2.getClip();
			//			g2.setClip(new Rectangle(x, y, width, height));

			FontHandler.resizeFont(g2, 22);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString(text, x + (width / 6), y + (height / 4));
			FontHandler.resetFont(g2);

			//			g2.setClip(ge);

			g2.setColor(temp);
		}
	}

	class scrollBar extends GuiObject {

		int trant = 0;

		public scrollBar( int x, int y, int width, int height, UIMenu menu ) {
			super(MainFile.game,x, y, width, height, menu);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {

			if (y >= this.y && y <= (this.y + height)) {
				float yy = (y - getY()) - 10;
				trant = (int) yy;
			}
		}

		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {
			if (trant < 0) {
				trant = 0;
			}

			if (trant > (height - 27)) {
				trant = (height - 27);
			}

			int am = 0;
			for(GuiObject ob : guiObjects)
				if(ob instanceof texturePackButton) am += 1;

			translate = trant;

			Rectangle rect = new Rectangle(x, y, width, height);

			g2.setColor(Color.lightGray);
			g2.fill(rect);

			g2.setColor(Color.black);
			g2.draw(rect);

			Rectangle re = new Rectangle(x + 1, y + trant + 1, width - 2, 25);
			g2.setColor(Color.gray);
			g2.fill(re);

			g2.setColor(Color.darkGray);
			g2.draw(re);
		}

		public void onMouseWheelMoved( int change ) {
			trant += -((change / 120) * 10);
		}
	}
}
