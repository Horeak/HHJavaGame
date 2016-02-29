package WorldFiles;

import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import BlockFiles.Util.ILightSource;
import BlockFiles.Util.ITickBlock;
import BlockFiles.Util.LightUnit;
import EntityFiles.Entities.EntityPlayer;
import EntityFiles.Entity;
import EntityFiles.EntityItem;
import Items.Utils.ItemStack;
import Main.MainFile;
import Threads.WorldEntityUpdateThread;
import Threads.WorldLightUpdateThread;
import Threads.WorldUpdateThread;
import Utils.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO Make sure there is no code left that is hardcoded to one player
//TODO World is generating holes

//TODO Add biomes. (Could for example make it where it gets a random biome for a random range so for example: forest in chunks 1 to 5 and snow from 5 to 7 and desert from 7 to 10)
//TODO Remove world limit and instead generate based on a seed

public class World {
	public WorldUpdateThread worldUpdateThread = new WorldUpdateThread();
	public WorldEntityUpdateThread worldEntityUpdateThread = new WorldEntityUpdateThread();
	public WorldLightUpdateThread worldLightUpdateThread = new WorldLightUpdateThread();

	public HashMap<String, Object> worldProperties = new HashMap<>();

	public ArrayList<Entity> Entities = new ArrayList<>();
	public ArrayList<Entity> RemoveEntities = new ArrayList<>();

	//TODO Add Biomes
	//TODO Store a heightMap in the biome with the ground height for each x-value in the chunks which is generated when the biome is generated
	public HashMap<Point, Biome> biomes = new HashMap<>();

	//TODO Save generated
	public HashMap<Point, Chunk> worldChunks;
	public ArrayList<String> generatedChunks = new ArrayList<>();

	public String worldName;
	public EnumWorldTime worldTimeOfDay = EnumWorldTime.DAY;

	public long worldSeed = MainFile.random.nextLong();

	public int WorldTime = worldTimeOfDay.timeBegin, WorldTimeDayEnd = EnumWorldTime.NIGHT.timeEnd;
	public int WorldDay = 1;

	public long timePlayed;

	public boolean generating = false;
	public boolean loaded = false;
	public boolean isLive = false;
	public boolean loading = false;

	public World( String name) {
		while(FileUtil.isThereWorldWithName(name)){
			name += "-";
		}

		this.worldName = name;
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
		if(!loaded) {
			MainFile.game.getClient().setPlayer(new EntityPlayer(0, 0, MainFile.game.getClient().playerId));
		}else{
			loadPlayer();
		}

		worldUpdateThread = new WorldUpdateThread();
		worldUpdateThread.start();


		TimeTaker.startTimeTaker("worldTimePlayed:" + worldName);
		isLive = true;
	}

	public void resetValues() {
		worldChunks = new HashMap<>();
		worldTimeOfDay = EnumWorldTime.MORNING;
		WorldTime = worldTimeOfDay.timeBegin;
		WorldDay = 1;
	}

	public void generate() {
		if(!loaded) {
			saveWorld();

		}

		worldEntityUpdateThread = new WorldEntityUpdateThread();
		worldLightUpdateThread = new WorldLightUpdateThread();

		worldEntityUpdateThread.start();
		worldLightUpdateThread.start();
	}
	public void doneGenerating() {
		if(!loaded) {
			spawnPlayer(MainFile.game.getClient().getPlayer());
		}else{
			loadPlayer();
		}
	}

	public void stop() {
		saveWorld();
		MainFile.game.getClient().setPlayer(null);

		worldUpdateThread.stop();
		worldEntityUpdateThread.stop();
		worldLightUpdateThread.stop();

		worldChunks.clear();
	}

