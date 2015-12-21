package WorldGeneration.Util;
public abstract class GenerationBase {
	public abstract boolean canGenerate( int x, int y );

	public abstract void generate( int x, int y );

	public abstract String getGenerationName();

	public abstract WorldGenPriority generationPriority();

}
