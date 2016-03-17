package WorldGeneration.Util;

import WorldFiles.Chunk;
import WorldFiles.World;

public abstract class GenerationBase {

	public abstract boolean canGenerate( Chunk chunk, int x, int y );
	public abstract void generate( Chunk chunk, int x, int y );

	public abstract WorldGenPriority generationPriority();

}
