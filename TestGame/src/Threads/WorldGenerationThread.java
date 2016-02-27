package Threads;

import Main.MainFile;
import Render.Renders.WorldGenerationScreen;
import Utils.LoggerUtil;
import Utils.Registrations;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;


public class WorldGenerationThread extends Thread {

	public WorldGenerationThread() {
		setName("WorldGenerationThread");
	}

	public void run() {
		try {
		MainFile.game.getServer().getWorld().generating = true;

		for (WorldGenPriority priority : WorldGenPriority.values()) {
			for (StructureGeneration gen : Registrations.structureGenerations) {
				if (gen.generationPriority().equals(priority)) {
					if(MainFile.game.getServer().getWorld() != null)
					if (gen.canGenerate(MainFile.game.getServer().getWorld())) {
						WorldGenerationScreen.generationStatus = priority.name() + "-|-" + gen.getGenerationName();
						gen.generate(MainFile.game.getServer().getWorld());
					}
				}
			}

			for (GenerationBase gen : Registrations.generationBases) {
				if (gen.generationPriority().equals(priority)) {
					if(MainFile.game.getServer().getWorld() != null)
					for (int x = 0; x < MainFile.game.getServer().getWorld().worldSize.xSize; x++) {
						for (int y = 0; y < MainFile.game.getServer().getWorld().worldSize.ySize; y++) {
							if (gen.canGenerate(MainFile.game.getServer().getWorld(), x, y)) {
								WorldGenerationScreen.generationStatus = priority.name() + "-|-" + gen.getGenerationName();
								gen.generate(MainFile.game.getServer().getWorld(), x, y);
							}
						}
					}
				}
			}

		}

		MainFile.game.getServer().getWorld().doneGenerating();
		MainFile.game.getServer().getWorld().generating = false;


		} catch (Exception e) {
			LoggerUtil.exception(e);
		}

		stop();


	}
}
