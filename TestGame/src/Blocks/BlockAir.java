package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

//Used for lighting system
public class BlockAir extends Block {
	@Override
	public String getBlockDisplayName() {
		return "AIR";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.transparent;
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return null;
	}
}
