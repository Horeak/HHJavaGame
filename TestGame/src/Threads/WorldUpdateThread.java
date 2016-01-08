package Threads;

import EntityFiles.Entity;
import Main.MainFile;
import WorldFiles.EnumWorldTime;


public class WorldUpdateThread extends Thread {

	public WorldUpdateThread() {
		setName("WorldUpdateThread");
	}
	public void run() {
		while (true) {

			try {

				if (!MainFile.gameContainer.isPaused()) {

					for (EnumWorldTime en : EnumWorldTime.values()) {
						if (MainFile.getServer().getWorld().WorldTime > en.timeBegin && MainFile.getServer().getWorld().WorldTime < en.timeEnd) {
							MainFile.getServer().getWorld().worldTimeOfDay = en;
						}
					}
					MainFile.getServer().getWorld().WorldTime += 1;

					if (MainFile.getServer().getWorld().WorldTime > MainFile.getServer().getWorld().WorldTimeDayEnd) {
						MainFile.getServer().getWorld().WorldTime = 0;
						MainFile.getServer().getWorld().WorldDay += 1;
					}

					MainFile.getServer().getWorld().updateBlocks();

					for (Entity ent : MainFile.getServer().getWorld().Entities) {
						ent.updateEntity();
					}
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
