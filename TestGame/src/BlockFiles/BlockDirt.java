package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockDirt extends Block {

	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Dirt";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(107, 62, 33);
	}

	public boolean useBlockTexture() {
		return true;
	}

	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		if(texture == null) texture =  MainFile.game.imageLoader.getImage("blocks","dirt");

		return texture;
	}

	public int getMaxBlockDamage() {
		return 5;
	}


}
