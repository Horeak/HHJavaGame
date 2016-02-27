package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockCrackedStone extends BlockStone {

	public static Image texture;

	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return texture;
	}

	@Override
	public void loadTextures() {
		texture =  MainFile.game.imageLoader.getImage("blocks","crackedStone");
	}

	@Override
	public String getBlockDisplayName() {
		return "Cracked Stone";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.darkGray;
	}

	public int getMaxBlockDamage() {
		return 15;
	}
}