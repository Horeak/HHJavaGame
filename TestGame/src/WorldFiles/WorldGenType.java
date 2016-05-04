package WorldFiles;

import NoiseGenerator.SimplexNoiseGenerator;

public abstract class WorldGenType {

	//TODO Make this an Enum?

	public enum WorldGenTypes{
		NORMAL_WORLD(new WorldGenType() {
			@Override
			public String getWorldTypeName() {
				return "Normal World";
			}

			@Override
			public boolean overrideBiomeHeight() {
				return false;
			}

			@Override
			public void genWorldHeight( World world, Biome biome, int start ) {

			}
		}),

		FLAT_WORLD(new WorldGenType() {
			@Override
			public String getWorldTypeName() {
				return "Flat World";
			}

			@Override
			public boolean overrideBiomeHeight() {
				return true;
			}

			@Override
			public void genWorldHeight( World world, Biome biome, int start ) {
				for (int x = 0; x < Chunk.chunkSize; x++) {
					world.heightHashMap.put(start + x, 0);
				}
			}
		}),

		MOUNTAIN_WORLD(new WorldGenType() {
			@Override
			public String getWorldTypeName() {
				return "Mountain World";
			}

			@Override
			public boolean overrideBiomeHeight() {
				return true;
			}

			@Override
			public void genWorldHeight( World world, Biome biome, int start ) {
				SimplexNoiseGenerator noise = new SimplexNoiseGenerator(world.worldSeed);

				float height = 0;

				for (int i = 0; i < Chunk.chunkSize; i++)   {
					int pos = start + i;

					height = (float) noise.getNoise(pos, biome.getOctaves() * 2, biome.getFrequency(), biome.getAmplitude() * 4) * 10;

					if(!world.heightHashMap.containsKey(pos)){
						world.heightHashMap.put(pos, Math.round(height));
					}
				}
			}
		});

		public WorldGenType genType;
		WorldGenTypes(WorldGenType genType){
			this.genType = genType;
		}
	}


	public abstract String getWorldTypeName();

	public abstract boolean overrideBiomeHeight();
	public abstract void genWorldHeight(World world, Biome biome, int start);

	public boolean shouldGenBiome(Biome biome){return true;}
}
