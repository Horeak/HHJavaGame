package Utils.TexutrePackFiles;


import BlockFiles.BlockRender.DefaultBlockRendering;
import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import GameFiles.BaseGame;
import Items.Items;
import Items.Utils.Item;
import Main.MainFile;
import Rendering.AbstractWindowRender;
import Utils.FileUtil;
import Utils.ImageLoader;
import Utils.LoggerUtil;
import org.newdawn.slick.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TextureLoader extends ImageLoader {

	public TextureLoader( BaseGame game ){
		super(game);
	}

	public ArrayList<Image> images = new ArrayList<>();

	public Image getImage(TexturePack pack, String folder, String id ) {
		try {

			FileInputStream stream = new FileInputStream(pack.fileLocation + (folder != null ? "/" + folder : "") + "/" + id + ".png");
			Image im = new Image(stream, id, false);

			if(im == null || stream == null){
				throw new FileNotFoundException();
			}

			images.add(im);
			return im;

		} catch (Exception e) {
			if(e instanceof FileNotFoundException){
				try {
					FileInputStream streamm = new FileInputStream(MainFile.defaultTexturePack.fileLocation + (folder != null ? "/" + folder : "") + "/" + (!id.contains("packImage") ? id : "packImage_Default") + ".png");
					Image imm = new Image(streamm, id, false);

					images.add(imm);
					return imm;
				}catch (Exception ee){
					LoggerUtil.exception(ee);
				}
			}else {
				LoggerUtil.exception(e);
			}
		}

		return null;
	}

	public Image getImage(String folder, String id ) {
		return getImage(MainFile.game.texturePack, folder, id);
	}

	public void reloadTextures(){
		for(Image im : images){
			try{
				im.destroy();
			}catch (Exception e){
				LoggerUtil.exception(e);
			}
		}

		for(TexturePack pack : FileUtil.texturePacks){
			if(pack != null) {
				pack.loadImage();
			}
		}

		if(MainFile.game.getClient() != null && MainFile.game.getClient().getPlayer() != null)
		MainFile.game.getClient().getPlayer().loadTextures();


		for(AbstractWindowRender windowRender : MainFile.game.getAbstractWindowRenderers()){
			windowRender.loadTextures();
		}

		DefaultBlockRendering.breakImages = new Image[]{  MainFile.game.imageLoader.getImage("textures/breakBlock", "break1"),  MainFile.game.imageLoader.getImage("textures/breakBlock", "break2"),  MainFile.game.imageLoader.getImage("textures/breakBlock", "break3"),  MainFile.game.imageLoader.getImage("textures/breakBlock", "break4"),  MainFile.game.imageLoader.getImage("textures/breakBlock", "break5") };

		for(Block bl : Blocks.blockRegistry.keySet()){
			bl.loadTextures();
		}

		for(Item im : Items.ItemRegistry.keySet()){
			im.loadTextures();
		}
	}
}
