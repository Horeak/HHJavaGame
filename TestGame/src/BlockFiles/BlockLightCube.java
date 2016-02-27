package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.ILightSource;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockLightCube extends Block implements ILightSource {


	@Override
	public String getBlockDisplayName() {
		return "Light Cube";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.yellow;
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

}
