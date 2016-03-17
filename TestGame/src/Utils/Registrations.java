package Utils;

import WorldGeneration.Structures.CaveGeneration;
import WorldGeneration.CrackedStoneGeneration;
import WorldGeneration.IronOreGeneration;
import WorldGeneration.TreeGeneration;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.StructureGeneration;

import java.util.ArrayList;
import java.util.logging.Level;


public class Registrations {

	public static ArrayList<GenerationBase> generationBases = new ArrayList<GenerationBase>();
	public static ArrayList<StructureGeneration> StructureGenerations = new ArrayList<StructureGeneration>();

	public static void registerGenerations() {
		generationBases.add(new CrackedStoneGeneration());
		generationBases.add(new TreeGeneration());

		generationBases.add(new IronOreGeneration());

		StructureGenerations.add(new CaveGeneration());

		//TODO Add random "dungeons"


		LoggerUtil.out.log(Level.INFO, "World generations registered.");
	}
}
