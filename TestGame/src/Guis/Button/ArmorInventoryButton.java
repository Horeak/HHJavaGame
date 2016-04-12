package Guis.Button;

import Guis.GuiGame;
import Interface.UIMenu;
import Main.MainFile;
import Utils.TexutrePackFiles.TexturePack;
import org.newdawn.slick.Graphics;

public class ArmorInventoryButton extends InventoryButton {

	public int num;
	public GuiGame gui;

	public static org.newdawn.slick.Image ghostHat, ghostChest, ghostPants, ghostShoes;
	public static TexturePack prevTexture;

	public ArmorInventoryButton( GuiGame gui, int x, int y,  int number ) {
		super(gui,x, y, 32, 32, false, number, MainFile.game.getClient().getPlayer().armorInventory);

		this.num = number;
		this.gui = gui;

		if(prevTexture == null){
			prevTexture = MainFile.game.texturePack;
		}

		boolean newTexture = prevTexture != MainFile.game.texturePack;

		if(ghostHat == null && number == 0 || number == 0 && newTexture){
			ghostHat = MainFile.game.getImageLoader().getImage("items/armor", "ghostHat");

		}else if(ghostChest == null && number == 1 || number == 1 && newTexture){
			ghostChest = MainFile.game.getImageLoader().getImage("items/armor", "ghostChest");

		}else if(ghostPants == null && number == 2 || number == 2 && newTexture){
			ghostPants = MainFile.game.getImageLoader().getImage("items/armor", "ghostPants");

		}else if(ghostShoes == null && number == 3 || number == 3 && newTexture){
			ghostShoes = MainFile.game.getImageLoader().getImage("items/armor", "ghostShoes");
		}

		prevTexture = MainFile.game.texturePack;
	}


	@Override
	public void renderObject( Graphics g2, UIMenu menu ) {
		super.renderObject(g2, menu);

		if(inv.getItem(num) == null) {
			if (num == 0) {
				ghostHat.draw(x + 3, y + 3, x + 28, y + 31, 0, 0, 16, 16);

			} else if (num == 1) {
				ghostChest.draw(x + 1, y, x + 30, y + 30, 0, 0, 16, 16);

			} else if (num == 2) {
				ghostPants.draw(x - 1, y, x + 32, y + 32, 0, 0, 16, 16);

			} else if (num == 3) {
				ghostShoes.draw(x + 3, y - 5, x + 28, y + 30, 0, 0, 16, 16);

			}
		}

	}
}