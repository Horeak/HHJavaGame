package Utils;

import Main.MainFile;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class ExtractFolderController {

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

	//TODO Adding new texture when addind new blocks requires deleted extracted or updating version.data. Perhaps check another way if the local is outdated
	public static boolean shouldExtract(String path){
		if(MainFile.inDebugMode)return true;

		File file = FileUtils.getFolder(path);
		File fe = FileUtils.getFile(file + "/version.data");

		if(fe != null){
			DataHandler handler = new DataHandler(fe);
			String version = handler.getString("version");

			DataHandler handler1 = MainFile.game.saveUtil.getDataHandler("version.data");
			String rem = handler1.getString("version");

			return rem == null || !rem.equalsIgnoreCase(version);
		}


		return false;
	}

	//TODO Add a function to check if a folder/file is same as game version (to allow checking for change to the default texturepack for example)
}
