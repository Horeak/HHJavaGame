package Blocks.BlockRender;

import Blocks.Util.Block;
import Items.IItem;
import Items.Rendering.IItemRenderer;
import Render.EnumRenderMode;
import WorldFiles.World;
import org.newdawn.slick.Graphics;

public interface IBlockRenderer extends IItemRenderer {
	void renderBlock( Graphics g, int rX, int rY, EnumRenderMode renderMode, Block block, boolean right, boolean top, boolean renderLighting, boolean isItem );

	default void renderItem( Graphics g, int rX, int rY, EnumRenderMode renderMode, IItem item ) {
		if (item instanceof Block) {
			Block block = (Block) item;

			World world = block.world;
			boolean top = false, right = false;

			if (world != null) {
				right = world.getBlock(block.x + 1, block.y) == null || world.getBlock(block.x + 1, block.y) != null && !world.getBlock(block.x + 1, block.y).isBlockSolid() && block.isBlockSolid();
				top = world.getBlock(block.x, block.y - 1) == null || world.getBlock(block.x, block.y - 1) != null && !world.getBlock(block.x, block.y - 1).isBlockSolid() && block.isBlockSolid();
			}

			renderBlock(g, rX, rY, renderMode, block, right, top, true, false);
		}
	}
}
