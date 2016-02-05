package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Blocks.Util.ILightSource;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockTorch extends Block implements ILightSource {

	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Torch";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.yellow;
	}


	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		if(texture == null) texture =  MainFile.game.imageLoader.getImage("blocks","torch");

		return texture;
	}

	@Override
	public int getOutputStrength() {
		return 16;
	}

	public int getMaxBlockDamage() {
		return 2;
	}

	public boolean isBlockSolid() {
		return false;
	}

	public boolean canPassThrough() {
		return true;
	}

}
