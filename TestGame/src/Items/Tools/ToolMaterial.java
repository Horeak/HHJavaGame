package Items.Tools;

public enum ToolMaterial {
	WOOD("Wood", 50, 2),
	STONE("Stone", 100, 4),
	IRON("Iron", 200, 8),
	SILVER("Silver", 400, 6),
	GOLD("Gold", 100, 10),


	NULL(null, -1, -1);


	public String name;

	public int durability;
	public int effectiveValue;

	ToolMaterial(String name, int durability, int effectiveValue){
		this.name = name;
		this.durability = durability;
		this.effectiveValue = effectiveValue;
	}

	@Override
	public String toString() {
		return "ToolMaterial{" +
				"name='" + name + '\'' +
				", durability=" + durability +
				", effectiveValue=" + effectiveValue +
				'}';
	}
}
