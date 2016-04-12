package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.BlockRender.IBlockRenderer;
import BlockFiles.Util.Block;
import BlockFiles.Util.ILightSource;
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

public class BlockSnow extends Block {
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
		return Blocks.blockSnow.getBlockTextureFromSide(EnumBlockSide.TOP, null, 0, 0);
	}

	@Override
	public void loadTextures( TextureLoader imageLoader ) {

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

	@Override
	public IItemRenderer getRender() {
		return new IBlockRenderer() {
			@Override
			public void renderBlock( Graphics g, int rX, int rY, Block block, boolean right, boolean top, boolean isItem, World world, int x, int y, int face ) {
				IBlockRenderer.drawTopImage(g, rX, rY + ConfigValues.size, Blocks.blockSnow.getBlockTextureFromSide(EnumBlockSide.TOP, null, 0, 0));
			}


			//TODO Dont think lighting or breakblock render is working!
			public void renderLighting(Graphics g, int rX, int rY, Block block, boolean right, boolean top, boolean isItem, World world, int x, int y, int face )
			{
				Color c = Color.transparent;

				if(!isItem) {
					float t = (float) block.getLightValue(world, x, y) / (float) ILightSource.MAX_LIGHT_STRENGTH;
					Color temp = world.getLightUnit(x, y).getLightColor();
					c = new Color(0, 0, 0, ConfigValues.brightness - t);
				}


				 if(face == 1 && top){
					if(!isItem){
						IBlockRenderer.drawTop(g, rX, rY + ConfigValues.size, c);
						IBlockRenderer.drawTop(g, rX, rY+ ConfigValues.size, new Color(1F, 1F, 1F, 0.06F));//This is just to add a tiny bit of light to right/top sides to be make distinguishable from each other
					}else{
						IBlockRenderer.drawTop(g, rX, rY+ ConfigValues.size, new Color(1F, 1F, 1F, 0.26F));
					}
				}

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
	}


	//TODO Add snow item
	@Override
	public ItemStack getItemDropped( World world, int x, int y ) {
		return super.getItemDropped(world, x, y);
	}

	public boolean opaqueRender(){return false;}

	@Override
	public ITickBlock getTickBlock() {
		return new ITickBlock() {
			@Override
			public boolean shouldUpdate( World world, int x, int y ) {
				return world.getBlock(x, y + 1) == null;
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
