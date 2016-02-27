package Threads;

import EntityFiles.Entity;
import Main.MainFile;
import Utils.LoggerUtil;

public class WorldEntityUpdateThread extends Thread {

	public WorldEntityUpdateThread() {
		setName("WorldEntityUpdateThread");
	}

	public void run() {
		while (true) {
			try {

				if (!MainFile.game.gameContainer.isPaused()) {
					for (Entity ent : MainFile.game.getServer().getWorld().Entities) {
						ent.updateEntity();
					}

					MainFile.game.getServer().getWorld().Entities.removeAll(MainFile.game.getServer().getWorld().RemoveEntities);
					MainFile.game.getServer().getWorld().RemoveEntities.clear();
				}

				try {
					sleep(130);
				} catch (Exception e) {

				}

			} catch (Exception e) {
				LoggerUtil.exception(e);
			}
		}
	}
}

