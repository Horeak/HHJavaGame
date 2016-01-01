package WorldGeneration.Util;

import WorldFiles.World;

public abstract class StructureGeneration {

	public abstract boolean canGenerate( World world );

	public abstract void generate( World world );

	public abstract String getGenerationName();

	public abstract WorldGenPriority generationPriority();

}
