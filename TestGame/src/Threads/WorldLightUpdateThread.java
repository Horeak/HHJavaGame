package Threads;

import Main.MainFile;

public class WorldLightUpdateThread extends Thread {

	public WorldLightUpdateThread() {
		setName("WorldLightUpdateThread");
	}

	public void run() {
		while (true) {
			try {

				if (!MainFile.game.gameContainer.isPaused()) {
					MainFile.game.getServer().getWorld().updateLightForBlocks();
				}

				try {
					sleep(50);
				} catch (Exception e) {

				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}