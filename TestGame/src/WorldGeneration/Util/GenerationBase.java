package WorldGeneration.Util;

import WorldFiles.World;

public abstract class GenerationBase {
	public abstract boolean canGenerate( World world, int x, int y );

	public abstract void generate( World world, int x, int y );

	public abstract String getGenerationName();

	public abstract WorldGenPriority generationPriority();

}
