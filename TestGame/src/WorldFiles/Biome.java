package WorldFiles;

import java.util.HashMap;
import java.util.Map;

public class Biome implements Cloneable{
	public static HashMap<String, Biome> biomeHashMap = new HashMap<>();

	//Integer 1=x-value, Integer 2=Ground height
	public HashMap<Integer, Integer> heightMap = new HashMap<>();

	public String name;
	public String id;

	public Biome(String id, String name){
		this.name = name;
		this.id = id;
	}

	public static Biome addBiome(Biome b){
		biomeHashMap.put(b.id, b);
		return b;
	}

	public static Biome getInstanceOf(String id){
		for(Map.Entry<String, Biome> ent : biomeHashMap.entrySet()){
			if(ent.getKey().equalsIgnoreCase(id)){
				try {
					return (Biome)ent.getValue().clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}


	private static Biome plainsBiome = addBiome(new Biome("plainsBiome", "Plains"));
}
