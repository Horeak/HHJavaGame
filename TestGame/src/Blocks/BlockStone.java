package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;


public class BlockStone extends Block {

	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Stone";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(151, 152, 151);
	}

	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		if(texture == null) texture =  MainFile.game.imageLoader.getImage("blocks","stone");

		return texture;
	}

	public int getMaxBlockDamage() {
		return 20;
	}
}
