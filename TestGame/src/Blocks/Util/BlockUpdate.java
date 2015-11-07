package Blocks.Util;


public interface BlockUpdate {

	int timeSinceUpdate = 1;

	default boolean updateOutofBounds() {
		return false;
	}

	boolean shouldupdate();

	//In seconds
	default int blockupdateDelay() {
		return 1;
	}

	default int getTimeSinceUpdate() {
		return timeSinceUpdate;
	}

	void updateBlock();
}
