package WorldFiles;

import Blocks.BlockAir;
import Blocks.Util.Block;
import Blocks.Util.ILightSource;
import Blocks.Util.ITickBlock;
import EntityFiles.Entities.EntityPlayer;
import EntityFiles.Entity;
import Threads.WorldEntityUpdateThread;
import Threads.WorldGenerationThread;
import Threads.WorldLightUpdateThread;
import Threads.WorldUpdateThread;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class World {

	public String worldName;
	public EnumWorldSize worldSize;

	public WorldGenerationThread worldGenerationThread = new WorldGenerationThread();
	public WorldUpdateThread worldUpdateThread = new WorldUpdateThread();
	public WorldEntityUpdateThread worldEntityUpdateThread = new WorldEntityUpdateThread();
	public WorldLightUpdateThread worldLightUpdateThread = new WorldLightUpdateThread();

	public HashMap<String, Object> worldProperties = new HashMap<>();
	public ArrayList<Entity> Entities = new ArrayList<>();
	public EntityPlayer player;
	public Block[][] Blocks;

	public int WorldTime = 0, WorldTimeDayEnd = EnumWorldTime.NIGHT.timeEnd;
	public EnumWorldTime worldTimeOfDay = EnumWorldTime.DAY;
	public int WorldDay = 1;

	public boolean generating = false;
	public String generationStatus = "";

	public World( String name, EnumWorldSize size ) {
		this.worldName = name;
		this.worldSize = size;

		resetValues();
	}

	public EnumWorldTime getNextWorldTime() {
		boolean t = false;
		for (EnumWorldTime en : EnumWorldTime.values()) {
			if (!t) {
				if (WorldTime > en.timeBegin && WorldTime < en.timeEnd) {
					if (en == EnumWorldTime.NIGHT) {
						return EnumWorldTime.MORNING;
					} else {
						t = true;
					}
				}
			} else {
				return en;
			}
		}

		return EnumWorldTime.MORNING;
	}

	public void setTimeOfDay( EnumWorldTime time ) {
		worldTimeOfDay = time;
		WorldTime = time.timeBegin;
	}

	public void start() {
		spawnPlayer();

		worldUpdateThread.start();
	}

	public void resetValues() {
		Blocks = new Block[ worldSize.xSize ][ worldSize.ySize ];

		WorldTime = 0;
		worldTimeOfDay = EnumWorldTime.MORNING;
		WorldDay = 1;
	}

	public void generate() {
		worldGenerationThread.start();
		worldEntityUpdateThread.start();
		worldLightUpdateThread.start();
	}

	public void doneGenerating() {
		spawnPlayer();
	}

	public void stop() {
		worldUpdateThread.stop();
		worldEntityUpdateThread.stop();
		worldLightUpdateThread.stop();
	}

	public boolean isAirBlock( int x, int y ) {
		if (getBlock(x, y, true) instanceof BlockAir) {
			return true;
		}

		return false;
	}


	public Block getBlock( int x, int y ) {
		return getBlock(x, y, false);
	}

	public Block getBlock( int x, int y, boolean allowAir ) {
		if (Blocks != null) {
			if (x >= 0 && y >= 0 && x < worldSize.xSize && y < worldSize.ySize) {
				if (!allowAir && Blocks[ x ][ y ] instanceof BlockAir) {
					return null;
				}

				return Blocks[ x ][ y ];
			}
		}
		return null;
	}

	public void setBlock( Block block, int x, int y ) {
		if (Blocks != null) {

			if (x >= 0 && y >= 0) {
				if (block != null) {
					block.x = x;
					block.y = y;
				}

				if (block != null) block.world = this;

				if (block != null) {
					Blocks[ x ][ y ] = block;
				} else {
					Blocks[ x ][ y ] = new BlockAir();
					Blocks[ x ][ y ].x = x;
					Blocks[ x ][ y ].y = y;
				}

				getBlock(x, y, true).updateBlock(this, x, y);
				updateNearbyBlocks(getBlock(x, y));
			}
		}
	}

	//TODO why doesnt this work set the players position?
	public void spawnPlayer() {
		int xx = 0, yy = 0;
		for (int y = 0; y < worldSize.ySize; y++) {
			int x = new Random().nextInt(worldSize.xSize);

			if (getBlock(x, y) != null && getBlock(x, y).canBlockSeeSky()) {
				xx = x;
				yy = y - 1;
				break;
			}
		}

		if (player == null) {
			player = new EntityPlayer(xx, yy);
			Entities.add(player);
		}
		player.setEntityPosition(xx, yy);
	}

	public void updateBlocks() {
		for (int x = 0; x < worldSize.xSize; x++) {
			for (int y = worldSize.ySize - 1; y > 0; y--) {
				Block block = getBlock(x, y);

				if (block != null) {
					if (block instanceof ITickBlock) {
						Vec2d tempB = new Vec2d(x, y);
						Vec2d tempP = new Vec2d(player.getEntityPostion().x, player.getEntityPostion().y);
						ITickBlock up = (ITickBlock) block;

						if (tempB.distance(tempP) <= (ConfigValues.renderDistance * 2) || up.updateOutofBounds()) {
							if (up.getTimeSinceUpdate() == up.blockupdateDelay()) {
								if (up.shouldupdate()) {
									up.updateBlock();
								}
							}
						}
					}
				}
			}
		}
	}


	public void updateNearbyBlocks( Block block ) {
		for (Block bl : getNearbyBlocks(block)) {

			if (bl != null) {
				bl.updateBlock(block.world, block.x, block.y);
			}
		}
	}

	public Block[] getNearbyBlocks( Block block ) {
		Block[] bl = new Block[ 4 ];

		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {

				if (x != 0 && y != 0) continue;

				if (block != null) {
					Block b = getBlock(block.x + x, block.y + y, true);

					if (b != null) {
						if (b.x != block.x || b.y != block.y) {
							bl[ (x + 1) + (y + 1) ] = b;
						}
					}
				}
			}
		}

		return bl;
	}

	public void updateLightForBlock( Block block ) {
		if (block != null) {
			block.setLightValue(0);
			block.getLightUnit().setLightColor(ILightSource.DEFAULT_LIGHT_COLOR);

			if (block instanceof ILightSource) {
				block.setLightValue(((ILightSource) block).getOutputStrength());
				block.getLightUnit().setLightColor(((ILightSource) block).getLightColor());
			}

			boolean hasLight = false;

			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					if (x != 0 && y != 0) {
						continue;
					}

					Block b = getBlock(block.x + x, block.y + y, true);
					if (b != null) {

						if (b.getLightValue() > 0) {
							hasLight = true;
						}

						if (block.getLightValue() < b.getLightValue()) {
							block.setLightValue(b.getLightValue() - 1);
							if (block.getLightUnit().getLightColor() != b.getLightUnit().getLightColor()) {
								block.getLightUnit().setLightColor(b.getLightUnit().getLightColor());
							}

						}
					}
				}
			}

			if (!hasLight && !(block instanceof ILightSource)) {
				block.setLightValue(0);
				block.getLightUnit().setLightColor(ILightSource.DEFAULT_LIGHT_COLOR);
			}

		}
	}


	//TODO Make it where light can travel through air
	//TODO Maybe try to find a way where it only update blocks nearby?
	public void updateLightForBlocks() {
		if (player != null) {
			for (int x = -(ConfigValues.lightUpdateRenderRange / 2); x < (ConfigValues.lightUpdateRenderRange / 2); x++) {
				for (int y = -(ConfigValues.lightUpdateRenderRange / 2); y < (ConfigValues.lightUpdateRenderRange / 2); y++) {
					Block b = getBlock((int) player.getEntityPostion().x + x, (int) player.getEntityPostion().y + y, true);

					if (b != null) {
						updateLightForBlock(b);
						b.updateBlock(this, b.x, b.y);
					}
				}
			}

		} else {
			for (Block[] bb : Blocks) {
				for (Block block : bb) {
					if (block != null) {
						updateLightForBlock(block);
					}
				}
			}
		}
	}

}
