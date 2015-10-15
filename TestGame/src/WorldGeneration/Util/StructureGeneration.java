package WorldGeneration.Util;
/*
* Project: Random Java Creations
* Package: WorldGeneration
* Created: 26.07.2015
*/

public abstract class StructureGeneration
{

	public abstract boolean canGenerate();
	public abstract void generate();

	public abstract String getGenerationName();

	public abstract WorldGenPriority generationPriority();

}
