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
import Render.Renders.WorldGenerationScreen;
import Utils.LoggerUtil;
import Utils.Registrations;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

//TODO Use chunkMap to debug yellow chunks!
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

	public void generateChunk(){
		if(world == null) LoggerUtil.exception(new Exception("ERROR: Null world before generation"));

		for (WorldGenPriority priority : WorldGenPriority.values()) {
			world.generating = true;

			for (StructureGeneration gen : Registrations.structureGenerations) {
				if (gen.generationPriority().equals(priority)) {
					if(MainFile.game.getServer().getWorld() != null)
						if (gen.canGenerate(this)) {
							WorldGenerationScreen.generationStatus = priority.name() + "-|-" + gen.getGenerationName();
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
									WorldGenerationScreen.generationStatus = priority.name() + "-|-" + gen.getGenerationName();
									gen.generate(this, x, y);
								}
							}
						}
				}
			}

		}

		world.generating = false;
	}

	public Block getBlock( int x, int y ) {
		return getBlock(x, y, false);
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

	public Block getBlock_( int x, int y ) {
		return getBlock_(x, y, false);
	}

	public Block getBlock_( int x, int y, boolean allowAir ) {
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
		return null;
	}

	public void removeTickBlock(int x, int y){
		int Ux = x - (chunkX * chunkSize);
		int Uy = y - (chunkY * chunkSize);

		tickableBlocks.remove(new Point(Ux, Uy));
	}

	public void setBlock_( Block block, int xPos, int yPos ) {
		int wX = xPos + (chunkX * chunkSize), wY = yPos + (chunkY * chunkSize);
		if(xPos < 0) xPos *= -1;
		if(yPos < 0) yPos *= -1;

		if(!world.generating) {
			removeTickBlock(wX, wY);
		}

		if (blocks != null) {
			if (xPos >= 0 && yPos >= 0) {
				if (xPos < chunkSize && yPos < chunkSize) {

					if (block != null) {
						blocks[ xPos ][ yPos ] = block;

						if(block instanceof ITickBlock){
							tickableBlocks.add(new Point(xPos,yPos));
						}

					} else {
						blocks[ xPos ][ yPos ] = Blocks.blockAir;
					}

					if(!world.generating) {
						getBlock(wX, wY, true).updateBlock(world, wX, wY, wX, wY);
						world.updateNearbyBlocks(wX, wY);
					}


					lightUnits[xPos][yPos] = new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0);
				}
			}
		}
	}

	public void setBlock( Block block, int x, int y ) {
		int Ux = x - (chunkX * chunkSize);
		int Uy = y - (chunkY * chunkSize);
		setBlock_(block, Ux, Uy);
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

	public static boolean shouldRangeLoad(int chunkX, int chunkY){
		return MainFile.game.getClient() != null && MainFile.game.getClient().getPlayer() != null && MainFile.game.getClient().getPlayer().getEntityPostion().distance((chunkX * chunkSize) + (chunkSize / 2), (chunkY * chunkSize) + (chunkSize / 2)) <= (chunkSize * 1.75F);
	}

	public boolean shouldBeLoaded(){
		for(Point p : tickableBlocks){
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
