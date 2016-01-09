package Threads;

import Main.MainFile;
import Render.Renders.WorldGenerationScreen;
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
		MainFile.getServer().getWorld().generating = true;

		for (WorldGenPriority priority : WorldGenPriority.values()) {
			for (StructureGeneration gen : Registrations.structureGenerations) {
				if (gen.generationPriority().equals(priority)) {
					if(MainFile.getServer().getWorld() != null)
					if (gen.canGenerate(MainFile.getServer().getWorld())) {
						WorldGenerationScreen.generationStatus = priority.name() + "-|-" + gen.getGenerationName();
						gen.generate(MainFile.getServer().getWorld());
					}
				}
			}

			for (GenerationBase gen : Registrations.generationBases) {
				if (gen.generationPriority().equals(priority)) {
					if(MainFile.getServer().getWorld() != null)
					for (int x = 0; x < MainFile.getServer().getWorld().worldSize.xSize; x++) {
						for (int y = 0; y < MainFile.getServer().getWorld().worldSize.ySize; y++) {
							if (gen.canGenerate(MainFile.getServer().getWorld(), x, y)) {
								WorldGenerationScreen.generationStatus = priority.name() + "-|-" + gen.getGenerationName();
								gen.generate(MainFile.getServer().getWorld(), x, y);
							}
						}
					}
				}
			}

		}

		MainFile.getServer().getWorld().doneGenerating();
		MainFile.getServer().getWorld().generating = false;


		} catch (Exception e) {
			e.printStackTrace();
		}

		stop();


	}
}
