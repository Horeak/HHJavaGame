package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockWood extends Block {
	public static Image topTexture =  MainFile.game.imageLoader.getImage("blocks","woodTop");
	public static Image sideTexture =  MainFile.game.imageLoader.getImage("blocks","woodSide");

	@Override
	public String getBlockDisplayName() {
		return "Wood";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(58, 32, 12);
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return side == EnumBlockSide.TOP ? topTexture : sideTexture;
	}
}
