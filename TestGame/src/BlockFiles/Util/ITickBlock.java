package BlockFiles.Util;


import WorldFiles.World;

public interface ITickBlock {


	default boolean updateOutofBounds() {
		return false;
	}

	boolean shouldupdate(World world, int x, int y);

	//In seconds
	default int blockupdateDelay() {
		return 1;
	}

	default boolean shouldBlockLoadChunk(World world, int x, int y){return false;}

	int getTimeSinceUpdate();
	void setTimeSinceUpdate(int i);

	void updateBlock( World world, int x, int y);
}
