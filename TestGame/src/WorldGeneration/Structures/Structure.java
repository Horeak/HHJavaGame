package WorldGeneration.Structures;

import BlockFiles.Util.Block;
import WorldFiles.Chunk;
import WorldFiles.World;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;

public class Structure implements Serializable{
	public World world;
	public String name;

	public HashMap<Point, Block> blocks = new HashMap<>();

	public Structure(World world, String structureName){
		this.world = world;
		this.name = structureName;
	}

	public void setBlock(Block block, int x, int y, boolean addToStructure){
		if(addToStructure){
			blocks.put(new Point(x, y), block);
		}

		world.setBlock(block, x, y);
	}

	public void setBlock(Block block, int x, int y){
		setBlock(block, x, y, true);
	}

	//TODO Is structure not being set or is it being reset?
	public void finishGen( Chunk chunk){
		finishGen(chunk.chunkX * Chunk.chunkSize, chunk.chunkY * Chunk.chunkSize);
	}

	public void finishGen(int x, int y){
		world.setStucture(this, x, y);

		if(world.getChunk(x, y) != null){
			world.getChunk(x, y).setStucture(this);
		}
	}

}
