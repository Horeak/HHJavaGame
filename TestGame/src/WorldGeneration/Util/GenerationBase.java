package WorldGeneration.Util;

import WorldFiles.Chunk;
import WorldFiles.World;

public abstract class GenerationBase {

	public abstract boolean canGenerate( World world, Chunk chunk, int x, int y );
	public abstract void generate( World world, Chunk chunk, int x, int y );

	public abstract WorldGenPriority generationPriority();

}
