package Sounds;

import org.newdawn.slick.Sound;

public class SoundInst {

	public Sound sound;
	public float pitch, volumeMulti;

	public SoundInst( Sound sound, float pitch, float volumeMulti ) {
		this.sound = sound;
		this.pitch = pitch;
		this.volumeMulti = volumeMulti;
	}

	public boolean playing(){
		return sound.playing();
	}

	public void stop(){
		sound.stop();
	}

	public void play(){
		sound.play(pitch, volumeMulti);
	}



	@Override
	public String toString() {
		return "SoundInst{" +
				"sound=" + sound +
				", pitch=" + pitch +
				", volumeMulti=" + volumeMulti +
				'}';
	}

	@Override
	public boolean equals( Object o ) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SoundInst)) {
			return false;
		}

		SoundInst soundInst = (SoundInst) o;

		if (Float.compare(soundInst.pitch, pitch) != 0) {
			return false;
		}
		if (Float.compare(soundInst.volumeMulti, volumeMulti) != 0) {
			return false;
		}
		if (sound != null ? !sound.equals(soundInst.sound) : soundInst.sound != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = sound != null ? sound.hashCode() : 0;
		result = 31 * result + (pitch != +0.0f ? Float.floatToIntBits(pitch) : 0);
		result = 31 * result + (volumeMulti != +0.0f ? Float.floatToIntBits(volumeMulti) : 0);
		return result;
	}
}
