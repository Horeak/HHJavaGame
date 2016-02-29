package WorldGeneration.Util;

import WorldFiles.Chunk;
import WorldFiles.World;

public abstract class StructureGeneration {

	public abstract boolean canGenerate( Chunk chunk );

	public abstract void generate( Chunk chunk );

	public abstract String getGenerationName();

	public abstract WorldGenPriority generationPriority();

}