	//TODO Add auto saving (Save the world every 5 min or so)
	public void saveWorld(){
		DataHandler handlerSets = MainFile.game.saveUtil.getDataHandler("saves/" + worldName + "/world.data");

		handlerSets.setObject("worldSeed", worldSeed);
		handlerSets.setObject("worldTimeOfDay", worldTimeOfDay);
		handlerSets.setObject("worldTime", WorldTime);
		handlerSets.setObject("dayNumber", WorldDay);
		handlerSets.setObject("timeStart", TimeTaker.getTime("worldTimePlayed:" + worldName));

		DataHandler handlerProperties = MainFile.game.saveUtil.getDataHandler("saves/" + worldName + "/worldProperties.data");
		handlerProperties.setObject("properties", worldProperties);


		for(String t : generatedChunks){
			String[] g = t.split("\\|");

			int x = Integer.parseInt(g[0]);
			int y = Integer.parseInt(g[1]);

			loadChunk(x, y);
			MainFile.game.saveUtil.saveObjectFile(worldChunks.get(new Point(x, y)), "saves/" + worldName + "/chunks/" + "chunk_" + x + "_" + y + ".data");
		}
		MainFile.game.saveUtil.saveObjectFile(Entities, "saves/" + worldName + "/worldEntities.data");
	}

	public void loadPlayer(){
		//TODO Make it where if it cant find the player in the world just add the one that was created before checking
		for(Entity ent : Entities){
			if(ent instanceof EntityPlayer){
				EntityPlayer pl = (EntityPlayer)ent;

				if(pl.name.equalsIgnoreCase(MainFile.game.getClient().playerId)){
					MainFile.game.getClient().setPlayer(pl);
					return;
				}
			}
		}

		//If player loading failed add created player
		if(MainFile.game.getClient().getPlayer() != null){
			Entities.add(MainFile.game.getClient().getPlayer());
		}
	}

	//TODO Blocks are not being loaded!
	public void loadWorld(String name){
		loading = true;

		DataHandler handlerSets = MainFile.game.saveUtil.getDataHandler("saves/" + name + "/world.data");
		worldName = name;
		worldSeed = handlerSets.getLong("worldSeed");
		WorldTime = handlerSets.getInteger("worldTime");
		WorldDay = handlerSets.getInteger("dayNumber");
		timePlayed = handlerSets.getLong("timeStart");
		TimeTaker.startTimeTaker("worldTimePlayed:" + worldName, System.currentTimeMillis() - timePlayed);

		String tt = handlerSets.getString("worldTimeOfDay");
		for(EnumWorldTime ee : EnumWorldTime.values()) {
			if (ee.name().equals(tt)) {
				worldTimeOfDay = ee;
				break;
			}
		}

		DataHandler handlerProperties = MainFile.game.saveUtil.getDataHandler("saves/" + worldName + "/worldProperties.data");
		worldProperties = (HashMap<String, Object>)handlerProperties.getObject("properties");
		Entities = (ArrayList<Entity>)MainFile.game.saveUtil.loadObjectFile("saves/" + worldName + "/worldEntities.data");
		loadPlayer();


		loaded = true;
		loading = false;
	}

	public void loadChunksNear(int xx, int yy){
		for(int x = -1; x < 2; x++){
			for(int y = -1; y < 2; y++){
				loadChunk((xx / 16) + (x), (yy / 16) + (y));
			}
		}
	}

	public void unloadChunk(int chunkX, int chunkY){
		if(worldChunks.containsKey(new Point(chunkX, chunkY))){
			MainFile.game.saveUtil.saveObjectFile(worldChunks.get(new Point(chunkX, chunkY)), "saves/" + worldName + "/chunks/" + "chunk_" + chunkX + "_" + chunkY + ".data");
			worldChunks.remove(new Point(chunkX, chunkY));
		}
	}

	public boolean isChunkLoaded(int chunkX, int chunkY){
		return worldChunks.containsKey(new Point(chunkX, chunkY));
	}


	public void loadChunk(int chunkX, int chunkY){
		if(!isChunkLoaded(chunkX, chunkY)){
			Chunk chunk = (Chunk)MainFile.game.saveUtil.loadObjectFile("saves/" + worldName + "/chunks/" + "chunk_" + chunkX + "_" + chunkY + ".data");

			if(chunk != null){
				worldChunks.put(new Point(chunkX, chunkY), chunk);
			}else{
				createChunk(chunkX, chunkY);
			}
		}

		if(getChunk_(chunkX, chunkY) != null) {
			getChunk_(chunkX, chunkY).world = this;
		}
	}

