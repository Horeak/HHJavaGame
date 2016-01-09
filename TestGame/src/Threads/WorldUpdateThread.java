package Threads;

import Main.MainFile;


public class WorldUpdateThread extends Thread {

	public WorldUpdateThread() {
		setName("WorldUpdateThread");
	}
	public void run() {
		while (true) {

			try {

				if (!MainFile.gameContainer.isPaused() && !MainFile.getServer().getWorld().generating) {
					MainFile.getServer().getWorld().updateTime();
					MainFile.getServer().getWorld().updateBlocks();
				}

				try {
					sleep(1000);
				} catch (Exception e) {

				}



			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
