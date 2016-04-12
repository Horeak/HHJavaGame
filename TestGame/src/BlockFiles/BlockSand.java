package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.ITickBlock;
import BlockFiles.Util.Material;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockSand extends Block {

	public static Image texture;


	@Override
	public int getMaxBlockDamage() {
		return 5;
	}

	@Override
	public String getBlockDisplayName() {
		return "Sand";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.yellow;
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return texture;
	}

	@Override
	public Material getBlockMaterial() {
		return Material.SAND;
	}

	@Override
	public void loadTextures( TextureLoader imageLoader ) {
		texture =  imageLoader.getImage("blocks","sand");
	}

	@Override
	public ITickBlock getTickBlock() {

		//TODO This will mostlikely affect perfromance so look at better ways
		return new ITickBlock() {

			int time = 0;
			boolean hasTicked = false;

			@Override
			public boolean shouldUpdate( World world, int x, int y ) {
				return world.getBlock(x, y + 1) == null;
			}

			@Override
			public int getTimeSinceUpdate( World world, int x, int y ) {
				return time;
			}

			@Override
			public void setTimeSinceUpdate( World world, int x, int y, int i ) {
				time = i;
			}

			@Override
			public void tickBlock( World world, int x, int y ) {

				if(!hasTicked) {
					if (world.getBlock(x, y) instanceof BlockSand) {
						world.setBlock(null, x, y);
					}

					if (world.getBlock(x, y + 1) == null) {
						world.setBlock(Blocks.blockSand, x, y + 1);
					}
					hasTicked = true;
				}
			}


			@Override
			public float blockUpdateDelay() {
				return 0.25F;
			}
		};
	}
}
