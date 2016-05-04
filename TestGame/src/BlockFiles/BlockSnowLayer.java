package BlockFiles;

import BlockFiles.BlockRender.DefaultBlockRendering;
import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.BlockRender.IBlockRenderer;
import BlockFiles.Util.Block;
import BlockFiles.Util.ITickBlock;
import BlockFiles.Util.Material;
import Items.Rendering.IItemRenderer;
import Items.Utils.ItemStack;
import Utils.ConfigValues;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class BlockSnowLayer extends Block {

	public static Image topImage, sideImage;

	@Override
	public String getBlockDisplayName() {
		return "Snow layer";
	}

	@Override
	public int getMaxBlockDamage() {
		return 2;
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.white;
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return side == EnumBlockSide.TOP ? topImage : sideImage;
	}

	@Override
	public void loadTextures( TextureLoader imageLoader ) {
		topImage = imageLoader.getImage("blocks", "snowLayerTop");
		sideImage = imageLoader.getImage("blocks", "snowLayerSide");
	}

	@Override
	public Material getBlockMaterial() {
		return Material.SNOW;
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		return super.useItem(world, x, y, stack);
	}


	@Override
	public boolean canPassThrough() {
		return true;
	}

	@Override
	public boolean isBlockSolid() {
		return false;
	}


	public static final IBlockRenderer snowLayerRender = new IBlockRenderer() {


		//TODO Need to fix overlay rendering not working! Maybe add render order or something?
		@Override
		public void renderBlock( Graphics g, int rX, int rY, Block block, boolean right, boolean top, boolean isItem, World world, int x, int y, int face ) {
			if(isItem && face == 1){
				DefaultBlockRendering.staticReference.renderBlock(g, rX, rY + (ConfigValues.size / 2), block, false, true, isItem, world, x, y, face);
			}else if(!isItem){
				DefaultBlockRendering.staticReference.renderBlock(g, rX, rY + ConfigValues.size, block, true, true, isItem, world, x, y, face);
			}
		}
		public void renderLighting(Graphics g, int rX, int rY, Block block, boolean right, boolean top, boolean isItem, World world, int x, int y, int face )
		{
			if(isItem){
				return;
			}


			if(!isItem){
				right = world.getBlock(x + 1, y + 1) == null || world.getBlock(x + 1, y + 1) != null && !world.getBlock(x + 1, y + 1).isBlockSolid() && world.getBlock(x + 1, y + 1).getItemName() != block.getItemName();
			}

			//TODO Need to make the lighting system work based on texture size to prevent this lighting to make the below block darker!
			//DefaultBlockRendering.staticReference.renderLighting(g, rX, rY + ConfigValues.size, block, right, top, isItem, world, x, y, face);
		}


		public void renderBreakImage(Graphics g, int rX, int rY, Block block, boolean right, boolean top, boolean isItem, World world, int x, int y, int face )
		{
			if(isItem) return;

			Image im = IBlockRenderer.getBreakImageForBlock(block, x, y);

			if(face != 2 && im != null){
				im = im.getFlippedCopy(true, false);
			}

			if(face == 1 && top){
				IBlockRenderer.drawTopImage(g, rX, rY + ConfigValues.size, im);
			}

		}
	};
	@Override
	public IItemRenderer getRender() {
		return snowLayerRender;
	}


	//TODO Add snow item
	@Override
	public ItemStack getItemDropped( World world, int x, int y ) {
		return super.getItemDropped(world, x, y);
	}

	public boolean opaqueRender() {
		return true;
	}

	@Override
	public ITickBlock getTickBlock() {
		return new ITickBlock() {
			@Override
			public boolean shouldUpdate( World world, int x, int y ) {
				return world.getBlock(x, y + 1) == null || world.getBlock(x, y + 1) instanceof BlockSnowLayer;
			}

			@Override
			public int getTimeSinceUpdate( World world, int x, int y ) {
				return 0;
			}

			@Override
			public float blockUpdateDelay() {
				return 0;
			}

			@Override
			public void setTimeSinceUpdate( World world, int x, int y, int i ) {

			}

			@Override
			public void tickBlock( World world, int x, int y ) {
				world.breakBlock(x, y);
			}
		};
	}
}
