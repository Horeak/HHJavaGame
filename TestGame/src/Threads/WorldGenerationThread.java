package Threads;

import Main.MainFile;
import Utils.Registrations;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.StructureGeneration;
import WorldGeneration.Util.WorldGenPriority;


public class WorldGenerationThread extends Thread {

	public WorldGenerationThread() {
		setName("WorldGenerationThread");
	}

	public void run() {

		MainFile.currentWorld.generating = true;

		for (WorldGenPriority priority : WorldGenPriority.values()) {
			for (GenerationBase gen : Registrations.generationBases) {
				if (gen.generationPriority().equals(priority)) {

					for (int x = 0; x < MainFile.currentWorld.worldSize.xSize; x++) {
						for (int y = MainFile.currentWorld.worldSize.ySize - 1; y > 0; y--) {

							if (gen.canGenerate(x, y)) {
								MainFile.currentWorld.generationStatus = priority.name() + "-|-" + gen.getGenerationName();
								gen.generate(x, y);
							}
						}
					}
				}
			}
			for (StructureGeneration gen : Registrations.structureGenerations) {
				if (gen.generationPriority().equals(priority)) {
					if (gen.canGenerate()) {
						MainFile.currentWorld.generationStatus = priority.name() + "-|-" + gen.getGenerationName();
						gen.generate();
					}
				}
			}
		}

		MainFile.currentWorld.generating = false;
	}
}
