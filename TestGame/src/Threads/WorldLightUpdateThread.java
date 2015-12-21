package Threads;

import Main.MainFile;

public class WorldLightUpdateThread extends Thread {

	public WorldLightUpdateThread() {
		setName("WorldLightUpdateThread");
	}

	public void run() {
		while (true) {
			try {

				if (!MainFile.gameContainer.isPaused())
				MainFile.currentWorld.updateLightForBlocks();

				try {
					sleep(10);
				} catch (Exception e) {

				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
