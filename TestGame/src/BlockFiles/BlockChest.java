package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Inventory.ChestInventory;
import BlockFiles.Util.Block;
import Guis.GuiChest;
import Guis.GuiFurnace;
import Items.Utils.IInventory;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.ConfigValues;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockChest extends BlockWood {

	public static Image frontIcon, topIcon, sideIcon;

	@Override
	public String getBlockDisplayName() {
		return "Chest";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(30, 100, 40);
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return side == EnumBlockSide.FRONT ? frontIcon : side == EnumBlockSide.TOP ? topIcon : sideIcon;
	}

	@Override
	public void loadTextures() {
		frontIcon = MainFile.game.imageLoader.getImage("blocks", "chestFront");
		topIcon = MainFile.game.imageLoader.getImage("blocks", "chestTop");
		sideIcon = MainFile.game.imageLoader.getImage("blocks", "chestSide");
	}

	@Override
	public IInventory getInventory() {
		return new ChestInventory();
	}

	public boolean blockClicked(World world, int x, int y, ItemStack stack){
		MainFile.game.setCurrentMenu(new GuiChest(MainFile.game.gameContainer, ConfigValues.PAUSE_GAME_IN_INV, world, x, y));

		return true;
	}
}
