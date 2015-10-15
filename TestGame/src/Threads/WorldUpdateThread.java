package Threads;

import EntityFiles.Entity;
import Main.MainFile;
import WorldFiles.EnumWorldTime;


public class WorldUpdateThread extends Thread {

	public WorldUpdateThread(){setName("WorldUpdateThread");}

	//TODO Time updating too fast?
	public void run() {
		while (true) {
			try {
				for (EnumWorldTime en : EnumWorldTime.values()) {
					if (MainFile.currentWorld.WorldTime > en.timeBegin && MainFile.currentWorld.WorldTime < en.timeEnd) {
						MainFile.currentWorld.worldTimeOfDay = en;
					}
				}
				MainFile.currentWorld.WorldTime += 1;

				if (MainFile.currentWorld.WorldTime > MainFile.currentWorld.WorldTimeDayEnd) {
					MainFile.currentWorld.WorldTime = 0;
					MainFile.currentWorld.WorldDay += 1;
				}

				MainFile.currentWorld.updateBlocks();

				for(Entity ent : MainFile.currentWorld.Entities){
					ent.updateEntity();
				}

				try {
					sleep(1000);
				}catch (Exception e){

				}


			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
