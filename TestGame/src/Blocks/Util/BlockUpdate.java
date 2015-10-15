package Blocks.Util;


public interface BlockUpdate {

	default boolean updateOutofBounds(){return false; }
	boolean shouldupdate();

	//In seconds
	default int blockupdateDelay(){return 1;}
	int timeSinceUpdate = 1;

	default int getTimeSinceUpdate(){return timeSinceUpdate;}

	void updateBlock();
}
