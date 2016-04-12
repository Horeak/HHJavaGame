package Sounds;


import Main.MainFile;
import Utils.FileUtils;
import Utils.LoggerUtil;
import Utils.TexutrePackFiles.TexturePack;
import org.newdawn.slick.Sound;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

public class SoundLoader {

	public Sound getSound( TexturePack pack, String folder, String id, String fileType ) {
		try {
			File fe = FileUtils.getFile(pack.fileLocation + (folder != null ? "/" + folder : "") + "/" + id + fileType);
			Sound sd = new Sound(fe.toURL());

			return sd;

		} catch (Exception e) {
			LoggerUtil.exception(e);
		}

		return null;
	}

	public Sound getSound(String folder, String id ) {
		return getSound(MainFile.game.texturePack, folder, id, ".wav");
	}

	public Sound getSound(String folder, String id, String fileType ) {
		return getSound(MainFile.game.texturePack, folder, id, fileType);
	}

	CopyOnWriteArrayList<SoundInst> sds = new CopyOnWriteArrayList<>();

	public void playSound(SoundInst soundInst, boolean overrideOtherSounds){
		for(SoundInst sd : sds){
			if(!sd.playing()){
				sds.remove(sd);
			}
		}

		if(!soundInst.playing()){
			soundInst.play();

			if(overrideOtherSounds){
				for(SoundInst sd : sds){
					sd.stop();
					sds.remove(sd);
				}
			}

			sds.add(soundInst);
		}
	}


	//TODO Reload sounds!
//	public void reloadTextures(){
//		for(Image im : images){
//			try{
//				im.destroy();
//			}catch (Exception e){
//				LoggerUtil.exception(e);
//			}
//		}
//
//		for(TexturePack pack : FileUtil.texturePacks){
//			if(pack != null) {
//				pack.loadImage(this);
//			}
//		}
//
//		if(MainFile.game.getClient() != null && MainFile.game.getClient().getPlayer() != null)
//		MainFile.game.getClient().getPlayer().loadTextures(this);
//
//
//		for(AbstractWindowRender windowRender : MainFile.game.getAbstractWindowRenderers()){
//			windowRender.loadTextures(this);
//		}
//
//		unknownBlock = getImage("blocks", "unknown");
//
//		breakImages = new Image[]{  getImage("textures/breakBlock", "break1"),  getImage("textures/breakBlock", "break2"),  getImage("textures/breakBlock", "break3"),  getImage("textures/breakBlock", "break4"),  getImage("textures/breakBlock", "break5") };
//
//		for(Block bl : Blocks.blockRegistry.keySet()){
//			bl.loadTextures(this);
//		}
//
//		for(Item im : Items.ItemRegistry.keySet()){
//			im.loadTextures(this);
//		}
//	}
}
