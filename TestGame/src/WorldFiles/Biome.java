package WorldFiles;

import Main.MainFile;
import NoiseGenerator.PerlinNoiseGenerator;
import NoiseGenerator.SimplexNoiseGenerator;
import WorldGeneration.Structures.CaveGeneration;
import WorldGeneration.Structures.GrassGeneration;
import WorldGeneration.Structures.StoneGeneration;
import WorldGeneration.TreeGeneration;
import WorldGeneration.Util.StructureGeneration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Biome implements Cloneable, Serializable{
	public static transient HashMap<String, Biome> biomeHashMap = new HashMap<>();
	public static transient ArrayList<String> biomeIDs = new ArrayList<>();

	//Integer 1=x-value, Integer 2=Ground height
	public ConcurrentHashMap<Integer, Integer> heightHashMap = new ConcurrentHashMap<>();

	public int getHeight(int x){
		return heightHashMap.get(x);
	}
	public boolean containes(int x){return heightHashMap.containsKey(x);}


	public String name;
	public String id;
	public transient StructureGeneration[] worldGens;
	public int length;

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

	public static Biome getInstanceOf(String id, int legngth){
		for(Map.Entry<String, Biome> ent : biomeHashMap.entrySet()){
			if(ent.getKey().equalsIgnoreCase(id)){
				try {
					Biome b = (Biome)ent.getValue().clone();
					b.length = legngth;

					return b;
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public abstract void generateHeightMap(World world, int start);


	private static transient Biome plainsBiome = addBiome(new Biome("plainsBiome", "Plains", new StructureGeneration[]{ new GrassGeneration(), new StoneGeneration()}) {

		@Override
		public void generateHeightMap(World world, int start) {
			PerlinNoiseGenerator noiseGenerator = new PerlinNoiseGenerator(world.worldSeed);

			for(int x = 0; x < (length * Chunk.chunkSize); x++){

				float g = x * 0.05F;
				double t = noiseGenerator.noise(g) * 10;

				//TODO Make plains biome smoother (It is a plains biome not a hills biome...)
				if(!heightHashMap.containsKey(start + x)) {
					heightHashMap.put(start + x, -(int) Math.round(t));
				}
			}
		}
	});


	@Override
	public String toString() {
		return "Biome{" +
				"name='" + name + '\'' +
				", id='" + id + '\'' +
				", length=" + length +
				'}';
	}
}
