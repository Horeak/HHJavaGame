package Utils;

import WorldGeneration.*;
import WorldGeneration.Dungeons.*;
import WorldGeneration.Structures.CaveGeneration;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.StructureGeneration;

import java.util.ArrayList;
import java.util.logging.Level;


public class Registrations {

	public static final ArrayList<GenerationBase> generationBases = new ArrayList<GenerationBase>();
	public static final ArrayList<StructureGeneration> StructureGenerations = new ArrayList<StructureGeneration>();

	public static void registerGenerations() {
		generationBases.add(new TreeGeneration());

		generationBases.add(new CoalOreGeneration());
		generationBases.add(new IronOreGeneration());
		generationBases.add(new SilverOreGeneration());
		generationBases.add(new GoldOreGeneration());

		StructureGenerations.add(new CaveGeneration());

		StructureGenerations.add(new Dungeon1Genertion());
		StructureGenerations.add(new Dungeon2Genertion());
		StructureGenerations.add(new Dungeon3Genertion());
		StructureGenerations.add(new Dungeon4Genertion());
		StructureGenerations.add(new Dungeon5Genertion());

		LoggerUtil.out.log(Level.INFO, "World generations registered.");
	}
}
