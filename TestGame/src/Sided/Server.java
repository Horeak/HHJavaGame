package Sided;

import WorldFiles.World;

//TODO Find a way which will allow world changes to be sent between client/server
public class Server {
	private World world;

	public World getWorld() {
		return world;
	}
	public void setWorld(World world) {
		this.world = world;
	}
}
