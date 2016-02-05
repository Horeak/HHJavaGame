package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockWoodenPlanks extends Block {

	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Wooden Planks";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(58, 32, 12).brighter();
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		if(texture == null) texture = MainFile.game.imageLoader.getImage("blocks","woodenplanks");

		return texture;
	}
}
