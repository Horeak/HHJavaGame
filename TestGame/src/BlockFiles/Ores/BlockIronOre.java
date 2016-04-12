package BlockFiles.Ores;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.BlockStone;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockIronOre extends BlockStone  implements IOre{

	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Iron Ore";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(130, 97, 86);
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return texture;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		texture = imageLoader.getImage("blocks","ironOre");
	}
}
