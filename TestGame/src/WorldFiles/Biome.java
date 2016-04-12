package WorldFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Blocks;
import NoiseGenerator.PerlinNoiseGenerator;
import NoiseGenerator.SimplexNoiseGenerator;
import WorldGeneration.Structures.*;
import WorldGeneration.Util.StructureGeneration;
import org.newdawn.slick.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Biome implements Cloneable, Serializable{
	public static transient HashMap<String, Biome> biomeHashMap = new HashMap<>();
	public static transient ArrayList<String> biomeIDs = new ArrayList<>();

	public String name;
	public String id;
	public transient StructureGeneration[] worldGens;

	public Biome(String id, String name, StructureGeneration[] worldGens){
		this.name = name;
		this.id = id;
		this.worldGens = worldGens;
	}

	public static Biome addBiome(Biome b){
		biomeHashMap.put(b.id, b);
		biomeIDs.add(b.id);
		return b;
	}

	public static Biome getInstanceOf(String id){
		for(Map.Entry<String, Biome> ent : biomeHashMap.entrySet()){
			if(ent.getKey().equalsIgnoreCase(id)){
				try {
					Biome b = (Biome)ent.getValue().clone();

					return b;
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	transient PerlinNoiseGenerator noiseGenerator;

	//TODO Need to redo this!
	public void generateHeightMap(World world, int start) {
		if (noiseGenerator == null || noiseGenerator.seed != world.worldSeed) {
			noiseGenerator = new PerlinNoiseGenerator(world.worldSeed);
		}

		SimplexNoiseGenerator noise = new SimplexNoiseGenerator(world.worldSeed);

		float height = 0;

		for (int i = 0; i < Chunk.chunkSize; i++)   {
			int pos = start + i;

			height = (float) noise.getNoise(pos, getOctaves(), getFrequency(), getAmplitude()) * 10;

			if(!world.heightHashMap.containsKey(pos)){
				world.heightHashMap.put(pos, Math.round(height));
			}
		}


	}

	public abstract int getOctaves();
	public abstract float getFrequency();
	public abstract float getAmplitude();

	public abstract Image getBackgroundImage( int height);


	private static transient Biome plainsBiome = addBiome(new Biome("plainsBiome", "Plains", new StructureGeneration[]{ new GrassGeneration(), new StoneGeneration()}) {

		@Override
		public int getOctaves() {
			return 4;
		}

		@Override
		public float getFrequency() {
			return 0.2F;
		}

		@Override
		public float getAmplitude() {
			return 0.4F;
		}

		@Override
		public Image getBackgroundImage( int height ) {
			if(height >= 0 && height <= Chunk.chunkSize) return Blocks.blockDirt.getBlockTextureFromSide(EnumBlockSide.FRONT, null, 0,0);
			if(height > Chunk.chunkSize) return Blocks.blockStone.getBlockTextureFromSide(EnumBlockSide.FRONT, null, 0,0);

			return null;
		}
	});

	private static transient Biome snowBiome = addBiome(new Biome("snowBiome", "Snow Biome", new StructureGeneration[]{ new SnowGeneration(), new StoneGeneration(), new SnowLayerGeneration()}) {

		@Override
		public int getOctaves() {
			return 2;
		}

		@Override
		public float getFrequency() {
			return 0.2F;
		}

		@Override
		public float getAmplitude() {
			return 0.3F;
		}

		@Override
		public Image getBackgroundImage( int height ) {
			if(height >= 0 && height <= Chunk.chunkSize) return Blocks.blockDirt.getBlockTextureFromSide(EnumBlockSide.FRONT, null, 0,0);
			if(height > Chunk.chunkSize) return Blocks.blockStone.getBlockTextureFromSide(EnumBlockSide.FRONT, null, 0,0);

			return null;
		}
	});


	private static transient Biome desertBiome = addBiome(new Biome("desertBiome", "Desert", new StructureGeneration[]{ new SandGeneration(), new StoneGeneration()}) {


		@Override
		public int getOctaves() {
			return 8;
		}

		@Override
		public float getFrequency() {
			return 0.2F;
		}

		@Override
		public float getAmplitude() {
			return 0.2F;
		}

		@Override
		public Image getBackgroundImage( int height ) {
			if(height >= 2 && height <= Chunk.chunkSize) return Blocks.blockSand.getBlockTextureFromSide(EnumBlockSide.FRONT, null, 0,0);
			if(height > Chunk.chunkSize) return Blocks.blockStone.getBlockTextureFromSide(EnumBlockSide.FRONT, null, 0,0);

			return null;
		}
	});



	@Override
	public String toString() {
		return "Biome{" +
				"name='" + name + '\'' +
				", id='" + id + '\'' +
				'}';
	}
}
