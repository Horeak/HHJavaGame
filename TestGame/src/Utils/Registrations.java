package Utils;

import WorldGeneration.CrackedStoneGeneration;
import WorldGeneration.Structures.AirBlockGeneration;
import WorldGeneration.Structures.GrassGeneration;
import WorldGeneration.Structures.StoneGeneration;
import WorldGeneration.TreeGeneration;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.StructureGeneration;

import java.util.ArrayList;


public class Registrations {

	public static ArrayList<GenerationBase> generationBases = new ArrayList<GenerationBase>();
	public static ArrayList<StructureGeneration> structureGenerations = new ArrayList<StructureGeneration>();

	public static void registerGenerations() {
		structureGenerations.add(new GrassGeneration());
		structureGenerations.add(new StoneGeneration());
		structureGenerations.add(new AirBlockGeneration());

		generationBases.add(new TreeGeneration());
		generationBases.add(new CrackedStoneGeneration());
	}
}
