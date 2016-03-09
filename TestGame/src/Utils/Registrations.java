package Utils;

import WorldGeneration.CrackedStoneGeneration;
import WorldGeneration.TreeGeneration;
import WorldGeneration.Util.GenerationBase;

import java.util.ArrayList;
import java.util.logging.Level;


public class Registrations {

	public static ArrayList<GenerationBase> generationBases = new ArrayList<GenerationBase>();
	public static void registerGenerations() {
		generationBases.add(new TreeGeneration());
		generationBases.add(new CrackedStoneGeneration());


		LoggerUtil.out.log(Level.INFO, "World generations registered.");
	}
}
