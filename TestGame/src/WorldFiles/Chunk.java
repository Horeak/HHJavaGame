package WorldFiles;

import BlockFiles.BlockAir;
import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import BlockFiles.Util.ILightSource;
import BlockFiles.Util.ITickBlock;
import BlockFiles.Util.LightUnit;
import Main.MainFile;
import Utils.LoggerUtil;
import Utils.Registrations;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class Chunk implements Serializable{
	public static int chunkSize = 16;

	public ArrayList<Point> tickableBlocks = new ArrayList<>();

	private LightUnit[][] lightUnits = new LightUnit[chunkSize][chunkSize];
	public Block[][] blocks = new Block[chunkSize][chunkSize];

	public int chunkX, chunkY;
	public transient World world;

	public boolean generated = false;

	//TODO Why isnt grass on other chunks then the first one becoming dirt? Make sure TickBlocks work in other chunks!
	public Chunk(World world, int chunkX, int chunkY){
		this.world = world;

		this.chunkX = chunkX;
		this.chunkY = chunkY;

		if(world.getBiome(chunkX * chunkSize) == null) world.createBiome(chunkX);
	}

	public void generateChunk(){
		if(world == null) LoggerUtil.exception(new Exception("ERROR: Null world before generation"));

		for (WorldGenPriority priority : WorldGenPriority.values()) {
			world.generating = true;
			generated = false;

			if(world.getBiome(chunkX * chunkSize) != null)
			for (StructureGeneration gen : world.getBiome(chunkX * chunkSize).worldGens) {
				if (gen.generationPriority().equals(priority)) {
					if(MainFile.game.getServer().getWorld() != null)
						if (gen.canGenerate(this)) {
							gen.generate(this);
						}
				}
			}

			for (GenerationBase gen : Registrations.generationBases) {
				if (gen.generationPriority().equals(priority)) {
					if(MainFile.game.getServer().getWorld() != null)
						for (int x = 0; x < chunkSize; x++) {
							for (int y = 0; y < chunkSize; y++) {
								if (gen.canGenerate(this, x, y)) {
									gen.generate(this, x, y);
								}
							}
						}
				}
			}

		}

		generated = true;
		world.generating = false;
	}


	public Block getBlock( int x, int y ) {
		return getBlock(x, y, false);
	}

	public Block getBlock( int x, int y, boolean allowAir ) {
		if(x < 0) x *= -1;
		if(y < 0) y *= -1;

		if (blocks != null) {
			if (x >= 0 && y >= 0 && x < chunkSize && y < chunkSize) {
				if (!allowAir && blocks[ x ][ y ] instanceof BlockAir) {
					return null;
				}

				return blocks[ x ][ y ];
			}
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


		if(!world.generating) {
			removeTickBlock(wX, wY);
		}

		if (blocks != null) {
			if (xPos >= 0 && yPos >= 0) {
				if (xPos < chunkSize && yPos < chunkSize) {
					blocks[ xPos ][ yPos ] = block;

					if (block != null) {
						if(block instanceof ITickBlock){
							tickableBlocks.add(new Point(xPos,yPos));
						}
					}

					if(!world.generating) {
						if(getBlock(xPos, yPos, true) != null) {
							getBlock(xPos, yPos, true).updateBlock(world, wX, wY, wX, wY);
						}

						world.updateNearbyBlocks(wX, wY);
					}


					lightUnits[xPos][yPos] = new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0);
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

	public LightUnit getLightUnit(int xPos, int yPos){
		if(lightUnits != null){
			if(xPos >= 0 && yPos >= 0){
				if(xPos < chunkSize && yPos < chunkSize){
					return lightUnits[xPos][yPos];
				}
			}
		}

		return new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0F);
	}

	public static boolean shouldRangeLoad(int chunkX, int chunkY){
		return MainFile.game.getClient() != null && MainFile.game.getClient().getPlayer() != null && MainFile.game.getClient().getPlayer().getEntityPostion().distance((chunkX * chunkSize) , (chunkY * chunkSize)) <= (chunkSize * 2.3F);
	}

	public boolean shouldBeLoaded(){
		if(tickableBlocks != null && tickableBlocks.size() > 0)
		for(Point p : new ArrayList<>(tickableBlocks)){
			if(p == null)continue;

			Block b = getBlock(p.x, p.y, false);

			if(b instanceof ITickBlock){
				if(((ITickBlock) b).shouldBlockLoadChunk(world, p.x * chunkSize, p.y * chunkSize)) return true;
			}
		}

		return shouldRangeLoad(chunkX, chunkY);
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
