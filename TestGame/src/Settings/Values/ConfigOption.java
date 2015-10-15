package Settings.Values;

public abstract class ConfigOption {

	public abstract Object getValue();
	public abstract String getValueDisplay();

	public abstract String getOptionDisplayName();
	public String getOptionCodeName(){
		return "config." + getOptionDisplayName().toLowerCase().replace(" ", "_");
	}

	public abstract void changeValue();
}
