package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Inventory.FurnaceInventory;
import BlockFiles.Util.Block;
import BlockFiles.Util.ITickBlock;
import Crafting.CraftingRegister;
import Guis.GuiFurnace;
import Items.Utils.IInventory;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.ConfigValues;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.awt.*;
import java.util.HashMap;

public class BlockFurnace extends Block {

	public static Image furnaceFrontOff, furnaceFrontOn, furnaceSide, furnaceTop;

	public void addInfo(World world, int x, int y) {
		super.addInfo(world, x, y);

		if(world.getInventory(x, y) == null || !(world.getInventory(x, y) instanceof FurnaceInventory)) return;

		ItemStack input = world.getInventory(x, y).getItem(0);
		ItemStack fuel = world.getInventory(x, y).getItem(1);
		ItemStack output = world.getInventory(x, y).getItem(2);

		String tI = (input != null ? input.getItem() != null ? input.getStackName() : null : null);
		String tF = (fuel != null ? fuel.getItem() != null ? fuel.getStackName() : null : null);
		String tO = (output != null ? output.getItem() != null ? output.getStackName() : null : null);

		blockInfoList.add("");

		blockInfoList.add("Input item: " + tI);
		blockInfoList.add("Fuel item: " + tF);
		blockInfoList.add("Output item: " + tO);
		blockInfoList.add("Smelt time: " + (((FurnaceInventory) world.getInventory(x, y)).smeltTime));
		blockInfoList.add("Fuel: " + (((FurnaceInventory) world.getInventory(x, y)).fuel));
	}

	@Override
	public String getBlockDisplayName() {
		return "Furnace";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.darkGray;
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return side == EnumBlockSide.FRONT ? (world.getInventory(x, y) instanceof FurnaceInventory && ((FurnaceInventory)world.getInventory(x, y)).smeltTime > 0) ? furnaceFrontOn : furnaceFrontOff : side == EnumBlockSide.SIDE ? furnaceSide : furnaceTop;
	}

	@Override
	public void loadTextures() {
		furnaceFrontOff = MainFile.game.imageLoader.getImage("blocks", "furnaceFrontOff");
		furnaceFrontOn = MainFile.game.imageLoader.getImage("blocks", "furnaceFrontOn");

		furnaceSide = MainFile.game.imageLoader.getImage("blocks", "furnaceSide");
		furnaceTop = MainFile.game.imageLoader.getImage("blocks", "furnaceTop");
	}

	public void updateBlock( World world, int fromX, int fromY, int curX, int curY ) {
	}

	public boolean blockClicked(World world, int x, int y, ItemStack stack){
		MainFile.game.setCurrentMenu(new GuiFurnace(MainFile.game.gameContainer, ConfigValues.PAUSE_GAME_IN_INV, world, x, y));

		return true;
	}

	@Override
	public IInventory getInventory() {
		return new FurnaceInventory();
	}

	public ITickBlock getTickBlock(){
		return new furanceTickBlock();
	}



	class furanceTickBlock implements ITickBlock{
		@Override
		public boolean shouldUpdate( World world, int x, int y ) {
			return true;
		}

		@Override
		public int getTimeSinceUpdate(World world, int x, int y) {
			return world.getInventory(x, y) instanceof FurnaceInventory ? ((FurnaceInventory)world.getInventory(x, y)).timeSinceUpdate : 0;
		}

		@Override
		public void setTimeSinceUpdate(World world, int x, int y, int i ) {
			if(world.getInventory(x, y) instanceof FurnaceInventory){
				((FurnaceInventory)world.getInventory(x, y)).timeSinceUpdate = i;
			}
		}

		@Override
		public void tickBlock( World world, int x, int y ) {
			if(world.getInventory(x, y) instanceof FurnaceInventory){
				((FurnaceInventory)world.getInventory(x, y)).update();
			}
		}

	}

}
