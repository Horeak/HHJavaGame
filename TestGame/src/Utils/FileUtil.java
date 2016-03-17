package Utils;

import Main.MainFile;
import Utils.TexutrePackFiles.TexturePack;
import WorldFiles.EnumWorldSize;
import WorldFiles.World;
import org.newdawn.slick.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class FileUtil {

	public static ArrayList<World> worlds;
	public static ArrayList<TexturePack> texturePacks;

	public static ArrayList<World> getSavedWorlds(){
		ArrayList<World> worlds = new ArrayList<>();

		if(FileUtil.worlds != null){
			worlds = new ArrayList<>(FileUtil.worlds);
		}

		try {

			File file = FileUtils.getFolder(MainFile.game.getFilesSaveLocation() + "saves/");

			if(file.exists() && file.isDirectory()){
				for(File fe : file.listFiles()) {

					if (!isThereWorldWithName(fe.getName())) {
						//TODO Skip world if it is detected as broken/invalid
						if (!fe.isFile()) {
							DataHandler handler = MainFile.game.saveUtil.getDataHandler("saves/" + fe.getName() + "/world.data");

							String name = fe.getName();

							World world = new World(name);
							world.worldName = name;

							//Load some values useful for loading the world
							world.partialLoad("world.data", "worldSeed", world.getClass().getDeclaredField("worldSeed"));
							world.partialLoad("world.data", "dayNumber", world.getClass().getDeclaredField("WorldDay"));

							world.loadProperties();

							worlds.add(world);
						}
					}
				}
			}

		}catch (Exception e){
			LoggerUtil.exception(e);
		}

		return worlds;
	}

	public static boolean  isThereWorldWithName(String t){
		if(worlds != null)
		for(World world : worlds){
			if(world.worldName.equalsIgnoreCase(t)){
				return true;
			}
		}


		return false;
	}

	public static ArrayList<TexturePack> getTexturePacks(){
		ArrayList<TexturePack> texturePacks = new ArrayList<>();

		if(FileUtil.texturePacks != null){
			texturePacks = new ArrayList<>(FileUtil.texturePacks);
		}

//		if(!texturePacks.contains(MainFile.defaultTexturePack))
//		texturePacks.add(MainFile.defaultTexturePack);

		try {

			File file = FileUtils.getFolder(MainFile.game.getFilesSaveLocation() + "texturepacks/");

			if(file.exists() && file.isDirectory()){
				for(File fe : file.listFiles()) {

					if (!fe.isFile()) {
						TexturePack texturePack = new TexturePack(fe.getName(), file + "/" + fe.getName() + "/");
						texturePacks.add(texturePack);

					}
				}
			}

		}catch (Exception e){
			LoggerUtil.exception(e);
		}

		return texturePacks;
	}

	public static TexturePack getTexturePackByName(String name){
		if(texturePacks != null)
		for(TexturePack pack : texturePacks){
			if(pack != null)
			if(pack.name.equalsIgnoreCase(name)){
				return pack;
			}
		}


		return null;
	}

}
