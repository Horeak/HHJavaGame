package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.Material;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockDirt extends Block {

	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Dirt";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(107, 62, 33);
	}

	public boolean useBlockTexture() {
		return true;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		texture =  imageLoader.getImage("blocks","dirt");
	}

	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return texture;
	}

	public int getMaxBlockDamage() {
		return 5;
	}

	@Override
	public Material getBlockMaterial() {
		return Material.DIRT;
	}
}
