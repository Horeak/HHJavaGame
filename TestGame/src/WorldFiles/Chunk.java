package WorldFiles;

import BlockFiles.BlockAir;
import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import BlockFiles.Util.ILightSource;
import BlockFiles.Util.ITickBlock;
import BlockFiles.Util.LightUnit;
import EntityFiles.EntityItem;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.LoggerUtil;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class Chunk implements Serializable{
	public static int chunkSize = 16;

	public ArrayList<Point> tickableBlocks = new ArrayList<>();

	private LightUnit[][] lightUnits = new LightUnit[chunkSize][chunkSize];
	public Block[][] blocks = new Block[chunkSize][chunkSize];

	public int chunkX, chunkY;

	public transient World world;

	//TODO Unload and save chunks when not in range
	public Chunk(World world, int chunkX, int chunkY){
		this.world = world;

		this.chunkX = chunkX;
		this.chunkY = chunkY;

	}

	public Block getBlock( int x, int y, boolean allowAir ) {
		int Ux = x - (chunkX * chunkSize);
		int Uy = y - (chunkY * chunkSize);

		if (blocks != null) {
			if (Ux >= 0 && Uy >= 0 && Ux < chunkSize && Uy < chunkSize) {
				if (!allowAir && blocks[ Ux ][ Uy ] instanceof BlockAir) {
					return null;
				}

				return blocks[ Ux ][ Uy ];
			}
		}
		return null;
	}

	public void removeTickBlock(int x, int y){
		int Ux = x - (chunkX * chunkSize);
		int Uy = y - (chunkY * chunkSize);

		tickableBlocks.remove(new Point(Ux, Uy));
	}

	public void setBlock( Block block, int x, int y ) {
		int Ux = x - (chunkX * chunkSize);
		int Uy = y - (chunkY * chunkSize);

		if(world == null){
			LoggerUtil.out.log(Level.SEVERE, "ERROR: Chunk has null world instance!");

			world = MainFile.game.getServer().getWorld();

//			return;
		}

		if(!world.generating) {
			removeTickBlock(x, y);
		}

		if (blocks != null) {
			if (Ux >= 0 && Uy >= 0) {
				if (Ux < chunkSize && Uy < chunkSize) {

					if (block != null) {
						blocks[ Ux ][ Uy ] = block;

						if(block instanceof ITickBlock){
							tickableBlocks.add(new Point(Ux,Uy));
						}

					} else {
						blocks[ Ux ][ Uy ] = Blocks.blockAir;
					}

					if(!world.generating) {
						getBlock(x, y, true).updateBlock(world, x, y, x, y);
						world.updateNearbyBlocks(x, y);
					}


					lightUnits[Ux][Uy] = new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0);
				}
			}
		}
	}

	public boolean isAirBlock( int x, int y ) {
		if (getBlock(x, y, true) instanceof BlockAir) {
			return true;
		}

		return false;
	}

	public LightUnit getLightUnit(int x, int y){
		x -= (chunkX * chunkSize);
		y -= (chunkY * chunkSize);

		if(lightUnits != null){
			if(x >= 0 && y >= 0){
				if(x < chunkSize && y < chunkSize){
					return lightUnits[x][y];
				}
			}
		}

		return new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0F);
	}

	public boolean shouldBeLoaded(){
		return MainFile.game.getClient().getPlayer().getEntityPostion().distance((chunkX * chunkSize) + (chunkSize / 2), (chunkY * chunkSize) + (chunkSize / 2)) <= (chunkSize * 1.75F);
	}


	@Override
	public boolean equals( Object o ) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Chunk)) {
			return false;
		}

		Chunk chunk = (Chunk) o;

		if (chunkX != chunk.chunkX) {
			return false;
		}
		if (chunkY != chunk.chunkY) {
			return false;
		}
		if (!Arrays.deepEquals(lightUnits, chunk.lightUnits)) {
			return false;
		}
		if (tickableBlocks != null ? !tickableBlocks.equals(chunk.tickableBlocks) : chunk.tickableBlocks != null) {
			return false;
		}
		if (!Arrays.deepEquals(blocks, chunk.blocks)) {
			return false;
		}
		if (world != null ? !world.equals(chunk.world) : chunk.world != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = Arrays.deepHashCode(lightUnits);
		result = 31 * result + (tickableBlocks != null ? tickableBlocks.hashCode() : 0);
		result = 31 * result + Arrays.deepHashCode(blocks);
		result = 31 * result + chunkX;
		result = 31 * result + chunkY;
		result = 31 * result + (world != null ? world.hashCode() : 0);
		return result;
	}


	@Override
	public String toString() {
		return "Chunk{" +
				"lightUnits=" + Arrays.toString(lightUnits) +
				", tickableBlocks=" + tickableBlocks +
				", blocks=" + Arrays.toString(blocks) +
				", chunkX=" + chunkX +
				", chunkY=" + chunkY +
				", world=" + world +
				'}';
	}
}
