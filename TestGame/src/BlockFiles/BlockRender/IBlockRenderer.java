package BlockFiles.BlockRender;

import BlockFiles.Util.Block;
import BlockFiles.Util.ILightSource;
import Items.Rendering.IItemRenderer;
import Items.Utils.ItemStack;
import Utils.BlockAction;
import Utils.ConfigValues;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Rectangle;

public interface IBlockRenderer extends IItemRenderer {
	void renderBlock( Graphics g, int rX, int rY, Block block, boolean right, boolean top, boolean isItem, World world, int x, int y, int face );

	@Deprecated
	default void renderItem( Graphics g, int rX, int rY, ItemStack item ) {}


	/**
	 * @param face The face of the block to render. (0=right, 1=top, 2=front) (required to fix the clipping on see through blocks!)
	 */
	default void renderBlock(Graphics g, int rX, int rY, ItemStack item, World world, int x, int y, int face){
		if (item.isBlock()) {
			Block block = item.getBlock();

			boolean top = false, right = false;
			boolean isItem = world == null;

			if (!isItem) {
				right = world.getBlock(x + 1, y) == null || world.getBlock(x + 1, y) != null && !world.getBlock(x + 1, y).isBlockSolid() && world.getBlock(x + 1, y).getItemName() != block.getItemName();
				top = world.getBlock(x, y - 1) == null || world.getBlock(x, y - 1) != null && !world.getBlock(x, y - 1).isBlockSolid() && world.getBlock(x, y - 1).getItemName() != block.getItemName();
			}else{
				right = true;
				top = true;
			}


			renderBlock(g, rX, rY, block, right, top, isItem, world, x, y, face);
			renderLighting(g, rX, rY, block, right, top, isItem, world, x, y, face);
			renderBreakImage(g, rX, rY, block, right, top, isItem, world, x, y, face);
		}
	}

	//TODO Fix colored lights!
	default void renderLighting(Graphics g, int rX, int rY, Block block, boolean right, boolean top, boolean isItem, World world, int x, int y, int face )
	{
		Color c = Color.transparent;

		if(!isItem) {
			float t = (float) block.getLightValue(world, x, y) / (float) ILightSource.MAX_LIGHT_STRENGTH;
//			float f = (1 - t) / 8;//This could be an alternative way to be able to control brightness

			Color temp = world.getLightUnit(x, y).getLightColor();
			c = new Color(0, 0, 0, ConfigValues.brightness - t);
		}


		if(face == 0 && right){
			if(!isItem){
				drawSide(g, rX, rY, c);
				drawSide(g, rX, rY, new Color(0F, 0F, 0F, 0.16F));//This is just to add a tiny bit of light to right/top sides to be make distinguishable from each other
			}else{
				drawSide(g, rX, rY, new Color(0F, 0F, 0F, 0.26F));
			}

		}else if(face == 1 && top){
			if(!isItem){
				drawTop(g, rX, rY, c);
				drawTop(g, rX, rY, new Color(1F, 1F, 1F, 0.06F));//This is just to add a tiny bit of light to right/top sides to be make distinguishable from each other
			}else{
				drawTop(g, rX, rY, new Color(1F, 1F, 1F, 0.26F));
			}


		}else if(face == 2){
			if(!isItem){
				drawFront(g, rX, rY, c);
			}else{
				drawFront(g, rX, rY, new Color(1F, 1F, 1F, 0.02F));
			}

		}

	}


	default void renderBreakImage(Graphics g, int rX, int rY, Block block, boolean right, boolean top, boolean isItem, World world, int x, int y, int face )
	{
		if(isItem) return;

		Image im = getBreakImageForBlock(block, x, y);

		if(face != 2 && im != null){
			im = im.getFlippedCopy(true, false);
		}

		if(face == 0 && right){
			drawRightImage(g, rX, rY, im);

		}else if(face == 1 && top){
			drawTopImage(g, rX, rY, im);

		}else if(face == 2){
			drawFrontImage(g, rX, rY, im);
		}

	}

	public static Image getBreakImageForBlock( Block block, int x, int y ) {
		if(x != BlockAction.prevX || y != BlockAction.prevY) return null;

		float tt = ((float) BlockAction.blockDamage / (float) block.getMaxBlockDamage()) * 5;
		int g = (int) tt;

		if(BlockAction.blockDamage > block.getMaxBlockDamage())
			return Utils.TexutrePackFiles.TextureLoader.breakImages[4];

		if (g > 0 && (g - 1) < 5) {
			return Utils.TexutrePackFiles.TextureLoader.breakImages[ g - 1 ];
		}

		return null;
	}

	public static void drawRightImage(Graphics g, int xStart, int yStart, Image image){
		if(image == null) return;

		g.pushTransform();

		g.rotate(xStart + (ConfigValues.size / 2), yStart + ConfigValues.size / 2, 90);
		g.translate(0, -ConfigValues.size);

		image.drawWarped(xStart - (ConfigValues.size / 2), yStart + (ConfigValues.size / 2), xStart + (ConfigValues.size / 2), yStart + (ConfigValues.size / 2), xStart + (ConfigValues.size), yStart + (ConfigValues.size), xStart, yStart + (ConfigValues.size));

		g.popTransform();
	}

	public static void drawTopImage(Graphics g, int xStart, int yStart, Image image){
		if(image == null) return;

		g.pushTransform();

		g.rotate(xStart + (ConfigValues.size / 2), yStart + ConfigValues.size / 2, 180);
		g.translate(-(ConfigValues.size / 2), ConfigValues.size * 1.5F);

		image.drawWarped(xStart, yStart, xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2), xStart + ((ConfigValues.size) * 1.5F), yStart - (ConfigValues.size / 2), xStart + ConfigValues.size, yStart);

		g.popTransform();
	}

	public static void drawFrontImage(Graphics g, int xStart, int yStart, Image image){
		if(image == null) return;

		g.pushTransform();
		image.draw(xStart, yStart, ConfigValues.size, ConfigValues.size);
		g.popTransform();
	}


	//TODO Scale these to the side the texture of that side!
	public static void drawFront( Graphics g, int xStart, int yStart, Color c) {
		g.setColor(c);
		g.fill(new Rectangle(xStart, yStart, ConfigValues.size, ConfigValues.size));
	}

	public static void drawSide( Graphics g, int xStart, int yStart, Color c) {
		xStart += ConfigValues.size;

		Path path = new Path(xStart, yStart);

		path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + (ConfigValues.size / 2), yStart + (ConfigValues.size / 2));
		path.lineTo(xStart, yStart + ConfigValues.size);

		path.close();
		g.setColor(c);
		g.fill(path);
	}

	public static void drawTop( Graphics g, int xStart, int yStart, Color c) {
		Path path = new Path(xStart, yStart);
		path.lineTo(xStart + (ConfigValues.size / 2), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + ((ConfigValues.size * 1.5F)), yStart - (ConfigValues.size / 2));
		path.lineTo(xStart + ConfigValues.size, yStart);
		path.close();
		g.setColor(c);
		g.fill(path);
	}

}
