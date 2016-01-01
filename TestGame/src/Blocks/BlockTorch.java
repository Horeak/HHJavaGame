package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Blocks.Util.ILightSource;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockTorch extends Block implements ILightSource {
	@Override
	public String getBlockDisplayName() {
		return "Torch";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.yellow;
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side ) {
		return null;
	}

	public boolean useBlockTexture() {
		return false;
	}

	@Override
	public int getOutputStrength() {
		return 16;
	}

	public int getMaxBlockDamage() {
		return 2;
	}

}
