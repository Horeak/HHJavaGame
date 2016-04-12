package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.ILightSource;
import BlockFiles.Util.Material;
import Utils.RenderUtil;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockTorch extends Block implements ILightSource {


	@Override
	public String getBlockDisplayName() {
		return "Torch";
	}

	@Override
	public Color getDefaultBlockColor() {
		return RenderUtil.getColorWithAlpha(Color.yellow, 0.75F);
	}


	@Override
	public int getOutputStrength() {
		return 16;
	}

	public int getMaxBlockDamage() {
		return 2;
	}

	public boolean isBlockSolid() {
		return false;
	}

	public boolean canPassThrough() {
		return true;
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return null;
	}

	public boolean useBlockTexture() {
		return false;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {

	}


	@Override
	public Material getBlockMaterial() {
		return Material.WOOD;
	}
}
