package Settings.Values;

public class Keybinding {

	private String name, id, group;
	private int key;

	public Keybinding( String Name, String id, int key, String group ) {
		this.name = Name;
		this.id = id;
		this.key = key;
		this.group = group;
	}

	public int getKey() {
		return key;
	}

	public void setKey( int key ) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getGroup() {
		return group;
	}

	@Override
	public String toString() {
		return "Keybinding{" +
				"name='" + name + '\'' +
				", id='" + id + '\'' +
				", group='" + group + '\'' +
				", key=" + key +
				'}';
	}
}
