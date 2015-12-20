package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Utils.RenderUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;


public class BlockStone extends Block {

	public static Image texture = RenderUtil.getBlockImage("stone");

	public BlockStone( int x, int y ) {
		super(x, y);
	}
	public BlockStone() {
		super();
	}

	@Override
	public String getBlockDisplayName() {
		return "Stone Block";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(151, 152, 151);
	}

	public Image getBlockTextureFromSide( EnumBlockSide side ) {
		return texture;
	}

	public int getMaxBlockDamage() {
		return 20;
	}
}
