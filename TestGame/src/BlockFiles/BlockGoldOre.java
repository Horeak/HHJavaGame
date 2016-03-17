package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockGoldOre extends BlockStone{

	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Gold Ore";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(228, 210, 90);
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return texture;
	}

	@Override
	public void loadTextures() {
		texture = MainFile.game.imageLoader.getImage("blocks","goldOre");
	}
}
