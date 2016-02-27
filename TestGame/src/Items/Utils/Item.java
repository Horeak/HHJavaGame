package Items.Utils;

import Items.Rendering.IItemRenderer;
import Items.Rendering.ItemRendrerer;
import Main.MainFile;
import Utils.LoggerUtil;
import org.newdawn.slick.Image;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Item implements IItem, Serializable {

	//TODO Add item registry similar to the blocks

	public abstract int getMaxItemDamage();
	public abstract Image getTexture();
	public abstract void loadTextures();

	public ArrayList<String> getTooltips(ItemStack stack){
		ArrayList<String> tt = new ArrayList<>();

		if(getMaxItemDamage() != -1 && stack.getStackDamage() > 0)
		tt.add("Durability: " + (getMaxItemDamage() - stack.getStackDamage()) + "/" + getMaxItemDamage());

		return tt;
	}


	public void damageItem(ItemStack stack){
		if(stack.getStackDamage() != -1) {
			if (stack.getStackDamage() < getMaxItemDamage())
				stack.setStackDamage(stack.getStackDamage() + 1);

			if (stack.getStackDamage() > getMaxItemDamage()) {
				int i = 0;
				for (ItemStack stackk : MainFile.game.getClient().getPlayer().inventoryItems.values()) {
					if (stackk != null) {
						if (stackk.getItem().equals(this)) {
							MainFile.game.getClient().getPlayer().setItem(i, null);
							break;
						}

						i += 1;
					}
				}
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
			return this.getClass().newInstance();
		} catch (Exception e) {
			LoggerUtil.exception(e);
		}
		return null;
	}
}
