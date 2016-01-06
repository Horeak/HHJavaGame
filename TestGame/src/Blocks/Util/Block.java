package Blocks.Util;
/*
* Project: Random Java Creations
* Package: Blocks
* Created: 26.07.2015
*/

import Blocks.BlockRender.DefaultBlockRendering;
import Blocks.BlockRender.EnumBlockSide;
import Items.Rendering.IItemRenderer;
import Items.Utils.IItem;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.BlockUtils;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.awt.*;
import java.util.ArrayList;

public abstract class Block implements IItem {

	public static int DEFAULT_MAX_STACK_SIZE = 64;

	public ArrayList<String> blockInfoList = new ArrayList<>();

	private int blockDamage = 0;
	private int blockBreakDelay = 0, blockBreakReach = 200;

	private int maxStackSize = DEFAULT_MAX_STACK_SIZE;

	private LightUnit unit = new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0);

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
		if(this instanceof ITickBlock){
			blockInfoList.add("Block is Tickable");
			blockInfoList.add("Should tick: " + ((ITickBlock)this).shouldupdate(world, x, y));
			blockInfoList.add("Block ticks every: " + ((ITickBlock)this).blockupdateDelay() + "s");
			blockInfoList.add("Time until update: " + (((ITickBlock)this).blockupdateDelay() - ((ITickBlock)this).getTimeSinceUpdate()) + "s");
			blockInfoList.add("");
		}

		blockInfoList.add("Block damage: " + getBlockDamage() + " / " + getMaxBlockDamage());
		blockInfoList.add("Light level: " + getLightValue(world, x, y));
		blockInfoList.add("Light color: " + getLightUnit().getLightColor());
		blockInfoList.add("Can block see sky: " + canBlockSeeSky(world, x, y));

	}

	public Shape blockBounds(int x, int y) {
		return new Rectangle(x, y, 1, 1);
	}

	public int getLightValue(World world, int x, int y) {
		int tt = unit.getLightValue();

		//TODO Find a way to achieve smooth transition between the light multipliers of to time periods

		int g = (int) ((canBlockSeeSky(world, x, y) ? (ILightSource.MAX_LIGHT_STRENGTH * (world.worldTimeOfDay.lightMultiplier)) : 0));

		if (tt < g) {
			tt += g;
		}

		if (tt > ILightSource.MAX_LIGHT_STRENGTH) tt = ILightSource.MAX_LIGHT_STRENGTH;
		return tt;
	}

	public void setLightValue( int lightValue ) {
		unit.setLightValue(lightValue);
	}
	public LightUnit getLightUnit() {
		return unit;
	}
	public void updateBlock( World world, int fromX, int fromY, int curX, int curY ) {
		if (blockBreakDelay >= blockBreakReach) {
			blockDamage = 0;
		} else if (getBlockDamage() > 0 && blockBreakDelay < blockBreakReach) {
			blockBreakDelay += 1;
		}
	}


	public abstract Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y );
	public boolean useBlockTexture() {
		return true;
	}

	public int getMaxBlockDamage() {
		return 10;
	}

	public int getBlockDamage() {
		return blockDamage;
	}
	public void setBlockDamage( int blockDamage ) {
		this.blockDamage = blockDamage;
		blockBreakDelay = 0;
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
			Block block = null;

			try {
				block = (Block)stack.getBlock().clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}


			if (BlockUtils.canPlaceBlockAt(block, x, y)) {
				MainFile.currentWorld.setBlock(block, x, y);


				if (MainFile.currentWorld.player.getItem(stack.slot) != null && MainFile.currentWorld.player.getItem(stack.slot).getStackSize() > 1) {
					MainFile.currentWorld.player.getItem(stack.slot).decreaseStackSize(1);
				} else {
					MainFile.currentWorld.player.setItem(stack.slot, null);
				}

				return true;
			}
		}

		return false;
	}

	public boolean canBlockSeeSky(World world, int x, int y) {
		for (int g = y - 1; g > 0; g -= 1) {
			Block cc = world.getBlock(x, g);
			if(cc != null && cc.isBlockSolid()) return false;
			if(cc != null && !cc.canPassThrough()) return false;
		}

		return true;
	}


	@Override
	public IItem clone() throws CloneNotSupportedException {
		try {
			Block block = this.getClass().newInstance();
			return block;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean equals( Object o ) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Block)) {
			return false;
		}

		Block block = (Block) o;

		return unit.equals(block.unit);

	}

	@Override
	public int hashCode() {
		int result = getBlockDamage();
		result = 31 * result + unit.hashCode();
		return result;
	}
}
