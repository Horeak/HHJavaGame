package WorldGeneration.Structures;

import BlockFiles.Util.Block;
import WorldFiles.World;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;

public class Structure implements Serializable{
	public transient World world;
	public String name;

	public HashMap<Point, Block> blocks = new HashMap<>();

	public Structure(World world, String structureName){
		this.world = world;
		this.name = structureName;
	}

	public org.newdawn.slick.Image getBackgroundImage(){return null;}

	public Block getBlock(int x, int y){
		return blocks.get(new Point(x, y));
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

	//TODO This can be used to for example spawn mobs in dungeons
	public void update(){}

	public boolean shouldRemoveBlock(int x, int y)
	{
		return true;
	}

}
