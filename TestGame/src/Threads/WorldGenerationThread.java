package Threads;

import Blocks.BlockAir;
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

		MainFile.currentWorld.generating = true;

		for (WorldGenPriority priority : WorldGenPriority.values()) {
			for (StructureGeneration gen : Registrations.structureGenerations) {
				if (gen.generationPriority().equals(priority)) {
					if (gen.canGenerate(MainFile.currentWorld)) {
						WorldGenerationScreen.generationStatus = priority.name() + "-|-" + gen.getGenerationName();
						gen.generate(MainFile.currentWorld);
					}
				}
			}

			for (GenerationBase gen : Registrations.generationBases) {
				if (gen.generationPriority().equals(priority)) {

					for (int x = 0; x < MainFile.currentWorld.worldSize.xSize; x++) {
						for (int y = MainFile.currentWorld.worldSize.ySize - 1; y > 0; y--) {

							if (gen.canGenerate(MainFile.currentWorld, x, y)) {
								WorldGenerationScreen.generationStatus = priority.name() + "-|-" + gen.getGenerationName();
								gen.generate(MainFile.currentWorld, x, y);
							}
						}
					}
				}
			}

		}

		WorldGenerationScreen.generationStatus = "Air blocks.";

		for (int x = 0; x < MainFile.currentWorld.worldSize.xSize; x++) {
			for (int y = 0; y < MainFile.currentWorld.worldSize.ySize; y++) {
				if (MainFile.currentWorld.getBlock(x, y) == null) {
					MainFile.currentWorld.setBlock(new BlockAir(), x, y);
				}
			}
		}
		WorldGenerationScreen.generationStatus = "";

		MainFile.currentWorld.doneGenerating();
		MainFile.currentWorld.generating = false;
	}
}
