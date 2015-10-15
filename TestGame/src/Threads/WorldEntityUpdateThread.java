package Threads;

import EntityFiles.Entity;
import Main.MainFile;

public class WorldEntityUpdateThread extends Thread {

	public WorldEntityUpdateThread(){setName("WorldEntityUpdateThread");}

	public void run() {
		while (true) {
			try {
				for(Entity ent : MainFile.currentWorld.Entities){
					ent.updateEntity();
				}

				try {
					sleep(100);
				}catch (Exception e){

				}

			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}

