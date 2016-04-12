package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.Material;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockSandstone extends Block {

	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Sandstone";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.yellow.darker();
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return texture;
	}

	@Override
	public void loadTextures( TextureLoader imageLoader ) {
		texture =  imageLoader.getImage("blocks","sandstone");
	}

	@Override
	public Material getBlockMaterial() {
		return Material.ROCK;
	}
}
