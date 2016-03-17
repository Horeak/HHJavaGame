package Threads;

import Main.MainFile;
import Utils.LoggerUtil;

public class WorldChunkUpdateThread extends Thread {

	public WorldChunkUpdateThread() {
		setName("WorldChunkUpdateThread");
	}

	public void run() {
		while (true) {
			try {

				if (!MainFile.game.gameContainer.isPaused()) {
					MainFile.game.getServer().getWorld().updateChunks();
				}

				try {
					//TODO Should this load faster/slower?
					sleep(100);
				} catch (Exception e) {

				}


			} catch (Exception e) {
				LoggerUtil.exception(e);
			}
		}
	}
}