	public Biome getBiome(int x){
		if(biomes != null && biomes.size() > 0) {
			for (Map.Entry<Point, Biome> ent : biomes.entrySet()) {
				if (x >= (ent.getKey().x * Chunk.chunkSize) && x <= (ent.getKey().y * Chunk.chunkSize)) {
					return ent.getValue();
				}
			}
		}
		return null;
	}

	public void createChunk(int chunkX, int chunkY){
		Chunk chunk = new Chunk(this, chunkX, chunkY);
		chunk.world = this;

		chunk.generateChunk();

		Biome biome = Biome.getInstanceOf("plainsBiome");

		if(getBiome(chunkX) == null && getBiome(chunkX + 1) == null){
			biomes.put(new Point(chunkX, chunkX + MainFile.random.nextInt(10)), biome);

		}else if(getBiome(chunkX) == null && getBiome(chunkX - 1) == null){
			biomes.put(new Point(chunkX - MainFile.random.nextInt(10), chunkX), biome);
		}

		generatedChunks.add(chunkX + "|" + chunkY);
		worldChunks.put(new Point(chunkX, chunkY), chunk);
	}


	public void updateTime(){
		for (EnumWorldTime en : EnumWorldTime.values()) {
			if(WorldTime > en.timeBegin){
				worldTimeOfDay = en;
			}
		}

		WorldTime += 1;

		if (WorldTime > WorldTimeDayEnd) {
			WorldTime = 0;
			WorldDay += 1;
		}
	}


	public void deleteChunk(int chunkX, int chunkY){
		worldChunks.put(new Point(chunkX, chunkY), null);
	}

	public Chunk getChunk_(int chunkX, int chunkY){
		if(!isChunkLoaded(chunkX, chunkY)) {
			if(Chunk.shouldRangeLoad(chunkX, chunkY)) {
				loadChunk(chunkX, chunkY);
			}
		}

	return worldChunks.get(new Point(chunkX, chunkY));
	}

	public Chunk getChunk(int x, int y){
		return getChunk_(x / Chunk.chunkSize, y / Chunk.chunkSize);
	}


	public Block getBlock( int x, int y ) {
		return getBlock(x, y, false);
	}

	public Block getBlock( int x, int y, boolean allowAir ) {
		try {
			if (getChunk(x, y) == null) {
				return allowAir ? Blocks.blockAir : null;
			}

			Block b = getChunk(x, y).getBlock(x, y, allowAir);
			return b != null ? b : null;

		}catch (Exception e){
			LoggerUtil.exception(e);
		}

		return null;
	}

	public void setBlock( Block block, int x, int y ) {
		try {
			if (getChunk(x, y) != null) {
				getChunk(x, y).setBlock(block, x, y);
			}
		}catch (Exception e){
			LoggerUtil.exception(e);
		}
	}

	public void removeTickBlock(int x, int y){
		if(getChunk(x, y) != null)
		getChunk(x, y).removeTickBlock(x, y);
	}


	public void breakBlock(int x, int y){
		if(getBlock(x, y) != null){
			ItemStack stack = getBlock(x, y).getItemDropped(this, x, y);

			if(stack != null){
				EntityItem item = new EntityItem(x, y, stack);
				Entities.add(item);
			}

			setBlock(null, x, y);
		}
	}

	public void spawnPlayer(EntityPlayer player) {
		int xx = 0, yy = -1;

		//TODO Re add
//		for (int y = -Chunk.chunkSize; y < 0; y++) {
//			Block block = getBlock(xx, y);
//
//			if (block != null) {
//				yy = y - 1;
//				break;
//			}
//		}

		player.setEntityPosition(xx, yy);

		if(!Entities.contains(player))
		Entities.add(player);

		MainFile.game.getClient().hasSpawnedPlayer = true;
	}

