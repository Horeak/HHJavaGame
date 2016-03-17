package Threads;

import Main.MainFile;
import Utils.LoggerUtil;

public class WorldLightUpdateThread extends Thread {

	public WorldLightUpdateThread() {
		setName("WorldLightUpdateThread");
	}

	public void run() {
		while (true) {
			try {

				//TODO Imporve
				if (!MainFile.game.gameContainer.isPaused()) {
					MainFile.game.getServer().getWorld().updateLightForBlocks();
				}

				try {
					sleep(100);
				} catch (Exception e) {

				}


			} catch (Exception e) {
				LoggerUtil.exception(e);
			}
		}
	}
}
