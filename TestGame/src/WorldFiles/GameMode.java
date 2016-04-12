package WorldFiles;

//This will make it easier to add more Gamemodes in the future
public enum GameMode {
	SURVIVAL("Survival", true, false, false),
	CREATIVE("Creative", false, true, true);


	public String name;

	public boolean canPlayerReceiveDamage, canUseCreativeMenu, canTeleport;
	GameMode(String name, boolean canPlayerReceiveDamage, boolean canUseCreativeMenu, boolean canTeleport){
		this.name = name;
		this.canPlayerReceiveDamage = canPlayerReceiveDamage;
		this.canUseCreativeMenu = canUseCreativeMenu;
		this.canTeleport = canTeleport;
	}
}
