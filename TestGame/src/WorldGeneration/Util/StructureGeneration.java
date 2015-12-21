package WorldGeneration.Util;
public abstract class StructureGeneration {

	public abstract boolean canGenerate();

	public abstract void generate();

	public abstract String getGenerationName();

	public abstract WorldGenPriority generationPriority();

}
