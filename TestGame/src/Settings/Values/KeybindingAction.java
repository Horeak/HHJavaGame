package Settings.Values;

public abstract class KeybindingAction {
	public Keybinding key;

	public KeybindingAction(Keybinding key){
		this.key = key;
	}

	public abstract void performAction();
}
