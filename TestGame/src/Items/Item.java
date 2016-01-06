package Items;

import Items.Rendering.IItemRenderer;
import Items.Rendering.ItemRendrerer;
import Items.Utils.IItem;
import Items.Utils.ItemStack;
import Main.MainFile;
import org.newdawn.slick.Image;

import java.util.ArrayList;

public abstract class Item implements IItem {

	public abstract int getMaxItemDamage();
	public abstract Image getTexture();

	public ArrayList<String> getTooltips(ItemStack stack){
		ArrayList<String> tt = new ArrayList<>();

		if(getMaxItemDamage() != -1 && stack.getStackDamage() > 0)
		tt.add("Durability: " + (getMaxItemDamage() - stack.getStackDamage()) + "/" + getMaxItemDamage());

		return tt;
	}


	public void damageItem(ItemStack stack){
		if(stack.getStackDamage() < getMaxItemDamage())
		stack.setStackDamage(stack.getStackDamage() + 1);

		if(stack.getStackDamage() > getMaxItemDamage()){
			int i = 0;
			for(ItemStack stackk : MainFile.currentWorld.player.inventoryItems){
				if(stackk.getItem().equals(this)){
					MainFile.currentWorld.player.setItem(i, null);
					break;
				}

				i += 1;
			}
		}
	}

	@Override
	public IItemRenderer getRender() {
		return ItemRendrerer.staticReferense;
	}

	@Override
	public IItem clone() throws CloneNotSupportedException {
		try {
			Item Item = this.getClass().newInstance();
			return Item;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
