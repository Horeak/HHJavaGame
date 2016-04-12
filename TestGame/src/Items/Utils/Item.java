package Items.Utils;

import Items.Rendering.IItemRenderer;
import Items.Rendering.ItemRendrerer;
import Main.MainFile;
import Utils.LoggerUtil;
import Utils.TexutrePackFiles.TextureLoader;
import org.newdawn.slick.Image;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Item implements IItem, Serializable {

	public abstract int getMaxItemDamage();
	public abstract Image getTexture( ItemStack stack );
	public abstract void loadTextures(TextureLoader imageLoader);

	public int registryValue = -1;

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
				for (ItemStack stackk : MainFile.game.getClient().getPlayer().items) {
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
	public IItem clone() {
		try {
			return this.getClass().newInstance();
		} catch (Exception e) {
			LoggerUtil.exception(e);
		}
		return null;
	}
}
