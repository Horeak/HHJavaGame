package BlockFiles.Ores;

import BlockFiles.Util.Material;

//This interface is used to indicated that an block is an ore
//Use for Structure rendering in BlockRendering.java
public interface IOre {
	default Material getBlockMaterial() {
		return Material.ORE;
	}
}
