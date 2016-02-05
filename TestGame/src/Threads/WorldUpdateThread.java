package Threads;

import Main.MainFile;


public class WorldUpdateThread extends Thread {

	public WorldUpdateThread() {
		setName("WorldUpdateThread");
	}
	public void run() {
		while (true) {

			try {

				if (!MainFile.game.gameContainer.isPaused() && !MainFile.game.getServer().getWorld().generating) {
					MainFile.game.getServer().getWorld().updateTime();
					MainFile.game.getServer().getWorld().updateBlocks();
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
