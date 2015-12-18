package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockDirt extends Block {

	public static Image texture = RenderUtil.getBlockImage("dirt");

	public BlockDirt( int x, int y ) {
		super(x, y);
	}
	public BlockDirt() {
		super();
	}

	@Override
	public String getBlockDisplayName() {
		return "Dirt Block";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(107, 62, 33);
	}

	public boolean useBlockTexture() {
		return true;
	}

	public Image getBlockTextureFromSide( EnumBlockSide side ) {
		return texture;
	}

}
