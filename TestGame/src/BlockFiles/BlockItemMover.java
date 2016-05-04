package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.ITickBlock;
import BlockFiles.Util.Material;
import Items.Utils.IInventory;
import Items.Utils.ItemStack;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.awt.*;

public class BlockItemMover extends Block{

	public static Image[] blockDirections = new Image[4];
	public static Image input,output;

	@Override
	public void addInfo(World world, int x, int y) {
		super.addInfo(world, x, y);

		if(world.getTickBlock(x, y) instanceof ItemMoverTickBlock){
			ItemMoverTickBlock tc = (ItemMoverTickBlock)world.getTickBlock(x, y);
			blockInfoList.add("Rotation: " + tc.rotation);
			blockInfoList.add("Input: " + tc.getInputBlock(x, y));
			blockInfoList.add("Output: " + tc.getOutputBlock(x, y));
		}
	}

	@Override
	public String getBlockDisplayName() {
		return "Item Mover";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.darkGray;
	}

	@Override
	public Material getBlockMaterial() {
		return Material.MACHINE;
	}

	@Override
	public Image getBlockTextureFromSide(EnumBlockSide side, World world, int x, int y)
	{
		if(world == null){
			return side == EnumBlockSide.TOP ? input : blockDirections[0];
		}else {

			ITickBlock bl = world.getTickBlock(x, y);

			if (bl instanceof ItemMoverTickBlock) {
				ItemMoverTickBlock tc = (ItemMoverTickBlock) bl;

				if (side == EnumBlockSide.FRONT || side == EnumBlockSide.TOP && (tc.rotation == 2 || tc.rotation == 4) || side == EnumBlockSide.SIDE && (tc.rotation == 1 || tc.rotation == 3)) {
					return blockDirections[ tc.rotation - 1 ];
				}else{
					if(side == EnumBlockSide.TOP){
						if(tc.rotation == 1){
							return input;
						}else if(tc.rotation == 3){
							return output;
						}
					}else if(side == EnumBlockSide.SIDE){
						if(tc.rotation == 2){
							return input;
						}else if(tc.rotation == 4){
							return output;
						}
					}
				}

			}
		}

		return null;
	}

	/**
	 * rotation values:
	 * 1 = up->down
	 * 2 = right->left
	 * 3 = down->up
	 * 4 = left->right
	 * @param imageLoader
	 */
	@Override
	public void loadTextures(TextureLoader imageLoader) {
		blockDirections[0] = imageLoader.getImage("blocks", "itemMoverDirection").getFlippedCopy(true, true);
		blockDirections[2] = imageLoader.getImage("blocks", "itemMoverDirection");

		blockDirections[1] = imageLoader.getImage("blocks", "itemMoverDirectionRight");
		blockDirections[3] = imageLoader.getImage("blocks", "itemMoverDirectionRight").getFlippedCopy(true, true);

		input = imageLoader.getImage("blocks", "itemMoverInput");
		output = imageLoader.getImage("blocks", "itemMoverOutput");

	}


	@Override
	public ITickBlock getTickBlock() {
		return new ItemMoverTickBlock();
	}

	//TODO Find a way where a keybind will rotate it (For example add a keybind to "R" that rotates blocks (perhaps have IRotateable(Would have to be stored in tickblocks or inventoryies to avoid adding more save data to chunks))
	@Override
	public boolean blockClicked(World world, int x, int y, ItemStack stack) {
		ITickBlock bl = world.getTickBlock(x, y);

		if(bl instanceof ItemMoverTickBlock){
			ItemMoverTickBlock tc = (ItemMoverTickBlock)bl;
			if(tc.rotation < 4){
				tc.rotation += 1;
			}else{
				tc.rotation = 1;
			}

		}


		return super.blockClicked(world, x, y, stack);
	}


	class ItemMoverTickBlock implements ITickBlock{


		/**
		 * rotation values:
		 * 1 = up->down
		 * 2 = right->left
		 * 3 = down->up
		 * 4 = left->right
		 */
		int rotation = 1;
		int timeSinceUpdate = 0;

		public Point getInputBlock(int x, int y){
			if(rotation == 1){
				return new Point(x, y - 1);

			}else if(rotation == 2){
				return new Point(x + 1, y);

			}else if(rotation == 3){
				return new Point(x, y + 1);

			}else if(rotation == 4){
				return new Point(x - 1, y);

			}

			return null;
		}

		public Point getOutputBlock(int x, int y){
			if(rotation == 1){
				return new Point(x, y + 1);

			}else if(rotation == 2){
				return new Point(x - 1, y);

			}else if(rotation == 3){
				return new Point(x, y - 1);

			}else if(rotation == 4){
				return new Point(x + 1, y);

			}

			return null;
		}

		public boolean validInputBlock(World world, int x, int y){
			Point p = getInputBlock(x, y);

			if(p == null) return false;

			return world.getInventory(p.x, p.y) != null;
		}

		public boolean validOutputBlock(World world, int x, int y){
			Point p = getOutputBlock(x, y);

			if(p == null) return false;

			return world.getInventory(p.x, p.y) != null;
		}

		@Override
		public boolean shouldUpdate(World world, int x, int y) {
			return validInputBlock(world, x, y) && validOutputBlock(world, x, y);
		}



		@Override
		public int getTimeSinceUpdate(World world, int x, int y) {
			return timeSinceUpdate;
		}

		@Override
		public void setTimeSinceUpdate(World world, int x, int y, int i) {
			timeSinceUpdate = i;
		}

		//TODO Maybe add multiple tiers of this? (For example move 10 items at a time instead of only 1?)
		@Override
		public void tickBlock(World world, int x, int y) {
			Point pInput = getInputBlock(x, y);
			Point pOutput = getOutputBlock(x, y);

			IInventory input = world.getInventory(pInput.x, pInput.y);
			IInventory output = world.getInventory(pOutput.x, pOutput.y);

			for(int i = 0; i < input.getInventorySize(); i++){
				ItemStack item = input.getItem(i);

				if(!input.canOutputfromSlot(i)) continue;

				if(item != null && item.getItem() != null){
					//TODO This is adding too many items for some reason? (One stack of torches became 64 + 2)
					ItemStack temp = new ItemStack(item);
					temp.setStackSize(1);

					if(output.addItem(temp)){
//						input.consumeFromSlot(null, i);
						input.getItem(i).decreaseStackSize(1);

						if(input.getItem(i).getStackSize() <= 0){
							input.setItem(i, null);
						}

						return;
					}
				}

			}

		}
	}

}
