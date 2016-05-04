package Items.DebugTools;

import Items.Items;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.Chunk;
import WorldFiles.World;
import WorldGeneration.Structures.Structure;
import org.newdawn.slick.Image;

import java.awt.*;

public class ItemStructureDestroyer  extends Item {
	@Override
	public int getMaxItemDamage() {
		return -1;
	}

	@Override
	public Image getTexture(ItemStack stack) {
		return Items.itemStick.getTexture(null);
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {

	}

	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return "Structure Destoryer";
	}

	@Override
	public boolean useItem(World world, int x, int y, ItemStack stack) {
		if(world.getStructure(x, y) != null){
			Structure st = world.getStructure(x, y);
			Chunk ch = world.getChunk(x, y);

			for(Point t : st.blocks.keySet()){
				world.setBlock(null, (ch.chunkX * Chunk.chunkSize) + t.x, (ch.chunkY * Chunk.chunkSize) + t.y);
				ch.setStructure(null, t.x, t.y);
			}


		}

		return true;
	}
}