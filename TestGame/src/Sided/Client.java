package Sided;

import EntityFiles.Entities.EntityPlayer;

public class Client {
	public boolean hasSpawnedPlayer = false;
	private EntityPlayer player;

	public String playerId;

	public Client(String playerID){
		this.playerId = playerID;

		setPlayer(new EntityPlayer(0,0, playerID));
	}


	public EntityPlayer getPlayer() {
		return player;
	}
	public void setPlayer(EntityPlayer player) {
		this.player = player;
		hasSpawnedPlayer = false;
	}
}
