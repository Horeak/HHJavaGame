package Threads;

import EntityFiles.Entity;
import Main.MainFile;

public class WorldEntityUpdateThread extends Thread {

	public WorldEntityUpdateThread() {
		setName("WorldEntityUpdateThread");
	}

	public void run() {
		while (true) {
			try {

				if (!MainFile.gameContainer.isPaused()) {
					for (Entity ent : MainFile.getServer().getWorld().Entities) {
						ent.updateEntity();
					}

					MainFile.getServer().getWorld().Entities.removeAll(MainFile.getServer().getWorld().RemoveEntities);
					MainFile.getServer().getWorld().RemoveEntities.clear();
				}

				try {
					sleep(130);
				} catch (Exception e) {

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

