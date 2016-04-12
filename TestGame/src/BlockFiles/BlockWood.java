package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.IFuel;
import BlockFiles.Util.Material;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockWood extends Block implements IFuel {
	public static Image topTexture =  null;
	public static Image sideTexture =  null;

	@Override
	public String getBlockDisplayName() {
		return "Wood";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(58, 32, 12);
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return side == EnumBlockSide.TOP ? topTexture : sideTexture;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		topTexture =  imageLoader.getImage("blocks","woodTop");
		sideTexture =  imageLoader.getImage("blocks","woodSide");
	}

	@Override
	public boolean canPassThrough() {
		return true;
	}

	@Override
	public int getFuelValue() {
		return 6;
	}

	@Override
	public Material getBlockMaterial() {
		return Material.WOOD;
	}
}
