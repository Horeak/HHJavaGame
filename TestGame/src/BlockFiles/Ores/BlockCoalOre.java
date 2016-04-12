package BlockFiles.Ores;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.BlockStone;
import Items.Items;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockCoalOre extends BlockStone implements IOre {

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
	public void loadTextures(TextureLoader imageLoader) {
		texture = imageLoader.getImage("blocks","coalOre");
	}

	@Override
	public ItemStack getItemDropped( World world, int x, int y ) {
		return new ItemStack(Items.itemCoal, 1 + (MainFile.random.nextInt(3)));
	}
}
