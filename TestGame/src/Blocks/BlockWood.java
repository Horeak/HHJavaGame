package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockWood extends Block {
	public static Image topTexture = RenderUtil.getBlockImage("woodTop");
	public static Image sideTexture = RenderUtil.getBlockImage("woodSide");

	@Override
	public String getBlockDisplayName() {
		return "Wood";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(58, 32, 12);
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side ) {
		return side == EnumBlockSide.TOP ? topTexture : sideTexture;
	}
}
