package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Utils.RenderUtil;
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
		if(texture == null) texture = RenderUtil.getBlockImage("dirt");

		return texture;
	}

	public int getMaxBlockDamage() {
		return 5;
	}


}
