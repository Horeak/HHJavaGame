package Blocks.Util;
/*
* Project: Random Java Creations
* Package: Blocks
* Created: 26.07.2015
*/

import Blocks.BlockRender.DefaultBlockRendering;
import Blocks.BlockRender.EnumBlockSide;
import Items.IItem;
import Items.Rendering.IItemRenderer;
import Main.MainFile;
import Utils.BlockUtils;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.awt.*;
import java.util.ArrayList;

public abstract class Block implements IItem {

	public static int DEFAULT_MAX_STACK_SIZE = 64;

	public int x, y;
	public World world;

	public ArrayList<String> blockInfoList = new ArrayList<>();
	int blockBreakDelay = 0, blockBreakReach = 200;
	private int stackDamage = 0;
	private int stackSize = 1;
	private int maxStackSize = DEFAULT_MAX_STACK_SIZE;
	private int blockDamage = 0;
	private LightUnit unit = new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0);
	public Block( int x, int y ) {
		this.x = x;
		this.y = y;
		this.world = MainFile.currentWorld;
	}

	public Block() {
		this(0, 0);
	}

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

	public void addInfo() {
		blockInfoList.add("Block damage: " + getBlockDamage() + " / " + getMaxBlockDamage());
		blockInfoList.add("Light level: " + getLightValue());
		blockInfoList.add("Light color: " + getLightUnit().getLightColor());
		blockInfoList.add("Can block see sky: " + canBlockSeeSky());

	}

	public Shape blockBounds() {
		return new Rectangle(x, y, 1, 1);
	}

	public int getLightValue() {
		int tt = unit.getLightValue();
		int g = (canBlockSeeSky() ? (int) ((float) ILightSource.MAX_LIGHT_STRENGTH * world.worldTimeOfDay.lightMultiplier) : 0);
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

	public void updateBlock( World world, int fromX, int fromY ) {
		if (blockBreakDelay >= blockBreakReach) {
			blockDamage = 0;
		} else if (getBlockDamage() > 0 && blockBreakDelay < blockBreakReach) {
			blockBreakDelay += 1;
		}
	}


	public abstract Image getBlockTextureFromSide( EnumBlockSide side );
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


	public IItem getItemDropped() {
		return this;
	}



	public String getItemName() {
		return getBlockDisplayName();
	}

	public IItemRenderer getRender() {
		return DefaultBlockRendering.staticReference;
	}


	@Override
	public int getItemDamage() {
		return stackDamage;
	}

	@Override
	public int getItemStackSize() {
		return stackSize;
	}

	@Override
	public int getItemMaxStackSize() {
		return maxStackSize;
	}

	@Override
	public String getItemID() {
		return "block." + getBlockDisplayName().toLowerCase().replace(" ", "_") + "." + getItemDamage();
	}


	public boolean useItem( World world, int x, int y ) throws IllegalAccessException, InstantiationException {
		Block block = this.getClass().newInstance();

		if (BlockUtils.canPlaceBlockAt(block, x, y)) {
			MainFile.currentWorld.setBlock(block, x, y);
			return true;
		}

		return false;
	}

	@Override
	public void decreaseStackSize( int i ) {
		stackSize -= i;
	}

	@Override
	public void increaseStackSize( int i ) {
		stackSize += i;

		if (stackSize > maxStackSize) stackSize = maxStackSize;
	}

	@Override
	public void setStackSize( int i ) {
		stackSize = i;
	}

	@Override
	public void onItemUsed( int hotbarSlot ) {
		if (MainFile.currentWorld.player.getItem(hotbarSlot) != null && MainFile.currentWorld.player.getItem(hotbarSlot).getItemStackSize() > 1) {
			MainFile.currentWorld.player.getItem(hotbarSlot).decreaseStackSize(1);
		} else {
			MainFile.currentWorld.player.setItem(hotbarSlot, null);
		}
	}

	public boolean canBlockSeeSky() {
		for (int g = y - 1; g > 0; g -= 1) {
			if (world.getBlock(x, g) != null) {
				return false;
			}
		}

		return true;
	}


	@Override
	public IItem clone() throws CloneNotSupportedException {
		try {
			Block block = this.getClass().newInstance();

			block.x = x;
			block.y = y;

			block.world = world;

			block.blockDamage = blockDamage;
			block.stackSize = stackSize;
			block.stackDamage = stackDamage;

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

		if (x != block.x) {
			return false;
		}
		if (y != block.y) {
			return false;
		}
		if (stackDamage != block.stackDamage) {
			return false;
		}
		if (stackSize != block.stackSize) {
			return false;
		}
		if (maxStackSize != block.maxStackSize) {
			return false;
		}
		if (getBlockDamage() != block.getBlockDamage()) {
			return false;
		}
		if (!world.equals(block.world)) {
			return false;
		}
		return unit.equals(block.unit);

	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		result = 31 * result + world.hashCode();
		result = 31 * result + stackDamage;
		result = 31 * result + stackSize;
		result = 31 * result + maxStackSize;
		result = 31 * result + getBlockDamage();
		result = 31 * result + unit.hashCode();
		return result;
	}
}
