package Items.Tools;

import BlockFiles.Util.Material;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Image;

import java.util.Arrays;

public class ITool extends Item{
	public ToolMaterial material;
	public Material[] effectiveMaterials;

	public String name;

	public String textureLoc;
	public String textureName;

	public int getBlockDamageValue(World world, int x, int y, ItemStack stack ) {
		if(stack.getStackDamage() < getMaxItemDamage() && effectiveMaterials != null ) {
			for(Material m : effectiveMaterials) {
				if (world.getBlock(x, y).getBlockMaterial() == m) {
					return material.effectiveValue;
				}
			}
		}
		return 1;
	}

	public Image texture;

	@Override
	public int getMaxItemDamage() {
		return material.durability;
	}

	@Override
	public Image getTexture( ItemStack stack ) {
		return texture;
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		texture =  imageLoader.getImage(textureLoc,textureName);
	}

	@Override
	public int getItemMaxStackSize() {
		return 1;
	}

	@Override
	public String getItemName() {
		return material.name + " " + name;
	}

	@Override
	public boolean useItem(World world, int x, int y, ItemStack stack) {
		return false;
	}

	@Override
	public String toString() {
		return "ITool{" +
				"textureName='" + textureName + '\'' +
				", material=" + material +
				", effectiveMaterials=" + Arrays.toString(effectiveMaterials) +
				", name='" + name + '\'' +
				", textureLoc='" + textureLoc + '\'' +
				'}';
	}
}
