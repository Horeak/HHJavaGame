package WorldGeneration.Structures;

import BlockFiles.Util.Block;
import WorldFiles.Chunk;

import java.awt.*;
import java.io.Serializable;

public class ChunkStructure extends Structure implements Serializable{
	public Chunk ch;

	//ChunkStructure is used if something is generated withing a chunk and only uses that one chunk
	public ChunkStructure( Chunk chunk, String structureName ){
		super(chunk.world, structureName);
		this.ch = chunk;
	}

	public void setBlock(Block block, int x, int y, boolean addToStructure){
		if(addToStructure){
			blocks.put(new Point(x, y), block);
		}

		ch.setBlock(block, x, y);
	}

	public void setBlock(Block block, int x, int y){
		setBlock(block, x, y, true);
	}


	public Block getBlock(int worldX, int worldY){
		return blocks.get(new Point(worldX - (ch.chunkX * Chunk.chunkSize), worldY - (ch.chunkY * Chunk.chunkSize)));
	}

}
