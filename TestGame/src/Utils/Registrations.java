package Utils;

import Render.AbstractWindowRender;
import Render.Renders.*;
import WorldGeneration.GrassGeneration;
import WorldGeneration.StoneGeneration;
import WorldGeneration.TreeGeneration;
import WorldGeneration.Util.GenerationBase;
import WorldGeneration.Util.StructureGeneration;

import java.util.ArrayList;


public class Registrations {

	public static ArrayList<AbstractWindowRender> windowRenders = new ArrayList<>();
	public static ArrayList<GenerationBase> generationBases = new ArrayList<GenerationBase>();
	public static ArrayList<StructureGeneration> structureGenerations = new ArrayList<StructureGeneration>();

	public static void registerWindowRenders() {
		windowRenders.add(new BackgroundRender());

		windowRenders.add(new BlockRendering());
		windowRenders.add(new EntityRendering());

		windowRenders.add(new BlockSelectionRender());
		windowRenders.add(new HotbarRender());

		windowRenders.add(new DebugInfoRender());
		windowRenders.add(new WorldGenerationScreen());
	}

	public static void registerGenerations() {
		structureGenerations.add(new GrassGeneration());
		structureGenerations.add(new StoneGeneration());

		generationBases.add(new TreeGeneration());
	}
}
