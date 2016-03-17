package Items.DebugTools;

import Items.Items;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import WorldFiles.Chunk;
import WorldFiles.World;
import org.newdawn.slick.Image;

public class ItemChunkRegenerator extends Item {
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
		return "Chunk Regenerator";
	}

	@Override
	public boolean useItem( World world, int x, int y, ItemStack stack ) {
		Chunk ch = world.getChunk(x, y);
		ch.generated = false;

		ch.generateChunk();

		return true;
	}
}
