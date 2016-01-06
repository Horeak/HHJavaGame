package Blocks.Util;


public interface ITickBlock {


	default boolean updateOutofBounds() {
		return false;
	}

	boolean shouldupdate();

	//In seconds
	default int blockupdateDelay() {
		return 1;
	}

	int getTimeSinceUpdate();
	void setTimeSinceUpdate(int i);

	void updateBlock();
}
