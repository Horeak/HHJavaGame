package Guis.Interfaces;

import Guis.Objects.GuiButton;
import Guis.Objects.MainMenuButton;
import Interface.UIMenu;
import Main.MainFile;
import Utils.FontHandler;
import WorldFiles.GameMode;
import WorldFiles.World;
import WorldFiles.WorldGenType;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;


public class CreateWorldMenu extends AbstractMainMenu {


	//TODO Add world seed input!
	//TODO Add difficulty?

	public CreateWorldMenu guiInst = this;

	public boolean textInput = false;
	public String worldName = "";

	public int worldModeInt = 0;
	public GameMode gameMode = GameMode.SURVIVAL;

	public int worldGenInt = 0;
	public WorldGenType worldGenType = WorldGenType.WorldGenTypes.NORMAL_WORLD.genType;

	createWorldButton createWorldButton;

	GuiButton gameModeButton;
	GuiButton worldGenTypeButton;

	public CreateWorldMenu() {
		super();

		int buttonSize = 50, buttonPos =  (buttonSize);

		createWorldButton = new createWorldButton(buttonPos * 8);
		createWorldButton.enabled = false;

		guiObjects.add(new backButton((buttonPos * 14) - (buttonSize / 2)));
		guiObjects.add(new worldNameInput(buttonPos * 2 + 20));

		gameModeButton = new MainMenuButton(MainFile.game,renderStart, (buttonPos * 3) + 20,190, 32, "Creative: ", guiInst){

			@Override
			public void renderObject(Graphics g2, UIMenu menu) {
				super.renderObject(g2, menu);
				text = gameMode.name + " mode";
			}

			@Override
			public void onClicked(int button, int x, int y, UIMenu menu) {
				int i = 0;
				for(GameMode em : GameMode.values()){
					if(i > worldModeInt){
						worldModeInt += 1;
						gameMode = em;
						return;
					}

					i += 1;
				}

				worldModeInt = 0;
				gameMode = GameMode.SURVIVAL;
			}
		};

		worldGenTypeButton = new MainMenuButton(MainFile.game,renderStart, (buttonPos * 4) + 20,190, 32, "", guiInst){

			@Override
			public void renderObject(Graphics g2, UIMenu menu) {
				super.renderObject(g2, menu);
				text = worldGenType.getWorldTypeName();
			}

			@Override
			public void onClicked(int button, int x, int y, UIMenu menu) {
				int i = 0;

				//????

				for(WorldGenType.WorldGenTypes emm : WorldGenType.WorldGenTypes.values()){

					if(i > worldGenInt){
						worldGenInt += 1;
						worldGenType = emm.genType;
						return;
					}

					i += 1;
				}

				worldGenInt = 0;
				worldGenType = WorldGenType.WorldGenTypes.NORMAL_WORLD.genType;
			}
		};


		guiObjects.add(gameModeButton);
		guiObjects.add(worldGenTypeButton);
		guiObjects.add(createWorldButton);

	}

	@Override
	public void render( Graphics g2 ) {
		if (!worldName.isEmpty()) {
			createWorldButton.enabled = true;
		} else {
			createWorldButton.enabled = false;
		}

		org.newdawn.slick.Color temp = g2.getColor();

		super.render(g2);

		//g2.setPaint(p);
		g2.setColor(org.newdawn.slick.Color.black);

		FontHandler.resizeFont(g2, 16);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Enter world name:", renderStart + 5, 100);
		FontHandler.resetFont(g2);

		g2.setColor(temp);
	}

	@Override
	public boolean canRender() {
		return true;
	}

	public void buttonPressed( GuiButton button ) {
	}

	public void keyPressed( int key, char c ) {
		if (textInput) {
			if (key == Input.KEY_BACK) {
				if (worldName.length() > 0) {
					worldName = worldName.substring(0, worldName.length() - 1);
				}
			} else {
				if (worldName.length() < 15) {

					if (Character.isDefined(c))
						if (Character.isLetter(c) || Character.isDigit(c) || Character.isSpaceChar(c)) {
						worldName += c;
					}
				}
			}

		}
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

	class createWorldButton extends MainMenuButton {
		public createWorldButton( int y ) {
			super(MainFile.game,renderStart, y, 190, 32, "Create world", guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			if (createWorldButton.enabled) {
				MainFile.game.getServer().setWorld(new World(worldName));

				MainFile.game.getServer().getWorld().gameMode = gameMode;
				MainFile.game.getServer().getWorld().worldGenType = worldGenType;

				MainFile.game.getServer().getWorld().generate();
				MainFile.game.getServer().getWorld().start();

				MainFile.game.setCurrentMenu(null);
			}
		}
	}

	class worldNameInput extends MainMenuButton {

		public worldNameInput( int y ) {
			super(MainFile.game,renderStart, y, 190, 32, null, guiInst);
		}

		@Override
		public void onClicked( int button, int x, int y, UIMenu menu ) {
			textInput ^= true;
		}

		@Override
		public void renderObject( Graphics g2, UIMenu menu ) {

			org.newdawn.slick.Color temp = g2.getColor();
			g2.setColor(FontHandler.getColorToSlick(textInput ? new Color(91, 91, 91, 185) : new Color(20, 20, 20, 185)));


			g2.fill(new Rectangle(x, y, width, height));

			g2.setColor(org.newdawn.slick.Color.white);
			FontHandler.resizeFont(g2, 22);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString(worldName, renderStart + 3, y);

			FontHandler.resetFont(g2);

			if (textInput) {
				int xx = renderStart + 3 + (worldName != null ? worldName.length() * 12 : 0);

				FontHandler.resizeFont(g2, 22);
				g2.drawString("_", xx, y);
				FontHandler.resetFont(g2);
			}


			g2.setColor(temp);
		}
	}

}
