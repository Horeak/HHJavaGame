package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockCrackedStone extends BlockStone {

	public static Image texture;

	public Image getBlockTextureFromSide( EnumBlockSide side ) {
		if (texture == null) {
			texture = RenderUtil.getBlockImage("crackedStone");
		}

		return texture;
	}

	@Override
	public String getBlockDisplayName() {
		return "Cracked Stone";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.darkGray;
	}
}
