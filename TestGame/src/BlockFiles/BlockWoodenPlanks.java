package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.IFuel;
import BlockFiles.Util.Material;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockWoodenPlanks extends Block implements IFuel {

	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Wooden Planks";
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(58, 32, 12).brighter();
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return texture;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		texture = imageLoader.getImage("blocks","woodenplanks");
	}

	@Override
	public int getFuelValue() {
		return 2;
	}

	@Override
	public Material getBlockMaterial() {
		return Material.WOOD;
	}
}
