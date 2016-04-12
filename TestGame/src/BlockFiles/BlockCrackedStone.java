package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Material;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockCrackedStone extends BlockStone {

	public static Image texture;

	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return texture;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		texture =  imageLoader.getImage("blocks","crackedStone");
	}

	@Override
	public String getBlockDisplayName() {
		return "Cracked Stone";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.darkGray;
	}

	public int getMaxBlockDamage() {
		return 15;
	}

	@Override
	public Material getBlockMaterial() {
		return Material.ROCK;
	}
}
