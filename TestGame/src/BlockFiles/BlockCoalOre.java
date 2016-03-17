package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockCoalOre extends Block{

	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Coal Ore";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(30, 30, 30);
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return texture;
	}

	@Override
	public void loadTextures() {
		texture = MainFile.game.imageLoader.getImage("blocks","coalOre");
	}
}
