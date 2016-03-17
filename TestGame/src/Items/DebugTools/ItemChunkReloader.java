package Items.DebugTools;

import Items.Items;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import WorldFiles.Chunk;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemChunkReloader extends Item {
	@Override
	public int getMaxItemDamage() {
		return -1;
	}

	@Override
	public Image getTexture() {
		return Items.itemStick.getTexture();
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
		return "Chunk Reloader";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		Chunk ch = world.getChunk(x, y);

		world.unloadChunk(ch.chunkX, ch.chunkY);
		world.loadChunk(ch.chunkX, ch.chunkY);
		return true;
	}
}