package WorldFiles;

import BlockFiles.BlockAir;
import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import BlockFiles.Util.ILightSource;
import BlockFiles.Util.ITickBlock;
import BlockFiles.Util.LightUnit;
import Items.Utils.IInventory;
import Main.MainFile;
import Utils.LoggerUtil;
import WorldGeneration.Structures.Structure;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Chunk implements Serializable{
	public static int chunkSize = 16;

	public ConcurrentHashMap<Point, ITickBlock> tickableBlocks = new ConcurrentHashMap<>();

	private transient LightUnit[][] lightUnits = new LightUnit[chunkSize][chunkSize];

	public String[][] blocks = new String[chunkSize][chunkSize];
	public IInventory[][] inventoryBlocks = new IInventory[chunkSize][chunkSize];

	public ConcurrentHashMap<Point, Structure> structures = new ConcurrentHashMap<>();

	public int chunkX, chunkY;
	public transient World world;

	public boolean generated = false;

	public Chunk(World world, int chunkX, int chunkY){
		this.world = world;

		this.chunkX = chunkX;
		this.chunkY = chunkY;
	}

	public void onLoad(){
	}

	public void onUnload(){
	}

	public void update(){
		for(Structure st : structures.values()){
			st.update();
		}
	}


	public Block getBlock( int x, int y ) {
		return getBlock(x, y, false);
	}

	public Block getBlock( int x, int y, boolean allowAir ) {
		if(x < 0) x *= -1;
		if(y < 0) y *= -1;

		if(x >= chunkSize)return null;
		if(y >= chunkSize)return null;

		if (blocks != null) {
			if (blocks[ x ][ y ] != null) {
				if (x >= 0 && y >= 0 && x < chunkSize && y < chunkSize) {
					if (!allowAir && blocks[ x ][ y ] != null && Blocks.getBlock(blocks[ x ][ y ]) instanceof BlockAir) {
						return null;
					}

					return Blocks.getBlock(blocks[ x ][ y ]);
				}
			}
		}else{
			LoggerUtil.exception(new Exception("Error: blocks array in Chunk was null during getBlock method!"));
		}
		return allowAir ? Blocks.blockAir : null;
	}

	public void removeTickBlock(int xPos, int yPos){
		tickableBlocks.remove(new Point(xPos, yPos));
	}

	public void setBlock( Block block, int xPos, int yPos ) {
		int wX = xPos + (chunkX * chunkSize), wY = yPos + (chunkY * chunkSize);
		if(wX < 0) wX *= -1;
		if(wY < 0) wY *= -1;

		if(xPos >= chunkSize) return;
		if(yPos >= chunkSize) return;

		if(xPos < 0) return;
		if(yPos < 0) return;


		if(!world.generating) {
			removeTickBlock(wX, wY);
			inventoryBlocks[xPos][yPos] = null;
		}

		if(getStructure(xPos, yPos) != null && block == null){
			if(getStructure(xPos, yPos).shouldRemoveBlock(xPos, yPos)){
				structures.remove(new Point(xPos,yPos));
			}
		}


		if (blocks != null) {
			if (xPos >= 0 && yPos >= 0) {
				if (xPos < chunkSize && yPos < chunkSize) {
					blocks[xPos][yPos] = Blocks.getId(block);

					if (block != null) {
						if (block.getTickBlock() != null) {
							tickableBlocks.put(new Point(xPos, yPos), block.getTickBlock());
						}

						IInventory in = block.getInventory();

						if (in != null) {
							inventoryBlocks[xPos][yPos] = in;
						}


						if (xPos < 0 || xPos > chunkSize || yPos < 0 || yPos > chunkSize)
							return;

						if (lightUnits == null)
							return;

						lightUnits[xPos][yPos] = new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0);
					}
				}
			}
		}else{
			LoggerUtil.exception(new Exception("Error: blocks array in Chunk was null during setBlock method!"));
		}
	}

	public Structure getStructure(int x, int y){
		Structure st = structures.get(new Point(x, y));

		if(st != null && st.world == null){
			st.world = world;
		}

		return st;
	}

	public void setStructure(Structure st){
		for(Point t : st.blocks.keySet()){
			structures.put(t, st);
		}
	}

	public void setStructure(Structure st, int x, int y){
		if(st == null){
			structures.remove(new Point(x, y));
		}else{
			structures.put(new Point(x, y), st);
		}
	}

	public boolean isAirBlock( int x, int y ) {
		if (getBlock(x, y, true) instanceof BlockAir) {
			return true;
		}

		return false;
	}

	public LightUnit getLightUnit(int xPos, int yPos){
		if(lightUnits != null){
			if(xPos >= 0 && yPos >= 0){
				if(xPos < chunkSize && yPos < chunkSize){
					if(lightUnits[xPos][yPos] == null){
						lightUnits[xPos][yPos] = new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0F);
					}

					return lightUnits[xPos][yPos];
				}
			}
		}else{
			lightUnits = new LightUnit[chunkSize][chunkSize];
		}

		return new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0F);
	}

	//TODO Have EntityPlayer input so that it can check multiple players for future multiplayer
	public static boolean shouldRangeLoad(int chunkX, int chunkY){
		return MainFile.game.getClient() != null && MainFile.game.getClient().getPlayer() != null && MainFile.game.getClient().getPlayer().getEntityPostion().distance((chunkX * chunkSize) , (chunkY * chunkSize)) < (chunkSize * 2.2F);
	}

	public boolean shouldBeLoaded(){
		if(tickableBlocks != null && tickableBlocks.size() > 0) {
			for (Map.Entry<Point, ITickBlock> ent : new HashMap<Point, ITickBlock>(tickableBlocks).entrySet()) {
				if (ent == null || ent.getKey() == null || ent.getValue() == null) continue;

				if (ent.getValue().shouldBlockLoadChunk(world, ent.getKey().x * chunkSize, ent.getKey().y * chunkSize))
					return true;
			}
		}

		return shouldRangeLoad(chunkX, chunkY);
	}


	public IInventory getInventory(int x, int y){
		return inventoryBlocks[x][y];
	}
	public ITickBlock getTickBlock(int x, int y){
		return tickableBlocks.get(new Point(x, y));
	}

	public void setInventory(IInventory inv, int x, int y){
		inventoryBlocks[x][y] = inv;
	}
	public void setTickBlock(ITickBlock tickBlock, int x, int y){
		tickableBlocks.put(new Point(x, y), tickBlock);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Chunk chunk = (Chunk) o;

		if (chunkX != chunk.chunkX) return false;
		if (chunkY != chunk.chunkY) return false;
		if (generated != chunk.generated) return false;
		if (tickableBlocks != null ? !tickableBlocks.equals(chunk.tickableBlocks) : chunk.tickableBlocks != null)
			return false;
		if (!Arrays.deepEquals(blocks, chunk.blocks)) return false;
		if (!Arrays.deepEquals(inventoryBlocks, chunk.inventoryBlocks)) return false;
		if (world != null ? !world.equals(chunk.world) : chunk.world != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = tickableBlocks != null ? tickableBlocks.hashCode() : 0;
		result = 31 * result + Arrays.deepHashCode(blocks);
		result = 31 * result + Arrays.deepHashCode(inventoryBlocks);
		result = 31 * result + chunkX;
		result = 31 * result + chunkY;
		result = 31 * result + (world != null ? world.hashCode() : 0);
		result = 31 * result + (generated ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Chunk{" +
				"tickableBlocks=" + tickableBlocks +
				", blocks=" + Arrays.toString(blocks) +
				", inventoryBlocks=" + Arrays.toString(inventoryBlocks) +
				", chunkX=" + chunkX +
				", chunkY=" + chunkY +
				", world=" + world +
				", generated=" + generated +
				'}';
	}
}
