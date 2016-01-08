package Settings.Values;

public abstract class ConfigOption {

	private String name;
	private Object ob;
	private Object[] obs;
	private int cur = 0;

	public ConfigOption(String name, Object[] values, Object change){
		this.name = name;
		this.obs = values;
		this.ob = change;
	}

	public void change(){
		cur += 1;
		if(cur >= obs.length)
			cur = 0;
		ob = obs[cur];

		setValue(ob);
	}

	public String getName() {
		return name;
	}
	public Object getOb() {
		return ob;
	}
	public Object[] getObs() {
		return obs;
	}
	public abstract void setValue(Object ob);
}
