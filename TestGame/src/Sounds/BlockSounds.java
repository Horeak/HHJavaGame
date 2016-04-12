package Sounds;

import Main.MainFile;
import org.newdawn.slick.Sound;

import java.util.concurrent.CopyOnWriteArrayList;

public class BlockSounds {

	public static CopyOnWriteArrayList<SoundInst> sounds = new CopyOnWriteArrayList<>();

	public static SoundInst addSound(Sound s, float pitch, float volume){
		SoundInst inst = new SoundInst(s, pitch, volume);
		sounds.add(inst);
		return inst;
	}


	public static SoundInst blockSandWalk = addSound(MainFile.game.soundLoader.getSound("sounds/walk", "sandWalk"), 0.9F, 1F);
	public static SoundInst blockDirtWalk = addSound(MainFile.game.soundLoader.getSound("sounds/walk", "sandWalk"), 1F, 1F);
	public static SoundInst blockRockWalk = addSound(MainFile.game.soundLoader.getSound("sounds/walk", "stoneWalk"), 1F, 1F);
	public static SoundInst blockSnowWalk = addSound(MainFile.game.soundLoader.getSound("sounds/walk", "sandWalk"), 0.5F, 1F);

}
