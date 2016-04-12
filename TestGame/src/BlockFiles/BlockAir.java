package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.Material;
import Utils.TexutrePackFiles.TextureLoader;
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

	@Override
	public void loadTextures(TextureLoader imageLoader) {

	}

	public int getMaxBlockDamage() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Material getBlockMaterial() {
		return Material.AIR;
	}
}
