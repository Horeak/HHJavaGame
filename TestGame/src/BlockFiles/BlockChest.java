package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Inventory.ChestInventory;
import BlockFiles.Util.Material;
import Guis.GuiChest;
import Items.Utils.IInventory;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.ConfigValues;
import Utils.TexutrePackFiles.TextureLoader;
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
	public void loadTextures(TextureLoader imageLoader) {
		frontIcon = imageLoader.getImage("blocks", "chestFront");
		topIcon = imageLoader.getImage("blocks", "chestTop");
		sideIcon = imageLoader.getImage("blocks", "chestSide");
	}

	@Override
	public Material getBlockMaterial() {
		return Material.WOOD;
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
