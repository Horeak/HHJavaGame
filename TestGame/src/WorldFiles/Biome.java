package WorldFiles;

import Main.MainFile;
import NoiseGenerator.PerlinNoiseGenerator;
import NoiseGenerator.SimplexNoiseGenerator;
import WorldGeneration.Structures.GrassGeneration;
import WorldGeneration.Structures.StoneGeneration;
import WorldGeneration.Util.StructureGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Biome implements Cloneable{
	public static HashMap<String, Biome> biomeHashMap = new HashMap<>();
	public static ArrayList<String> biomeIDs = new ArrayList<>();

	//Integer 1=x-value, Integer 2=Ground height
	public static final HashMap<Integer, Integer> heightMap = new HashMap<>();

	public String name;
	public String id;
	public StructureGeneration[] worldGens;
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

	//TODO Make sure this is actuly generated before world gen
	public abstract void generateHeightMap(World world, int start);


	private static Biome plainsBiome = addBiome(new Biome("plainsBiome", "Plains", new StructureGeneration[]{ new GrassGeneration(), new StoneGeneration() }) {


		//TODO The longer away from start it is the the rougher it gets. Needs fixing! WHY THE FUCK!?!??!
		//TODO Improve heightMap generation!!!!!!!
		@Override
		public void generateHeightMap(World world, int start) {
			PerlinNoiseGenerator noiseGenerator = new PerlinNoiseGenerator(world.worldSeed);

		//	float freq = 0.05F * length;
			for(int x = 0; x < (length * Chunk.chunkSize); x++){
				int xx = x;

				float g = (xx / (float)(length / 0.005F)) * 100;
				double t = noiseGenerator.noise(g) * 10;

				if(!heightMap.containsKey(start + x)) {
					heightMap.put(start + x, -(int) Math.round(t));
				}
			}
		}
	});
}
