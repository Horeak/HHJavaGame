package Blocks.BlockRender;

import Blocks.Util.Block;
import Items.Item;
import Items.Rendering.ItemRenderer;
import Render.EnumRenderMode;
import WorldFiles.World;
import org.newdawn.slick.Graphics;

public interface BlockRenderer extends ItemRenderer {
	void renderBlock( Graphics g, int rX, int rY, EnumRenderMode renderMode, Block block, boolean right, boolean top );

	default void renderItem( Graphics g, int rX, int rY, EnumRenderMode renderMode, Item item ) {
		if (item instanceof Block) {
			Block block = (Block) item;

			World world = block.world;

			boolean right = world.getBlock(block.x + 1, block.y) == null || world.getBlock(block.x + 1, block.y) != null && !world.getBlock(block.x + 1, block.y).isBlockSolid();
			boolean top = world.getBlock(block.x, block.y - 1) == null || world.getBlock(block.x, block.y - 1) != null && !world.getBlock(block.x, block.y - 1).isBlockSolid();


			renderBlock(g, rX, rY, renderMode, block, right, top);
		}
	}
}
