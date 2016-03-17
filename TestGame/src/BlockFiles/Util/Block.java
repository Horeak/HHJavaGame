package BlockFiles.Util;

import BlockFiles.BlockAir;
import BlockFiles.BlockRender.DefaultBlockRendering;
import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Blocks;
import Items.Rendering.IItemRenderer;
import Items.Utils.IInventory;
import Items.Utils.IItem;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.BlockUtils;
import WorldFiles.Chunk;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.awt.*;
import java.util.ArrayList;

public abstract class Block implements IItem{

	public static int DEFAULT_MAX_STACK_SIZE = 64;
	private int maxStackSize = DEFAULT_MAX_STACK_SIZE;

	public ArrayList<String> blockInfoList = new ArrayList<>();


	public abstract String getBlockDisplayName();
	public abstract Color getDefaultBlockColor();

	public float getMovementFriction() {
		return 1F;
	}
	public boolean isBlockSolid() {
		return true;
	}
	public boolean canPassThrough() {
		return !isBlockSolid();
	}

	public void addInfo(World world, int x, int y) {
		if(getTickBlock() != null){
			blockInfoList.add("Block is Tickable");
			blockInfoList.add("Should tick: " + getTickBlock().shouldUpdate(world, x, y));
			blockInfoList.add("Block ticks every: " + getTickBlock().blockUpdateDelay() + "s");
			blockInfoList.add("Time until update: " + (getTickBlock().blockUpdateDelay() - getTickBlock().getTimeSinceUpdate(world, x, y)) + "s");
			blockInfoList.add("");
		}

		blockInfoList.add("Light level: " + getLightValue(world, x, y));
		blockInfoList.add("Can block see sky: " + canBlockSeeSky(world, x, y));
	}

	public Shape blockBounds(int x, int y) {
		return new Rectangle(x, y, 1, 1);
	}


	public float getLightValue(World world, int x, int y) {
		if(!world.isChunkLoaded(x / Chunk.chunkSize, y / Chunk.chunkSize)) return 0F;

		LightUnit unit = world.getLightUnit(x, y);

		if(unit == null) return 0F;
		if(!world.ingnoreLightingHeight && y > Chunk.chunkSize) return unit.getLightValue();

		float tt = unit.getLightValue();

		//TODO Find a way to achieve smooth transition between the light multipliers of to time periods
		float g = (int) ((canBlockSeeSky(world, x, y) ? (ILightSource.MAX_LIGHT_STRENGTH * (world.worldTimeOfDay.lightMultiplier)) : 0));

		if (tt < g) {
			tt += g;
		}

		if (tt > ILightSource.MAX_LIGHT_STRENGTH)
			tt = ILightSource.MAX_LIGHT_STRENGTH;
		return tt;

	}

	public void updateBlock( World world, int fromX, int fromY, int curX, int curY ) {
	}

	public boolean blockClicked(World world, int x, int y, ItemStack stack){
		return false;
	}


	public abstract Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y );
	public boolean useBlockTexture() {
		return true;
	}
	public abstract void loadTextures();

	public int getMaxBlockDamage() {
		return 10;
	}

	public ItemStack getItemDropped(World world, int x, int y) {
		return new ItemStack(this);
	}
	public String getItemName() {
		return getBlockDisplayName();
	}
	public IItemRenderer getRender() {
		return DefaultBlockRendering.staticReference;
	}

	@Override
	public int getItemMaxStackSize() {
		return maxStackSize;
	}


	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		if(stack.isBlock()) {
			if (BlockUtils.canPlaceBlockAt(this, x, y)) {
				MainFile.game.getServer().getWorld().setBlock(this, x, y);

				boolean used = MainFile.game.getServer().getWorld().getBlock(x, y) == this;

				if(!used) return false;

				if (MainFile.game.getClient().getPlayer().getItem(stack.slot) != null && MainFile.game.getClient().getPlayer().getItem(stack.slot).getStackSize() > 1) {
					MainFile.game.getClient().getPlayer().getItem(stack.slot).decreaseStackSize(1);
				} else {
					MainFile.game.getClient().getPlayer().setItem(stack.slot, null);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean equals( IItem item ) {
		return item instanceof Block && Blocks.getId(this) == Blocks.getId((Block)item);
	}

	@Override
	public IItem clone() throws CloneNotSupportedException {
		return Blocks.getBlock(Blocks.getId(this));
	}


	public boolean canBlockSeeSky(World world, int x, int y) {
		if(world.getBlock(x, y, true) instanceof BlockAir) return false;

		//TODO Make sure this goes from bottom up and not top down!
		for (int g = y - 1; g > (y - (Chunk.chunkSize / 2)); g -= 1) {
			Block cc = world.getBlock(x, g);
			if(cc != null && cc.isBlockSolid()) return false;
			if(cc != null && !cc.canPassThrough()) return false;
		}

		int h = world.getBiome(x) != null ? world.getBiome(x).getHeight(x) : 0;

		return world.ingnoreLightingHeight ? true : (y - (Chunk.chunkSize)) <= h;
	}

	public IInventory getInventory(){
		return null;
	}
	public ITickBlock getTickBlock(){return null;}

}
