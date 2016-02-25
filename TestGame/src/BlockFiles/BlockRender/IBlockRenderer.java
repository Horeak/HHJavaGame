package BlockFiles.BlockRender;

import BlockFiles.Util.Block;
import Items.Rendering.IItemRenderer;
import Items.Utils.ItemStack;
import Render.EnumRenderMode;
import WorldFiles.World;
import org.newdawn.slick.Graphics;

public interface IBlockRenderer extends IItemRenderer {
	void renderBlock( Graphics g, int rX, int rY, EnumRenderMode renderMode, Block block, boolean right, boolean top, boolean renderLighting, boolean isItem, World world, int x, int y, int face );


	/**
	 *
	 * @param g
	 * @param rX
	 * @param rY
	 * @param renderMode
	 * @param item
	 * @param world
	 * @param x
	 * @param y
	 * @param face The face to render. (0=right, 1=top, 2=front) (required to fix the clipping on see through blocks!)
	 */
	default void renderBlock(Graphics g, int rX, int rY, EnumRenderMode renderMode, ItemStack item, World world, int x, int y, int  face){
		if (item.isBlock()) {
			Block block = item.getBlock();

			boolean top = false, right = false;

			if (world != null) {
				right = world.getBlock(x + 1, y) == null || world.getBlock(x + 1, y) != null && !world.getBlock(x + 1, y).isBlockSolid() && block.isBlockSolid();
				top = world.getBlock(x, y - 1) == null || world.getBlock(x, y - 1) != null && !world.getBlock(x, y - 1).isBlockSolid() && block.isBlockSolid();
			}

			renderBlock(g, rX, rY, renderMode, block, right, top, true, false, world, x, y, face);
		}
	}

	@Deprecated
	default void renderItem( Graphics g, int rX, int rY, EnumRenderMode renderMode, ItemStack item ) {}
}
