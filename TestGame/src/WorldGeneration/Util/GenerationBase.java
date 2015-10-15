package WorldGeneration.Util;
/*
* Project: Random Java Creations
* Package: WorldGeneration
* Created: 26.07.2015
*/

public abstract class GenerationBase
{

	//TODO Make world generation have world instance to allow world properties for world gen
	public abstract boolean canGenerate(int x, int y);
	public abstract void generate(int x, int y);

	public abstract String getGenerationName();

	public abstract WorldGenPriority generationPriority();

}
