package BlockFiles.Util;

import Sounds.BlockSounds;
import Sounds.SoundInst;

public enum Material {
	AIR(null, null, null),
	DIRT(BlockSounds.blockDirtWalk, null, null),
	SNOW(BlockSounds.blockSnowWalk, null, null),
	SAND(BlockSounds.blockSandWalk, null, null),
	ROCK(BlockSounds.blockRockWalk, null, null),
	MACHINE(null, null, null),
	PLANT(null, null, null),
	WOOD(null, null, null);


	public SoundInst blockWalkSound, blockBreakSound, blockFallSound;
	Material(SoundInst wd, SoundInst bd, SoundInst fd){
		this.blockWalkSound = wd;
		this.blockBreakSound = bd;
		this.blockFallSound = fd;
	}
}
