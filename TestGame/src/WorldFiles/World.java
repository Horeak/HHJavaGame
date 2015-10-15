package WorldFiles;

import Blocks.Util.Block;
import Blocks.Util.BlockUpdate;
import EntityFiles.Entities.EntityPlayer;
import EntityFiles.Entity;
import Threads.WorldEntityUpdateThread;
import Threads.WorldGenerationThread;
import Threads.WorldUpdateThread;
import Utils.ConfigValues;
import com.sun.javafx.geom.Vec2d;

import java.util.ArrayList;
import java.util.HashMap;

public class World {

	public String worldName;
	public EnumWorldSize worldSize;

	public World(String name, EnumWorldSize size){
		this.worldName = name;
		this.worldSize = size;

		resetValues();
	}


	public WorldGenerationThread worldGenerationThread = new WorldGenerationThread();
	public WorldUpdateThread worldUpdateThread = new WorldUpdateThread();
	public WorldEntityUpdateThread worldEntityUpdateThread = new WorldEntityUpdateThread();

	public HashMap<String, Object> worldProperties = new HashMap<>();
	public ArrayList<Entity> Entities = new ArrayList<>();

	public EntityPlayer player;

	public Block[][] Blocks;

	public int WorldTime = 0, WorldTimeDayEnd = 1800;
	public EnumWorldTime worldTimeOfDay = EnumWorldTime.DAY;
	public int WorldDay = 1;

	public boolean generating = false;
	public String generationStatus = "";

	public EnumWorldTime getNextWorldTime(){
		boolean t = false;
		for(EnumWorldTime en : EnumWorldTime.values()){
			if(!t) {
				if (WorldTime > en.timeBegin && WorldTime < en.timeEnd) {
					if (en == EnumWorldTime.NIGHT) {
						return EnumWorldTime.MORNING;
					} else {
						t = true;
					}
				}
			}else{
				return en;
			}
		}

		return EnumWorldTime.MORNING;
	}

	public void start(){
		spawnPlayer();

		worldUpdateThread.start();
	}

	public void resetValues(){
		Blocks = new Block[worldSize.xSize][worldSize.ySize];

		WorldTime = 0;
		worldTimeOfDay = EnumWorldTime.MORNING;
		WorldDay = 1;
	}

	public void generate(){
		worldGenerationThread.start();
		worldEntityUpdateThread.start();
	}


	public Block getBlock(int x, int y){
		if(Blocks != null) {
			if(x >= 0 && y >= 0 && x < worldSize.xSize && y < worldSize.ySize)
			return Blocks[x][y];
		}
		return null;
	}

	public void setBlock(Block block, int x, int y){
		if(Blocks != null) {

			if(x >= 0 && y >= 0) {
				if(block != null) {
					block.x = x;
					block.y = y;
				}

				Blocks[x][y] = block;
			}
		}
	}

	//TODO why doesnt this work set the players position?
	public void spawnPlayer() {
		int xx = 0, yy = 0;
		for(int y = 0; y < worldSize.ySize; y++){
			int x = (worldSize.xSize / 2) + 2;

			boolean t1 = getBlock(x, y) == null, t2 = getBlock(x, y - 1) == null, t3 = getBlock(x, y + 1) != null;

			System.out.println(t1 + " | " + t2 + " | " + t3);

			if(t1 && t2 && t3){
				xx = x;
				yy = y;
				break;
			}
		}

		player = new EntityPlayer(xx,yy);
		Entities.add(player);
	}

	public void updateBlocks(){
		for (int x = 0; x < worldSize.xSize; x++) {
			for (int y = worldSize.ySize - 1; y > 0; y--) {
				Block block = getBlock(x,y);

				if(block != null) {
					if (block instanceof BlockUpdate) {
						Vec2d tempB = new Vec2d(x, y);
						Vec2d tempP = new Vec2d(player.getEntityPostion().x, player.getEntityPostion().y);
						BlockUpdate up = (BlockUpdate) block;

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

}
