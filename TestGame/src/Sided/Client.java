package Sided;

import EntityFiles.Entities.EntityPlayer;
import Interface.Menu;

public class Client {
	private EntityPlayer player;
	private Menu currentMenu;

	public Client(String playerID){
		player = new EntityPlayer(0,0, playerID);
	}


	public EntityPlayer getPlayer() {
		return player;
	}
	public void setPlayer(EntityPlayer player) {
		this.player = player;
	}
	public Menu getCurrentMenu() {
		return currentMenu;
	}
	public void setCurrentMenu(Menu currentMenu) {
		this.currentMenu = currentMenu;
	}
}
