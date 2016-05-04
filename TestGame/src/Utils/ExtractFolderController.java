package Utils;

import Main.MainFile;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ExtractFolderController {

	//TODO Add seperate client and server extract folder

	public static void extractFolder(String path) {
		File file = FileUtils.getFolder(path);

		if(!shouldExtract(path)){
			LoggerUtil.out.log(Level.INFO, "Extracted data is up to date!");
			return;
		}

		try {
			//TODO Make it where this will overwrite some files like for example the default texturepack
			org.apache.commons.io.FileUtils.copyDirectory(file, new File(MainFile.game.getFilesSaveLocation() + "/"));
			LoggerUtil.out.log(Level.INFO, "Extracted standard assets!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static boolean shouldExtract(String path){
		return !ConfigValues.isServer;
	}
}
