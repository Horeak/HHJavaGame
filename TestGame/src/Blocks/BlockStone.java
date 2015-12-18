package Blocks;

import Blocks.Util.Block;
import org.newdawn.slick.Color;


public class BlockStone extends Block {

	//TODO Add noise

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

	public boolean useBlockTexture() {
		return false;
	}
}
