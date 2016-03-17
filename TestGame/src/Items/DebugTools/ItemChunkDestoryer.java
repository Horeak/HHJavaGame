package Items.DebugTools;

import Items.Items;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import WorldFiles.Chunk;
import WorldFiles.World;
import org.newdawn.slick.Image;

import java.awt.*;

public class ItemChunkDestoryer extends Item {
	@Override
	public int getMaxItemDamage() {
		return -1;
	}

	@Override
	public Image getTexture( ItemStack stack ) {
		return Items.itemStick.getTexture(null);
	}

	@Override
	public void loadTextures() {

	}

	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return "Chunk Destoryer";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		Chunk ch = world.getChunk(x, y);
		world.worldChunks.put(new Point(ch.chunkX,ch.chunkY), new Chunk(world, ch.chunkX, ch.chunkY));

		return true;
	}
}
