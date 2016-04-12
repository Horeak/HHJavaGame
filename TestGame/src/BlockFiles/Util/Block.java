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
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.Chunk;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.awt.*;
import java.util.ArrayList;

public abstract class Block implements IItem{

	public static int DEFAULT_MAX_STACK_SIZE = 64;
	private int maxStackSize = DEFAULT_MAX_STACK_SIZE;

	public int registryValue = -1;

	public ArrayList<String> blockInfoList = new ArrayList<>();


	public abstract String getBlockDisplayName();

	//TODO Queue color after block instance is first created? To prevent a new color instance being created each render.
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
	public boolean opaqueRender(){return isBlockSolid();}

	public void addInfo(World world, int x, int y) {
		if(world.getTickBlock(x, y) != null){
			blockInfoList.add("Block is Tickable");
			blockInfoList.add("Should tick: " + world.getTickBlock(x, y).shouldUpdate(world, x, y));
			blockInfoList.add("Block ticks every: " + world.getTickBlock(x, y).blockUpdateDelay() + "s");
			blockInfoList.add("Time until update: " + (world.getTickBlock(x, y).blockUpdateDelay() - (world.getTickBlock(x, y).getTimeSinceUpdate(world, x, y) / 1000)) + "s"); //Divided by 1000 to make it once each second
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
	public abstract void loadTextures(TextureLoader imageLoader);

	public int getMaxBlockDamage() {
		return 10;
	}

	public abstract Material getBlockMaterial();

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


	public void onStep(){
		if(getBlockMaterial() != null){
			if(getBlockMaterial().blockWalkSound != null) {
				if(!getBlockMaterial().blockWalkSound.playing()) {
					MainFile.game.soundLoader.playSound(getBlockMaterial().blockWalkSound, true);
				}
			}
		}
	}

	public void onFall(){
		if(getBlockMaterial() != null){
			if(getBlockMaterial().blockFallSound != null) {
				if(!getBlockMaterial().blockFallSound.playing()) {
					MainFile.game.soundLoader.playSound(getBlockMaterial().blockWalkSound, true);
				}
			}
		}
	}

	public void onHit(){
		if(getBlockMaterial() != null){
			if(getBlockMaterial().blockBreakSound != null) {
				if(!getBlockMaterial().blockBreakSound.playing()) {
					MainFile.game.soundLoader.playSound(getBlockMaterial().blockBreakSound, true);
				}
			}
		}
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
	public IItem clone() {
		return Blocks.getBlock(Blocks.getId(this));
	}


	//TODO Optimize!
	public boolean canBlockSeeSky(World world, int x, int y) {
		if(world.getBlock(x, y, true) instanceof BlockAir) return false;

		for (int g = y - 1; g > (y - (Chunk.chunkSize / 2)); g -= 1) {
			Block cc = world.getBlock(x, g);
			if(cc != null && cc.isBlockSolid()) return false;
			if(cc != null && !cc.canPassThrough()) return false;
		}

		int h = world.getBiome(x / Chunk.chunkSize) != null ? world.getHeight(x) : 0;

		return world.ingnoreLightingHeight ? true : (y - (Chunk.chunkSize)) <= h;
	}


	//TODO Add a isInventory() and isTickBlock() function so that blocks will not have to create a new instance each tick to check if is valid inventory or tick block

	public IInventory getInventory(){
		return null;
	}
	public ITickBlock getTickBlock(){return null;}
}
