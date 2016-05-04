package WorldFiles;import BlockFiles.Blocks;import BlockFiles.Util.Block;import BlockFiles.Util.ILightSource;import BlockFiles.Util.ITickBlock;import BlockFiles.Util.LightUnit;import EntityFiles.Entities.EntityItem;import EntityFiles.Entities.EntityPlayer;import EntityFiles.Entity;import Items.Utils.IInventory;import Items.Utils.ItemStack;import Main.MainFile;import Threads.WorldChunkUpdateThread;import Threads.WorldEntityUpdateThread;import Threads.WorldLightUpdateThread;import Threads.WorldUpdateThread;import Utils.*;import WorldGeneration.Structures.Structure;import WorldGeneration.Util.GenerationBase;import WorldGeneration.Util.StructureGeneration;import WorldGeneration.Util.WorldGenPriority;import java.awt.*;import java.lang.reflect.Field;import java.util.ArrayList;import java.util.HashMap;import java.util.Map;import java.util.concurrent.ConcurrentHashMap;import java.util.concurrent.CopyOnWriteArrayList;import java.util.logging.Level;//TODO Make sure there is no code left that is hardcoded to one player//TODO Make sure all chunk loading is possible to be done with multiple players//TODO Add a "teleport" function for players (Will have to load and if needed generate the chunks around the destination right before sending player)public class World {	public WorldUpdateThread worldUpdateThread = new WorldUpdateThread();	public WorldEntityUpdateThread worldEntityUpdateThread = new WorldEntityUpdateThread();	public WorldLightUpdateThread worldLightUpdateThread = new WorldLightUpdateThread();	public WorldChunkUpdateThread worldChunkUpdateThread = new WorldChunkUpdateThread();	public HashMap<String, Object> worldProperties = new HashMap<>();	//TODO Unload entities when chunks unload (Otherwise items will fall through the ground because chunk below unloaded!)(Is the current way possible? Not updating entities when in an unloaded chunk)	public CopyOnWriteArrayList<Entity> Entities = new CopyOnWriteArrayList<>();	public ConcurrentHashMap<Integer, Biome> biomes = new ConcurrentHashMap<>();	public ConcurrentHashMap<Point, Chunk> worldChunks;	public ConcurrentHashMap<Integer, Integer> heightHashMap = new ConcurrentHashMap<>();	public String worldName;	public EnumWorldTime worldTimeOfDay = EnumWorldTime.DAY;	public long worldSeed = MainFile.random.nextLong();	//TODO World time may be going too slow! (Make an cycle 30min long...)	public int WorldTime = worldTimeOfDay.timeBegin, WorldTimeDayEnd = EnumWorldTime.NIGHT.timeEnd;	public int WorldDay = 1;	public long timePlayed;	public boolean generating = false;	public boolean loaded = false;	public boolean isLive = false;	public boolean loading = false;	public boolean generateChunks = true;	public boolean shouldSave = true;	public boolean shouldLoad = true;	public boolean ingnoreLightingHeight = false;	public GameMode gameMode = GameMode.SURVIVAL;	public WorldGenType worldGenType = WorldGenType.WorldGenTypes.NORMAL_WORLD.genType;	//TODO Add world gen type which overwrites gen height like for example flatworld	//TODO Add world specific difficulty?	public World( String name) {		while(FileUtil.isThereWorldWithName(name)){			name += "-";		}		this.worldName = name;		resetValues();	}	public EnumWorldTime getNextWorldTime() {		boolean t = false;		for (EnumWorldTime en : EnumWorldTime.values()) {			if (!t) {				if (WorldTime > en.timeBegin && WorldTime < en.timeEnd) {					if (en == EnumWorldTime.NIGHT) {						return EnumWorldTime.MORNING;					} else {						t = true;					}				}			} else {				return en;			}		}		return EnumWorldTime.MORNING;	}	public void setTimeOfDay( EnumWorldTime time ) {		worldTimeOfDay = time;		WorldTime = time.timeBegin;	}	public void start() {		if(!loaded) {			MainFile.game.getClient().setPlayer(new EntityPlayer(0, 0, MainFile.game.getClient().playerId));			spawnPlayer(MainFile.game.getClient().getPlayer());		}else{			loadPlayer();		}		worldUpdateThread = new WorldUpdateThread();		worldUpdateThread.start();		TimeTaker.startTimeTaker("worldTimePlayed:" + worldName);		isLive = true;	}	public void resetValues() {		worldChunks = new ConcurrentHashMap<>();		worldTimeOfDay = EnumWorldTime.MORNING;		WorldTime = worldTimeOfDay.timeBegin;		WorldDay = 1;	}	public void generate() {		if(!loaded) {			saveWorld();		}		worldEntityUpdateThread = new WorldEntityUpdateThread();		worldLightUpdateThread = new WorldLightUpdateThread();		worldChunkUpdateThread = new WorldChunkUpdateThread();		worldEntityUpdateThread.start();		worldLightUpdateThread.start();		worldChunkUpdateThread.start();	}	public void stop() {		saveWorld();		MainFile.game.getClient().setPlayer(null);		worldUpdateThread.stop();		worldEntityUpdateThread.stop();		worldLightUpdateThread.stop();		worldChunkUpdateThread.stop();		worldUpdateThread = null;		worldEntityUpdateThread = null;		worldLightUpdateThread = null;		worldChunkUpdateThread = null;		worldChunks.clear();	}	public void loadPlayer(){		for(Entity ent : new ArrayList<Entity>(Entities)){			if(ent instanceof EntityPlayer){				EntityPlayer pl = (EntityPlayer)ent;				if(pl.name.equalsIgnoreCase(MainFile.game.getClient().playerId)){					MainFile.game.getClient().setPlayer(pl);					return;				}			}		}		//If player loading failed add created player		if(MainFile.game.getClient().getPlayer() != null){			Entities.add(MainFile.game.getClient().getPlayer());		}	}	//TODO Save/Load world mode!	//TODO Save/Load WorldGenType!	//TODO Add auto saving (Save the world every 5 min or so)	public void saveWorld(){		if(!shouldSave) return;		LoggerUtil.out.log(Level.INFO, "\"" + worldName + "\"" + " Is being saved!");		DataHandler handlerSets = MainFile.game.saveUtil.getDataHandler("saves/" + worldName + "/world.data");		handlerSets.setObject("worldSeed", worldSeed);		handlerSets.setObject("worldTimeOfDay", worldTimeOfDay);		handlerSets.setObject("worldTime", WorldTime);		handlerSets.setObject("dayNumber", WorldDay);		handlerSets.setObject("timeStart", TimeTaker.getTime("worldTimePlayed:" + worldName));		DataHandler handlerProperties = MainFile.game.saveUtil.getDataHandler("saves/" + worldName + "/worldProperties.data");		handlerProperties.setObject("properties", worldProperties);		for(Chunk chunk : worldChunks.values()){			unloadChunk(chunk.chunkX, chunk.chunkY);		}		MainFile.game.saveUtil.saveObjectFile(Entities, "saves/" + worldName + "/worldEntities.data");		MainFile.game.saveUtil.saveObjectFile(biomes, "saves/" + worldName + "/worldBiomes.data");	}	public boolean loadWorld(String name){		if(!shouldLoad) return false;		loading = true;		try {			DataHandler handlerSets = MainFile.game.saveUtil.getDataHandler("saves/" + name + "/world.data");			worldName = name;			worldSeed = handlerSets.getLong("worldSeed");			WorldTime = handlerSets.getInteger("worldTime");			WorldDay = handlerSets.getInteger("dayNumber");			timePlayed = handlerSets.getLong("timeStart");			TimeTaker.startTimeTaker("worldTimePlayed:" + worldName, System.currentTimeMillis() - timePlayed);			String tt = handlerSets.getString("worldTimeOfDay");			for (EnumWorldTime ee : EnumWorldTime.values()) {				if (ee.name().equals(tt)) {					worldTimeOfDay = ee;					break;				}			}			DataHandler handlerProperties = MainFile.game.saveUtil.getDataHandler("saves/" + worldName + "/worldProperties.data");			worldProperties = (HashMap<String, Object>) handlerProperties.getObject("properties");			Entities = (CopyOnWriteArrayList<Entity>) MainFile.game.saveUtil.loadObjectFile("saves/" + worldName + "/worldEntities.data");			biomes = (ConcurrentHashMap<Integer, Biome>) MainFile.game.saveUtil.loadObjectFile("saves/" + worldName + "/worldBiomes.data");			loaded = true;			loading = false;			LoggerUtil.out.log(Level.INFO, "\"" + worldName + "\"" + " Was loaded successfully!");			return true;		}catch (Exception e){			LoggerUtil.exception(e);			return false;		}	}	public Object partialLoad( String file, String key, Field f){		DataHandler handler = MainFile.game.saveUtil.getDataHandler("saves/" + worldName + "/" +  file);		Object ob = handler.getObject(key);		try {			if(f.get(this) instanceof Long) {				f.set(this, ob);			}else if(f.get(this) instanceof Integer){				if(ob instanceof Long){					ob = Integer.parseInt(Long.toString((Long) ob));				}				f.set(this, ob);			}else if(f.get(this) instanceof Double){				if(ob instanceof Long){					ob = Double.parseDouble(Long.toString((Long)ob));				}				f.set(this, ob);			}else if(f.get(this) instanceof Float){				if(ob instanceof Long){					ob = Float.parseFloat(Long.toString((Long)ob));				}				f.set(this, ob);			}else{				f.set(this, ob);			}		} catch (IllegalAccessException e) {			LoggerUtil.exception(e);		}		return ob;	}	public void loadProperties(){		DataHandler handlerProperties = MainFile.game.saveUtil.getDataHandler("saves/" + worldName + "/worldProperties.data");		worldProperties = (HashMap<String, Object>)handlerProperties.getObject("properties");	}	public void loadChunksNear(int xx, int yy){		for(int x = -1; x <= 1; x++){			for(int y = -1; y <= 1; y++){				if(!isChunkLoaded((xx / 16) + (x), (yy / 16) + (y))) {					loadChunk((xx / 16) + (x), (yy / 16) + (y));				}			}		}	}	public void unloadChunk(int chunkX, int chunkY){		if(isChunkLoaded(chunkX, chunkY)){			Chunk chunk = worldChunks.get(new Point(chunkX, chunkY));			chunk.onUnload();			worldChunks.remove(new Point(chunkX, chunkY));			if(shouldSave) {				MainFile.game.saveUtil.saveObjectFile(chunk, "saves/" + worldName + "/chunks/" + "chunk_" + chunkX + "_" + chunkY + ".data");			}		}	}	public boolean isChunkLoaded(int chunkX, int chunkY){		return worldChunks.containsKey(new Point(chunkX, chunkY));	}	public void loadChunk(int chunkX, int chunkY){		if(!isChunkLoaded(chunkX, chunkY)){			if(getBiome(chunkX) == null){				createBiome(chunkX);			}			Chunk chunk = null;			if(shouldLoad){				chunk = (Chunk)MainFile.game.saveUtil.loadObjectFile("saves/" + worldName + "/chunks/" + "chunk_" + chunkX + "_" + chunkY + ".data");			}			if(chunk != null){				chunk.world = this;				chunk.onLoad();				worldChunks.put(new Point(chunkX, chunkY), chunk);			}else{				createChunk(chunkX, chunkY);			}		}else{			LoggerUtil.out.log(Level.WARNING, "Trying to load a chunk which is already loaded!");		}	}	public Biome getBiome(int x){		if(biomes != null && biomes.size() > 0) {			return biomes.get(x);		}		return null;	}	//The minimum length in chunks a biome can be	public static int minBiomeLength = 4;	public synchronized void createBiome(int chunkX){		if(biomes.contains(chunkX)) return;		Biome biome = null;		if(biome == null || !worldGenType.shouldGenBiome(biome)) {			for (int i = 0; i < 10; i++) {				ArrayList<String> biomeChance = new ArrayList<>();				for(String b : Biome.biomeIDs){					biomeChance.add(b);				}				//Increases the chance of getting the same biome as the biome next to generated chunk				int increasedBiomeChance = 4;				int length = 0;				String t = null;				if(getBiome(chunkX - 1) != null){					for(int h = 0; h < increasedBiomeChance; h++)						biomeChance.add(getBiome(chunkX - 1).id);					length = getBiome(chunkX - 1).length + 1;					t = getBiome(chunkX - 1).id;				}else if(getBiome(chunkX + 1) != null){					for(int h = 0; h < increasedBiomeChance; h++)						biomeChance.add(getBiome(chunkX + 1).id);					length = getBiome(chunkX + 1).length + 1;					t = getBiome(chunkX + 1).id;				}				if(length != 0 && length <= minBiomeLength && t != null){					biome = Biome.getInstanceOf(t);				}else {					biome = Biome.getInstanceOf(biomeChance.get(MainFile.random.nextInt(biomeChance.size())));				}				//Resets the biome length when the biome isnt the same as last one				if(getBiome(chunkX - 1) != null && biome.id == getBiome(chunkX - 1).id || getBiome(chunkX + 1) != null && biome.id == getBiome(chunkX + 1).id){					biome.length = length;				}				if (worldGenType.shouldGenBiome(biome)) {					break;				} else {					continue;				}			}		}		if(biome != null && worldGenType.shouldGenBiome(biome)){			//TODO Maybe make the overrideBiomeHeight from worldGenType be activated after the biome has generated the height to allow changing the height not creating a new one from the world type?			if(worldGenType.overrideBiomeHeight()){				worldGenType.genWorldHeight(this, biome, chunkX * Chunk.chunkSize);			}else {				biome.generateHeightMap(this, chunkX * Chunk.chunkSize);			}		}else{			return;		}		biomes.put(chunkX, biome);	}	public synchronized Chunk createChunk(int chunkX, int chunkY) {		Chunk chunk = new Chunk(this, chunkX, chunkY);		chunk.world = this;		if (getBiome(chunkX) == null){			createBiome(chunkX);		}		if(generateChunks) {			generateChunk(chunk);		}		worldChunks.put(new Point(chunkX, chunkY), chunk);		return worldChunks.get(new Point(chunkX, chunkY));	}	public void generateChunk(Chunk chunk){		if(chunk.generated || generating) return;		Biome biome = getBiome(chunk.chunkX);		for (WorldGenPriority priority : WorldGenPriority.values()) {			generating = true;			chunk.generated = false;			if(biome != null && biome.worldGens != null && biome.worldGens.length > 0)				for (StructureGeneration gen : biome.worldGens) {					if (gen.generationPriority().equals(priority)) {						if (gen.canGenerate(this, chunk)) {							gen.generate(this, chunk);						}					}				}			for (StructureGeneration gen : Registrations.StructureGenerations) {				if (gen.generationPriority().equals(priority)) {					if (gen.canGenerate(this, chunk)) {						gen.generate(this, chunk);					}				}			}			for (GenerationBase gen : Registrations.generationBases) {				if(gen != null && gen.generationPriority() != null){					if (gen.generationPriority().equals(priority)) {						for (int x = 0; x < Chunk.chunkSize; x++) {							for (int y = 0; y < Chunk.chunkSize; y++) {								if (gen.canGenerate(this, chunk, x, y)) {									gen.generate(this, chunk, x, y);								}							}						}					}				}			}		}		generating = false;		chunk.generated = true;	}	public void updateTime(){		for (EnumWorldTime en : EnumWorldTime.values()) {			if(WorldTime > en.timeBegin){				worldTimeOfDay = en;			}		}		WorldTime += 1;		if (WorldTime > WorldTimeDayEnd) {			WorldTime = 0;			WorldDay += 1;		}	}	public void deleteChunk(int chunkX, int chunkY){		worldChunks.put(new Point(chunkX, chunkY), null);	}	public Chunk getChunk_(int chunkX, int chunkY){		if(!isChunkLoaded(chunkX, chunkY)) {			if(Chunk.shouldRangeLoad(chunkX, chunkY)) { //generating is used for AbstractMainMenu				loadChunk(chunkX, chunkY);			}		}		Chunk chunk = worldChunks.get(new Point(chunkX, chunkY));		if(chunk != null){			if(chunk.world != this){				chunk.world = this;			}		}		return chunk;	}	public static int getChunkX(int x){		return (x + (x < 0 ? -(Chunk.chunkSize - 1) : 0)) / Chunk.chunkSize;	}	public static int getChunkY(int y){		return (y + (y < 0 ? -(Chunk.chunkSize - 1) : 0)) /  Chunk.chunkSize;	}	public Chunk getChunk(int x, int y){		return getChunk_(getChunkX(x), getChunkY(y));	}	public IInventory getInventory(int x, int y){		try {			if (getChunk(x, y) == null) {				return null;			}			Chunk ch = getChunk(x, y);			if(ch == null) return null;			IInventory b = ch.getInventory(x - (ch.chunkX * Chunk.chunkSize), y - (ch.chunkY * Chunk.chunkSize));			return b != null ? b : null;		}catch (Exception e){			LoggerUtil.exception(e);		}		return null;	}	public ITickBlock getTickBlock(int x, int y){		try {			if (getChunk(x, y) == null) {				return null;			}			Chunk ch = getChunk(x, y);			if(ch == null) return null;			ITickBlock b = ch.getTickBlock(x - (ch.chunkX * Chunk.chunkSize), y - (ch.chunkY * Chunk.chunkSize));			return b != null ? b : null;		}catch (Exception e){			LoggerUtil.exception(e);		}		return null;	}	public void setInventory(IInventory inv, int x, int y){		try {			if (getChunk(x, y) == null) {				return;			}			Chunk ch = getChunk(x, y);			if(ch == null) return;			ch.setInventory(inv, x - (ch.chunkX * Chunk.chunkSize), y - (ch.chunkY * Chunk.chunkSize));		}catch (Exception e){			LoggerUtil.exception(e);		}	}	public void setTickBlock(ITickBlock tick, int x, int y){		try {			if (getChunk(x, y) == null) {				return;			}			Chunk ch = getChunk(x, y);			if(ch == null) return;			ch.setTickBlock(tick, x - (ch.chunkX * Chunk.chunkSize), y - (ch.chunkY * Chunk.chunkSize));		}catch (Exception e){			LoggerUtil.exception(e);		}	}	public Structure getStructure( int x, int y){		try {			if (getChunk(x, y) == null) {				return null;			}			Chunk ch = getChunk(x, y);			if(ch == null) return null;			Structure b = ch.getStructure(x - (ch.chunkX * Chunk.chunkSize), y - (ch.chunkY * Chunk.chunkSize));			return b != null ? b : null;		}catch (Exception e){			LoggerUtil.exception(e);		}		return null;	}	public void setStructure(Structure st, int x, int y){		try {			if (getChunk(x, y) == null) {				return;			}			Chunk ch = getChunk(x, y);			if(ch == null) return;			if(st == null){				ch.setStructure(st, x, y);			}else{				ch.setStructure(st);			}		}catch (Exception e){			LoggerUtil.exception(e);		}	}	public Block getBlock( int x, int y ) {		return getBlock(x, y, false);	}	public Block getBlock( int x, int y, boolean allowAir ) {		try {			if (getChunk(x, y) == null) {				return allowAir ? Blocks.blockAir : null;			}			Chunk ch = getChunk(x, y);			if(ch == null) return allowAir ? Blocks.blockAir : null;			Block b = ch.getBlock(x - (ch.chunkX * Chunk.chunkSize), y - (ch.chunkY * Chunk.chunkSize), allowAir);			return b != null ? b : null;		}catch (Exception e){			LoggerUtil.exception(e);		}		return null;	}	//TODO Why is placing blocks lagging?	public void setBlock( Block block, int x, int y ) {		try {			if (getChunk(x, y) != null) {				Chunk ch = getChunk(x, y);				ch.setBlock(block, x - (ch.chunkX * Chunk.chunkSize), y - (ch.chunkY * Chunk.chunkSize));				updateNearbyBlocks(x, y);				if(!generating) {					if(getBlock(x, y, true) != null) {						getBlock(x, y, true).updateBlock(this, x, y, x, y);					}				}			}		}catch (Exception e){			LoggerUtil.exception(e);		}	}	public void removeTickBlock(int x, int y){		if(getChunk(x, y) != null)		getChunk(x, y).removeTickBlock(x, y);	}	public void breakBlock(int x, int y){		if(getBlock(x, y) != null){			if(getInventory(x, y) != null){				IInventory inv = getInventory(x, y);				for(int i = 0; i < inv.getInventorySize(); i++){					ItemStack stack = inv.getItem(i);					spawnItemEntity(stack, x, y);				}			}			if(getBlock(x, y) != null) {				ItemStack stack = getBlock(x, y).getItemDropped(this, x, y);				if (stack != null && stack.getItem() != null)					spawnItemEntity(stack, x, y);			}			setBlock(null, x, y);		}	}	public void spawnItemEntity(ItemStack stack, float x, float y){		if(stack != null){			EntityItem item = new EntityItem(x, y, stack);			spawnEntity(item);		}	}	public void spawnPlayer(EntityPlayer player) {		loadChunk(0, 0);		int xx = 0, yy = (getHeight(0) - 2);		player.setEntityPosition(xx, yy);		player.playerGameMode = gameMode;		spawnEntity(player);		MainFile.game.getClient().hasSpawnedPlayer = true;	}	public void spawnEntity(Entity ent){		if(ent != null && !Entities.contains(ent)) {			Entities.add(ent);		}else{			LoggerUtil.out.log(Level.WARNING, "Tried to spawn entity already in the world!");		}	}	public void despawnEntity(Entity ent){		if(ent != null && Entities.contains(ent)) {			Entities.remove(ent);			//Mark the player as not spawned if the player is despawned			if(ent instanceof EntityPlayer){				if(((EntityPlayer) ent).name == MainFile.game.getClient().playerId){					MainFile.game.getClient().hasSpawnedPlayer = false;				}			}		}else{			LoggerUtil.out.log(Level.WARNING, "Tried to despawn non-exsisting entity!");		}	}	public void updateBlocks() {		try {			if(worldChunks != null) {				for(Chunk chunk : worldChunks.values()) {					if(chunk == null || chunk.tickableBlocks == null)						continue;					for (Map.Entry<Point, ITickBlock> ent : new HashMap<Point, ITickBlock>(chunk.tickableBlocks).entrySet()) {						int x = ent.getKey().x + (chunk.chunkX * Chunk.chunkSize), y = ent.getKey().y + (chunk.chunkY * Chunk.chunkSize);						if (ent.getValue().shouldUpdate(this, x, y)) {							if (ent.getValue().getTimeSinceUpdate(this, x, y) >= (ent.getValue().blockUpdateDelay() * 1000)) { //1000 is used as it is as many times the thread is updated in one second!								ent.getValue().tickBlock(this, x, y);								ent.getValue().setTimeSinceUpdate(this, x, y,0);							} else {								ent.getValue().setTimeSinceUpdate(this, x, y, ent.getValue().getTimeSinceUpdate(this, x, y) + 1);							}						}					}				}			}		}catch (Exception e){			LoggerUtil.exception(e);		}		for(Chunk chunk : worldChunks.values()) {			chunk.update();		}	}	public void updateNearbyBlocks(int xx, int yy ) {		for (int x = -1; x < 2; x++) {			for (int y = -1; y < 2; y++) {				if (x != 0 && y != 0)					continue;				int xPos = xx + x, yPos = yy + y;				Block b = getBlock(xPos, yPos);				if (b != null) {					if (xPos != xx || yPos != yy) {						b.updateBlock(this, xx, yy, xPos, yPos);					}				}			}		}	}	public LightUnit getLightUnit( int x, int y){		if(getChunk(x, y) != null) {			Chunk ch = getChunk(x, y);			if(ch == null) return new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0);			return ch.getLightUnit(x - (ch.chunkX * Chunk.chunkSize), y - (ch.chunkY * Chunk.chunkSize));		}				return new LightUnit(ILightSource.DEFAULT_LIGHT_COLOR, 0);	}	//TODO Is it possible to optimize this? (Decrease the amount of checks it has to do and the amount of times it accesses other blocks and light values)	public void updateLightForBlock( int xx, int yy ) {		Block block = getBlock(xx, yy, true);		LightUnit lu = getLightUnit(xx, yy);		if (block != null && lu != null) {			if (block instanceof ILightSource) {				lu.setLightValue(((ILightSource) block).getOutputStrength());				lu.setLightColor(((ILightSource) block).getLightColor());				return;			}else{				lu.setLightValue(0);				lu.setLightColor(ILightSource.DEFAULT_LIGHT_COLOR);			}			boolean hasLight = false;			for (int x = -1; x < 2; x++) {				for (int y = -1; y < 2; y++) {					if (x != 0 && y != 0) {						continue;					}					if(x == -1 || x == 1){						if(y == -1 || y == 1){							continue;						}					}					int xPos = xx + x, yPos = yy + y;					Block b = getBlock(xPos, yPos, true);					LightUnit lg = getLightUnit(xPos, yPos);					if (b != null && lg != null) {						if (lg.getLightValue() > 0) {							hasLight = true;						}						float bValue = b.getLightValue(this, xPos, yPos);						if (block.getLightValue(this, xx, yy) < bValue) {							lu.setLightValue(bValue - 1F);							if (lu.getLightColor() != lg.getLightColor()) {								lu.setLightColor(lg.getLightColor());							}						}					}				}			}			if (!hasLight && !(block instanceof ILightSource)) {				if(lu != null && (lu.getLightValue() > 0 || lu.getLightColor() != ILightSource.DEFAULT_LIGHT_COLOR)) {					lu.setLightValue(0);					lu.setLightColor(ILightSource.DEFAULT_LIGHT_COLOR);				}			}		}	}	public void updateChunks(){		for(Entity ent : Entities){			if(ent.shouldLoadChunk()){				if(!loading) {					loadChunksNear((int) ent.getEntityPostion().x, (int)ent.getEntityPostion().y);				}			}		}		for(Chunk chunk : worldChunks.values()){			if(chunk == null || chunk != null && chunk.shouldBeLoaded()) continue;			if(!chunk.shouldBeLoaded() && isChunkLoaded(chunk.chunkX, chunk.chunkY)){				unloadChunk(chunk.chunkX, chunk.chunkY);			}else if(chunk != null && !chunk.shouldBeLoaded()){				LoggerUtil.out.log(Level.WARNING, "Chunk shouldnt be loaded but is on active worldChunks list!");			}		}	}	public void updateLightForBlocks() {		//TODO Make it where if light value is under certain value it will not go from block to air (prevent light from sun going through a roof)		//TODO Try to remove this by using block updates or using chunk loading, time change and similar events to update the blocks		for(Chunk chunk : worldChunks.values()){			for(int x = 0; x < Chunk.chunkSize; x++){				for(int y = 0; y < Chunk.chunkSize; y++){					//TODO !!!					if(MainFile.random.nextInt(5) == 1) {						Block b = chunk.getBlock(x, y, true);						if (b != null) {							updateLightForBlock(x + (chunk.chunkX * Chunk.chunkSize), y + (chunk.chunkY * Chunk.chunkSize));						}					}				}			}		}	}	public int getHeight(int x){		return heightHashMap != null && heightHashMap.containsKey(x) ? heightHashMap.get(x) : 0;	}	public boolean containesHeight(int x){return heightHashMap != null && heightHashMap.containsKey(x);}	@Override	public boolean equals( Object o ) {		if (this == o) {			return true;		}		if (!(o instanceof World)) {			return false;		}		World world = (World) o;		if (WorldTime != world.WorldTime) {			return false;		}		if (WorldTimeDayEnd != world.WorldTimeDayEnd) {			return false;		}		if (WorldDay != world.WorldDay) {			return false;		}		if (!worldName.equals(world.worldName)) {			return false;		}		if (worldProperties != null ? !worldProperties.equals(world.worldProperties) : world.worldProperties != null) {			return false;		}		return worldTimeOfDay == world.worldTimeOfDay;	}	//TODO Is this even usefull?	public String getTimePlayed(){		if(isLive && !MainFile.game.gameContainer.isPaused()) {			timePlayed = System.currentTimeMillis() - TimeTaker.getStartTime("worldTimePlayed:" + worldName);		}		String t = TimeTaker.getText("worldTimePlayed:" + worldName, 0,(timePlayed), "<days><hours><mins><secs>", false);		return t;	}	@Override	public int hashCode() {		int result = worldName.hashCode();		result = 31 * result + (worldProperties != null ? worldProperties.hashCode() : 0);		result = 31 * result + (Entities != null ? Entities.hashCode() : 0);		result = 31 * result + WorldTime;		result = 31 * result + WorldTimeDayEnd;		result = 31 * result + (worldTimeOfDay != null ? worldTimeOfDay.hashCode() : 0);		result = 31 * result + WorldDay;		result = 31 * result + (generating ? 1 : 0);		return result;	}	@Override	public String toString() {		String t = getTimePlayed();		return "World{" +				"worldName='" + worldName + '\'' +				", entities=" + (Entities != null ? Entities.size() : 0) +				", timePlayed= " + (t != null && t.length() > 0  ? t.substring(0, t.length()-1) : "") +				", properties=" + worldProperties +				", loaded=" + loaded +				", generating=" + generating +				", WorldDay=" + WorldDay +				", WorldTime=" + WorldTime +				", worldTimeOfDay=" + worldTimeOfDay +				'}';	}}