	public synchronized void updateBlocks() {
		try {
			if(worldChunks != null) {
				for(Map.Entry<Point, Chunk> ent : new HashMap<Point, Chunk>(worldChunks).entrySet()) {

					if(ent.getValue() == null || ent.getValue().tickableBlocks == null)
						continue;

					//TODO ConcurrentModificationError
					for (Point p : new ArrayList<Point>(ent.getValue().tickableBlocks)) {
						Block block = getBlock(p.x, p.y);

						if (block != null) {
							if (block instanceof ITickBlock) {
								ITickBlock up = (ITickBlock) block;

								int x = p.x, y = p.y;

								if (MainFile.game.getClient().getPlayer().getEntityPostion().distance(x, y) <= (ConfigValues.renderDistance * 2) || up.updateOutofBounds()) {
									if (up.shouldupdate(this, x, y)) {
										if (up.getTimeSinceUpdate() == up.blockupdateDelay()) {
											up.updateBlock(this, x, y);
											up.setTimeSinceUpdate(0);
										} else {
											up.setTimeSinceUpdate(up.getTimeSinceUpdate() + 1);
										}
									}
								}
							}
						}
					}
				}
			}
		}catch (Exception e){
			LoggerUtil.exception(e);
		}
	}


	public void updateNearbyBlocks(int xx, int yy ) {
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (x != 0 && y != 0)
					continue;

				int xPos = xx + x, yPos = yy + y;
				Block b = getBlock(xPos, yPos, true);

				if (b != null) {
					if (xPos != xx || yPos != yy) {
						b.updateBlock(this, xx, yy, xPos, yPos);
					}
				}
			}
		}
	}

	public Block[] getNearbyBlocks( int xx, int yy ) {
		Block[] bl = new Block[ 4 ];

		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (x != 0 && y != 0) continue;

				int xPos = xx + x, yPos = yy + y;
				Block b = getBlock(xPos, yPos, true);

				if (b != null) {
					if (xPos != xx || yPos != yy) {
						bl[ (x + 1) + (y + 1) ] = b;
					}
				}
			}
		}

		return bl;
	}
	
	public LightUnit getLightUnit( int x, int y){
		if(getChunk(x, y) != null) {
			return getChunk(x, y).getLightUnit(x, y);
		}
		
		return new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0);
	}

	public void updateLightForBlock( int xx, int yy ) {
		Block block = getBlock(xx, yy, true);

		if (block != null) {
			getLightUnit(xx,yy).setLightValue(0);
			getLightUnit(xx,yy).setLightColor(ILightSource.DEFAULT_LIGHT_COLOR);

			if (block instanceof ILightSource) {
				getLightUnit(xx,yy).setLightValue(((ILightSource) block).getOutputStrength());
				getLightUnit(xx,yy).setLightColor(((ILightSource) block).getLightColor());
			}

			boolean hasLight = false;

			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					if (x != 0 && y != 0) {
						continue;
					}

					int xPos = xx + x, yPos = yy + y;
					Block b = getBlock(xPos, yPos, true);

					if (b != null) {

						if (b.getLightValue(this, xPos, yPos) > 0) {
							hasLight = true;
						}

						if (block.getLightValue(this, xx, yy) < b.getLightValue(this, xPos, yPos)) {
							getLightUnit(xx,yy).setLightValue(b.getLightValue(this, xPos, yPos) - 1F);
							if (getLightUnit(xx,yy).getLightColor() != getLightUnit(xx,yy).getLightColor()) {
								getLightUnit(xx,yy).setLightColor(getLightUnit(xx,yy).getLightColor());
							}

						}
					}
				}
			}

			if (!hasLight && !(block instanceof ILightSource)) {
				getLightUnit(xx,yy).setLightValue(0);
				getLightUnit(xx,yy).setLightColor(ILightSource.DEFAULT_LIGHT_COLOR);
			}

		}
	}

	public void updateLightForBlocks() {
		for(Chunk chunk : new HashMap<Point, Chunk>(worldChunks).values()){
			if(chunk == null || chunk != null && chunk.shouldBeLoaded()) continue;

			if(!chunk.shouldBeLoaded() && isChunkLoaded(chunk.chunkX, chunk.chunkY)){
				unloadChunk(chunk.chunkX, chunk.chunkY);
			}
		}

		if(MainFile.game.getClient().getPlayer() != null) {
			loadChunksNear((int) MainFile.game.getClient().getPlayer().getEntityPostion().x, (int) MainFile.game.getClient().getPlayer().getEntityPostion().y);
		}


		updateLightForBlocks(MainFile.game.getClient().getPlayer() != null);
	}

	public void updateLightForBlocks(Boolean t ) {
		if (t) {
			for (int x = -(ConfigValues.lightUpdateRenderRange / 2); x < (ConfigValues.lightUpdateRenderRange / 2); x++) {
				for (int y = -(ConfigValues.lightUpdateRenderRange / 2); y < (ConfigValues.lightUpdateRenderRange / 2); y++) {

					int xPos = (int) MainFile.game.getClient().getPlayer().getEntityPostion().x + x, yPos = (int) MainFile.game.getClient().getPlayer().getEntityPostion().y + y;
					Block b = getBlock(xPos, yPos, true);

					if (b != null) {
						updateLightForBlock(xPos, yPos);
						b.updateBlock(this, xPos, yPos, xPos, yPos);
					}
				}
			}

		} else {
			for(Chunk chunk : new ArrayList<Chunk>(worldChunks.values())){
				for(int x = 0; x < Chunk.chunkSize; x++){
					for(int y = 0; y < Chunk.chunkSize; y++){
						Block b = chunk.getBlock_(x, y);
						if(b != null){
							updateLightForBlock(x + (chunk.chunkX * Chunk.chunkSize), y + (chunk.chunkY * Chunk.chunkSize));
						}
					}
				}
			}

		}
	}


	@Override
	public boolean equals( Object o ) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof World)) {
			return false;
		}

		World world = (World) o;

		if (WorldTime != world.WorldTime) {
			return false;
		}
		if (WorldTimeDayEnd != world.WorldTimeDayEnd) {
			return false;
		}
		if (WorldDay != world.WorldDay) {
			return false;
		}
		if (!worldName.equals(world.worldName)) {
			return false;
		}
		if (worldProperties != null ? !worldProperties.equals(world.worldProperties) : world.worldProperties != null) {
			return false;
		}
		return worldTimeOfDay == world.worldTimeOfDay;

	}

	//TODO Getting tired of this... Still takes time when game is paused... Maybe is should just change to a tick system
	public String getTimePlayed(){
		if(isLive && !MainFile.game.gameContainer.isPaused()) {
			timePlayed = System.currentTimeMillis() - TimeTaker.getStartTime("worldTimePlayed:" + worldName);
		}
		String t = TimeTaker.getText("worldTimePlayed:" + worldName, 0,(timePlayed), "<days><hours><mins><secs>", false);

		return t;
	}

	@Override
	public int hashCode() {
		int result = worldName.hashCode();
		result = 31 * result + (worldProperties != null ? worldProperties.hashCode() : 0);
		result = 31 * result + (Entities != null ? Entities.hashCode() : 0);
		result = 31 * result + WorldTime;
		result = 31 * result + WorldTimeDayEnd;
		result = 31 * result + (worldTimeOfDay != null ? worldTimeOfDay.hashCode() : 0);
		result = 31 * result + WorldDay;
		result = 31 * result + (generating ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		String t = getTimePlayed();

		return "World{" +
				"worldName='" + worldName + '\'' +
				", entities=" + (Entities != null ? Entities.size() : 0) +
				", timePlayed= " + (t != null && t.length() > 0  ? t.substring(0, t.length()-1) : "") +
				", properties=" + worldProperties +
				", loaded=" + loaded +
				", generating=" + generating +
				", WorldDay=" + WorldDay +
				", WorldTime=" + WorldTime +
				", worldTimeOfDay=" + worldTimeOfDay +
				'}';
	}
}
