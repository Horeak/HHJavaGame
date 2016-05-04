package WorldGeneration.Util;

import WorldFiles.Chunk;
import WorldFiles.World;

public abstract class StructureGeneration {

	public abstract boolean canGenerate( World world, Chunk chunk );
	public abstract void generate( World world, Chunk chunk );

	public abstract WorldGenPriority generationPriority();

}
