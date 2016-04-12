package Threads;

import Main.MainFile;
import Utils.LoggerUtil;


public class WorldUpdateThread extends Thread {

	public WorldUpdateThread() {
		setName("WorldUpdateThread");
	}
	public void run() {
		int time = 0;
		while (true) {

			try {

				if (!MainFile.game.gameContainer.isPaused() && !MainFile.game.getServer().getWorld().generating) {
					MainFile.game.getServer().getWorld().updateBlocks();

					if(time >= 1000) {//1000 is to make the time update once each second!
						MainFile.game.getServer().getWorld().updateTime();
						time = 0;
					}else{
						time += 1;
					}

				}

				try {
					sleep(1);
				} catch (Exception e) {

				}



			} catch (Exception e) {
				LoggerUtil.exception(e);
			}
		}
	}
}
