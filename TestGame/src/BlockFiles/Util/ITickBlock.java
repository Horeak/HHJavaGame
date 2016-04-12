package BlockFiles.Util;


import WorldFiles.World;

import java.io.Serializable;

public interface ITickBlock extends Serializable{

	boolean shouldUpdate( World world, int x, int y);
	default float blockUpdateDelay() {
		return 1;
	}

	default boolean shouldBlockLoadChunk(World world, int x, int y){return false;}

	int getTimeSinceUpdate(World world, int x, int y);
	void setTimeSinceUpdate(World world, int x, int y, int i);

	void tickBlock( World world, int x, int y);


}
